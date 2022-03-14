package com.webstore;

import com.webstore.util.reflection.ReflectionMethods;

public class Runnable {
    public static void main(String[] args) {
        Class<?> paramsType[];
        paramsType = null;// new Class<?>[]{String.class};
        int i = 777;
        Object[] paramsA = {"test","array"};


        ReflectionMethods reflectionMethods = new ReflectionMethods();

        try {
            reflectionMethods.execMethodBySignature("com.webstore.util.reflection.TestObject", "printsAll", paramsType, false, true, paramsA);
            // reflectionMethods.execMethod("com.webstore.util.reflection.TestObject","printsAll", 1,2);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


    }
}
