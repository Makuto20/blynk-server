package cc.blynk.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * The Blynk Project.
 * Created by Dmitriy Dumanskiy.
 * Created on 2/4/2015.
 */
public class ReflectionUtil {

    /**
     * Used to generate map of class fields where key is field value and value is field name.
     */
    public static Map<Integer, String> generateMapOfValueNameInteger(Class<?> clazz) {
        Map<Integer, String> valuesName = new HashMap<>();
        try {
            for (Field field : clazz.getFields()) {
                valuesName.put((Integer) field.get(int.class), field.getName());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return valuesName;
    }

    /**
     * Used to generate map of class fields where key is field value and value is field name.
     */
    public static Map<Short, String> generateMapOfValueNameShort(Class<?> clazz) {
        Map<Short, String> valuesName = new HashMap<>();
        try {
            for (Field field : clazz.getFields()) {
                if (field.getType().isPrimitive()) {
                    valuesName.put((Short) field.get(short.class), field.getName());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return valuesName;
    }

    /**
     * Used to set any object property with value via reflection.
     */
    public static boolean setProperty(Object object, String fieldName, String fieldValue) throws Exception {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                Class fieldType = field.getType();
                if (fieldType.isArray()) {
                    //expecting String[] only
                    field.set(object, fieldValue.split(StringUtils.BODY_SEPARATOR_STRING));
                } else {
                    //field specific parser
                    if (fieldName.equals("color")) {
                        field.set(object, parseColor(fieldValue));
                    } else {
                        field.set(object, castTo(fieldType, fieldValue));
                    }
                }
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return false;
    }

    private static int parseColor(String fieldValue) {
        int decodedColor = Integer.decode(fieldValue);
        return convertARGBtoRGBA(setAlphaComponent(decodedColor, 255));
    }

    private static int convertARGBtoRGBA(int color) {
        final int a = (color & 0xff000000) >> 24;
        final int r = (color & 0x00ff0000) >> 16;
        final int g = (color & 0x0000ff00) >> 8;
        final int b = (color & 0x000000ff);

        return (r << 24) | (g << 16) | (b << 8) | (a & 0xff);
    }

    public static int setAlphaComponent(int color, int alpha) {
        return (color & 0x00ffffff) | (alpha << 24);
    }

    public static Object castTo(Class type, String value) {
        if (type == byte.class || type == Byte.class) {
            return Byte.valueOf(value);
        }
        if (type == short.class || type == Short.class) {
            return Short.valueOf(value);
        }
        if (type == int.class || type == Integer.class) {
            return Integer.valueOf(value);
        }
        if (type == long.class || type == Long.class) {
            return Long.valueOf(value);
        }
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.valueOf(value);
        }
        return value;
    }
}
