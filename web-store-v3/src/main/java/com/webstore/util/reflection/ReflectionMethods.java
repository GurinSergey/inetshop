package com.webstore.util.reflection;


import java.lang.reflect.Method;
import java.util.Arrays;


public class ReflectionMethods {
    private static Object invokeMethod(Object object, final String methodName, Class<?>[] paramsTypes, Boolean isGetFirst, Object... arg) {

        Object result = null;

        try {

            // тип объекта, на кот. вызывается метод
            Class<?> targetObjectClass = object.getClass();

            // получаем метод класса
            Method method = findMethodAtClass(targetObjectClass, methodName, paramsTypes, isGetFirst, arg);

            // если есть аргумент
            if (arg != null)
                result = method.invoke(object, arg);
            else
                result = method.invoke(object);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Получить метод класса или его суперкласса по имени и по типу аргуметов
     *
     * @param type       Класс
     * @param methodName Имя метода
     * @return Метод класса
     * @throws NoSuchMethodException
     */
    private static Method findMethodAtClass(final Class<?> type, final String methodName, Class<?>[] paramsTypes, Boolean isGetFirst, Object... arg)
            throws NoSuchMethodException {
        Class<?> currentType = type;


        Method[] methods = type.getMethods();

        // поиск метода с именем methodName в заданном классе и всех его
        // суперклассах
        while (currentType != null) {
            for (Method buff : methods) {

                if (buff.getName().equals(methodName)) {

                    //    return buff;
                    //если поиск не по сигнатуре, возвращаем первый попавшийся
                    if (isGetFirst) {
                        return buff;
                    }

                    if (paramsTypes != null) {
                        Class<?>[] params = buff.getParameterTypes();
                        if (Arrays.deepEquals(paramsTypes, params))
                            return buff;
                    } else if (buff.getParameterTypes().length == 0)
                        return buff;

                }
            }

            currentType = currentType.getSuperclass();
        }

        throw new

                NoSuchMethodException();

    }

    public Object execMethod(String objectName, String methodName, Object... paramsA)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {


        return this.execMethodBySignature(objectName, methodName, null, true, false, paramsA);


    }


    public Object execMethodBySignature(String objectName, String methodName, Class<?>[] paramsTypes, Boolean isGetFirst, Boolean convertToPrimitive, Object... paramsA)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> typeOfParams[];
        Class<?> type;
        Class mClassObject = Class.forName(objectName);
        Object cloneReflectionObject = mClassObject.newInstance();
//ЛАО если в метод прилетели параметры и поиск не по имени первого найденного метода (isGetFirst), тогда разбираем типы параметров
        if ((paramsA != null) && !isGetFirst) {
            if (paramsTypes == null) {
                typeOfParams = new Class<?>[paramsA.length];
                int ind = 0;
                for (Object o : paramsA) {
                    type = o.getClass();
                    if ((type == Integer.class) && convertToPrimitive) {
                        type = Integer.TYPE;
                    }
                    typeOfParams[ind++] = type;//o.getClass();
                }
                paramsTypes = typeOfParams;
            }
        }

        return invokeMethod(cloneReflectionObject, methodName, paramsTypes, isGetFirst, paramsA);


    }

}
