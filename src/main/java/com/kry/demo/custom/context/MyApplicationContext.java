package com.kry.demo.custom.context;

import com.google.common.base.Preconditions;
import com.kry.demo.custom.annotation.*;
import com.kry.demo.custom.config.MyBeanDefinition;
import com.kry.demo.custom.enums.ScopeEnum;
import com.kry.demo.custom.exception.*;
import com.kry.demo.custom.utils.PathUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * **********书山有路勤为径**********
 *
 * @author k1rry
 * @date 2021/11/30
 * **********学海无涯苦作舟**********
 */
public class MyApplicationContext {

    private Map<String, MyBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    // 完成一系列流程的实例集合
    private Map<String, Object> singletonBeans = new ConcurrentHashMap<>();

    // 创建了实例，但还没有完成属性注入的集合
    private Map<String, Object> initialBeans = new ConcurrentHashMap<>();

    // 构造方法缺失参数，未能创建实例的集合
    private Map<String, Set<String>> unableInitBeans = new ConcurrentHashMap<>();

    public MyApplicationContext(Class<?> configClass) {
        scan(configClass);

        register();
    }

    /**
     * scan bean by config scan path
     * @param configClass
     */
    private void scan(Class<?> configClass) {
        if (configClass.isAnnotationPresent(MyComponentScan.class)) {
            // analysis config class, get scan path
            MyComponentScan componentScan = configClass.getAnnotation(MyComponentScan.class);
            String scanPath = componentScan.value();

            Preconditions.checkArgument(StringUtils.isNotBlank(scanPath), "scan path must be not blank");

            String filePath = PathUtils.replaceUrlPath(scanPath);

            // get classes by scan path
            final ClassLoader classLoader = this.getClass().getClassLoader();
            URL resource = classLoader.getResource(filePath);
            if (resource == null) {
                throw new MyNoSuchResourceException(filePath);
            }
            File file = new File(resource.getFile());
            List<Class<?>> classes = getBeanClass(file);

            // build beanDefinition
            classes.forEach(clazz -> {
                if (!clazz.isAnnotationPresent(MyComponent.class)) {
                    return;
                }
                String beanName = getBeanName(clazz);
                MyBeanDefinition beanDefinition = new MyBeanDefinition();
                beanDefinition.setBeanClass(clazz);
                beanDefinition.setLazy(clazz.isAnnotationPresent(MyLazy.class));
                if (clazz.isAnnotationPresent(MyScope.class)) {
                    MyScope scope = clazz.getAnnotation(MyScope.class);
                    beanDefinition.setScope(ScopeEnum.PROTOTYPE.toString().equals(scope.value()) ? ScopeEnum.PROTOTYPE : ScopeEnum.SINGLETON);
                } else {
                    beanDefinition.setScope(ScopeEnum.SINGLETON);
                }
                beanDefinitionMap.put(beanName, beanDefinition);
            });
        }
    }

    /**
     * get bean classes by file
     * @param file
     * @return
     */
    private List<Class<?>> getBeanClass(File file) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        List<Class<?>> list = new ArrayList<>();
        List<String> classes = getClass(file);
        classes.forEach(className -> {
            try {
                Class<?> clazz = classLoader.loadClass(className);
                list.add(clazz);
            } catch (ClassNotFoundException e) {
                throw new MyNoSuchClassException(className);
            }
        });
        return list;
    }

    /**
     * get class name list by file name
     * @param file
     * @return
     */
    private List<String> getClass(File file) {
        List<String> list = new ArrayList<>();
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                list.addAll(getClass(f));
            }
        } else {
            String fileName = file.getAbsolutePath();
            if (fileName.endsWith(".class")) {
                String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                className = PathUtils.replace(className, PathUtils.reverse);
                list.add(className);
            }
        }
        return list;
    }

    /**
     * get bean name by class
     * @param clazz
     * @return
     */
    private String getBeanName(Class<?> clazz) {
        MyComponent component = clazz.getAnnotation(MyComponent.class);
        String value = component.value();
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        String clazzName = clazz.getName();
        String name = clazzName.substring(clazzName.lastIndexOf(".") + 1);
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
                Character.isUpperCase(name.charAt(0))){
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    /**
     * register bean
     */
    private void register() {
        beanDefinitionMap.forEach((k, v) -> {
            if (ScopeEnum.SINGLETON.equals(v.getScope()) && !v.isLazy() && !singletonBeans.containsKey(k)) {
                createBean(k, v);
            }
        });
    }

    /**
     * create bean
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object createBean(String beanName, MyBeanDefinition beanDefinition) {
        if (null == beanDefinition) {
            throw new MyNoSuchClassException(beanName);
        }
        // create bean instance
        Object instance = createInstance(beanName, beanDefinition.getBeanClass());
        // autowired field
        autowired(beanName, instance);
        // exec postConstruct
        execPostConstruct(instance);
        // if singleton bean, then add to singleton beans
        if (ScopeEnum.SINGLETON.equals(beanDefinition.getScope())) {
            singletonBeans.put(beanName, instance);
        }
        return instance;
    }

    /**
     * create bean, inferential construction method
     * @param beanClass
     * @return
     */
    private Object createInstance(String beanName, Class<?> beanClass) {
        Constructor<?> constructor = getConstructor(beanClass);
        if (constructor.getParameterCount() == 0) {
            try {
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new MyCreateInstanceException(e.getMessage());
            }
        } else {
            // fill constructor parameters
            Parameter[] parameters = constructor.getParameters();
            Object[] params = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                String fieldName = getFieldName(parameters[i].getType(), parameters[i].getName());
                preprocessParams(beanName, fieldName);
                if (singletonBeans.containsKey(fieldName)) {
                    params[i] = singletonBeans.get(fieldName);
                } else if (initialBeans.containsKey(fieldName)) {
                    params[i] = initialBeans.get(fieldName);
                } else {
                    throw new MyNoSuchClassException(fieldName);
                }
            }
            try {
                return constructor.newInstance(params);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new MyCreateInstanceException(e.getMessage());
            }
        }
    }

    /**
     * selected constructor
     * @param beanClass
     * @return
     */
    private Constructor<?> getConstructor(Class<?> beanClass) {
        Constructor<?>[] constructors = beanClass.getConstructors();
        Constructor<?> initConstructor = null;
        if (constructors.length == 1) {
            initConstructor = constructors[0];
        } else {
            for (Constructor<?> constructor : constructors) {
                if (constructor.isAnnotationPresent(MyAutowired.class)) {
                    initConstructor = constructor;
                    break;
                }
                if (constructor.getParameterCount() == 0) {
                    initConstructor = constructor;
                }
            }
            if (null == initConstructor) {
                throw new MyNoSuchMethodException(beanClass.getName() + ".<init>()");
            }
        }
        return initConstructor;
    }

    /**
     * pre processing constructor parameters
     * @param beanName
     * @param fieldName
     */
    private void preprocessParams(String beanName, String fieldName) {
        if (!singletonBeans.containsKey(fieldName) && !initialBeans.containsKey(fieldName)) {
            Set<String> set = unableInitBeans.get(beanName) != null ? unableInitBeans.get(beanName) : new HashSet<>();
            if (set.contains(fieldName)) {
                throw new MyBeanCurrentlyInCreationException("Error creating bean with name '" + beanName + "'" +
                        ": Requested bean is currently in creation: Is there an unresolvable circular reference?");
            }
            set.add(fieldName);
            unableInitBeans.put(beanName, set);

            createBean(fieldName, beanDefinitionMap.get(fieldName));

            set = unableInitBeans.get(beanName);
            set.remove(fieldName);
            if (!set.isEmpty()) {
                unableInitBeans.put(beanName, set);
            }
        }
    }

    /**
     * autowired field
     * @param beanName
     * @param instance
     */
    private void autowired(String beanName, Object instance) {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(MyAutowired.class)) {
                String fieldName = getFieldName(field.getType(), field.getName());
                if (!singletonBeans.containsKey(fieldName) && !initialBeans.containsKey(fieldName)) {
                    initialBeans.put(beanName, instance);
                    createBean(fieldName, beanDefinitionMap.get(fieldName));
                }
                if (setField(field, fieldName, instance, singletonBeans)) {
                    continue;
                }
                setField(field, fieldName, instance, initialBeans);
            }

        }
    }

    /**
     * get field name, first by target type, secondly by target name
     * @param targetType
     * @param name
     * @return
     */
    private String getFieldName(Class<?> targetType, String name) {
        List<String> list = new ArrayList<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            if (beanDefinition.getBeanClass().equals(targetType)) {
                list.add(beanName);
            }
        });
        if (list.isEmpty()) {
            throw new MyNoSuchClassException(targetType.getName());
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        if (list.contains(name)) {
            return name;
        }
        throw new MyNoUniqueBeanDefinitionException("need " + name + " but find: " + list);
    }

    /**
     * set field value
     * @param fieldName
     * @param instance
     * @param beans
     * @return
     */
    private boolean setField(Field field, String fieldName, Object instance, Map<String, Object> beans) {
        if (!beans.containsKey(fieldName)) {
            return false;
        }
        field.setAccessible(true);
        try {
            field.set(instance, beans.get(fieldName));
        } catch (IllegalAccessException e) {
            throw new MyInvocationException(e.getMessage());
        }
        return true;
    }

    /**
     * exec post construct
     * @param instance
     */
    private void execPostConstruct(Object instance) {
        Method[] methods = instance.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(PostConstruct.class)) {
                continue;
            }
            if (method.getParameters() != null && method.getParameters().length > 0) {
                throw new IllegalStateException("Lifecycle method annotation requires a no-arg method: " + method.getName());
            }
            try {
                method.setAccessible(true);
                method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new MyInvocationException("invoke method error: " + e.getMessage());
            }
        }
    }

    public <T> T getBean(Class<T> clazz) {
        List<String> list = new ArrayList<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            if (beanDefinition.getBeanClass().equals(clazz)) {
                list.add(beanName);
            }
        });
        if (list.size() > 1) {
            throw new MyNoUniqueBeanDefinitionException("need " + clazz.getName() + " but find: " + list);
        }
        return list.isEmpty() ? null : (T) bean(list.get(0));
    }

    public Object getBean(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName)) {
            return null;
        }
        return bean(beanName);
    }

    private Object bean(String beanName) {
        MyBeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (ScopeEnum.PROTOTYPE.equals(beanDefinition.getScope())) {
            return createBean(beanName, beanDefinition);
        }
        if (beanDefinition.isLazy()) {
            lazyInit(beanName, beanDefinition);
        }
        return singletonBeans.get(beanName);
    }

    private void lazyInit(String beanName, MyBeanDefinition beanDefinition) {
        createBean(beanName, beanDefinition);
    }
}
