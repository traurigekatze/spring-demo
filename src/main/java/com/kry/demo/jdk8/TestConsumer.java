package com.kry.demo.jdk8;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class TestConsumer {

    private void test() {
        Consumer<String> consumer = s -> {
            System.out.println("accept s: " + s);
            System.out.println("accept s.length(): " + s.length());
        };
        exec(consumer);
    }

    private void exec(Consumer<String> consumer) {
        List<String> names = Arrays.asList("kry", "john");
        names.forEach(consumer);
    }

    public static void main(String[] args) {
        new TestConsumer().test();
    }

}
