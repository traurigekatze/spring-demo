package com.kry.demo.custom.context;

import com.google.common.base.Preconditions;
import com.kry.demo.custom.annotation.MyComponent;
import com.kry.demo.custom.annotation.MyComponentScan;
import com.kry.demo.custom.annotation.MyLazy;
import com.kry.demo.custom.annotation.MyScope;
import com.kry.demo.custom.config.MyBeanDefinition;
import com.kry.demo.custom.enums.ScopeEnum;
import com.kry.demo.custom.exception.MyNoSuchClassException;
import com.kry.demo.custom.utils.PathUtils;
import com.kry.demo.custom.exception.MyNoSuchResourceException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public MyApplicationContext(Class configClass) {
        if (configClass.isAnnotationPresent(MyComponentScan.class)) {
            // analysis config class, get scan path
            MyComponentScan componentScan = (MyComponentScan) configClass.getAnnotation(MyComponentScan.class);
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
            List<Class> classes = getBeanClass(file);

            // build beanDefinition
            for (Class clazz : classes) {
                if (!clazz.isAnnotationPresent(MyComponent.class)) {
                    continue;
                }
                String beanName = getBeanName(clazz);
                MyBeanDefinition beanDefinition = new MyBeanDefinition();
                beanDefinition.setBeanClass(clazz);
                beanDefinition.setLazy(clazz.isAnnotationPresent(MyLazy.class));
                if (clazz.isAnnotationPresent(MyScope.class)) {
                    MyScope scope = (MyScope) clazz.getAnnotation(MyScope.class);
                    beanDefinition.setScope(ScopeEnum.prototype.toString().equals(scope.value()) ? ScopeEnum.prototype : ScopeEnum.singleton);
                } else {
                    beanDefinition.setScope(ScopeEnum.singleton);
                }
                beanDefinitionMap.put(beanName, beanDefinition);
            }
            beanDefinitionMap.forEach((k, v) -> {
                System.out.println(k + "   " + v.getBeanClass());
            });
        }
    }

    /**
     * get bean classes by file
     * @param file
     * @return
     */
    private List<Class> getBeanClass(File file) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        List<Class> list = new ArrayList<>();
        List<String> classes = getClass(file);
        for (String className : classes) {
            try {
                Class<?> clazz = classLoader.loadClass(className);
                list.add(clazz);
            } catch (ClassNotFoundException e) {
                throw new MyNoSuchClassException(className);
            }
        }
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
    private String getBeanName(Class clazz) {
        MyComponent component = (MyComponent) clazz.getAnnotation(MyComponent.class);
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




}
