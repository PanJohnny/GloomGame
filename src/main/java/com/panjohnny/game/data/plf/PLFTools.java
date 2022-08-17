package com.panjohnny.game.data.plf;

import com.panjohnny.game.GameObject;
import lombok.NonNull;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import static com.google.common.primitives.Chars.contains;

/**
 * Static hell isn't it? :D
 * This is basically used to do everything with PLF files.
 */
public final class PLFTools {
    /**
     * This is more lightweight way than other methods. It basically only outputs the x, y, width and height of the object.
     * @param object The object to get the fields from.
     * @return String interpreted version of the object. Such as {@code ObjectName(x,y,width,height)}
     * @see GameObject
     */
    public static String stringifyObject(GameObject object) {
        return object.getClass().getPackageName() + "." + object.getClass().getSimpleName() + "(" + object.getX() + "," + object.getY() + "," + object.getWidth() + "," + object.getHeight() + ")";
    }

    /**
     * Smarty stringifies array of objects. That basically means it will try the {@link #stringify(Object)} but if it fails it will use the {@link #stringifyObject(GameObject)} method.
     * @param objects The objects to stringify.
     * @return String version of the objects in []
     * @see #stringify(Object)
     * @see #stringifyObject(GameObject)
     */
    public static String stringifyArraySmart(Collection<GameObject> objects) {
        if(objects.size() == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (GameObject object : objects) {
            String string = "";
            try {
                string = stringify(object);
            } catch (Exception ignored) {
            }
            string = string.isEmpty() ? stringifyObject(object) : string;
            sb.append(string);
            sb.append(";;");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    /**
     * Turns string into {@link GameObject}
     * @param string The string to objectify.
     * @return The objectified string.
     * @see GameObject
     */
    public static GameObject objectifyString(String string) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String[] args = string.substring(string.indexOf('(') + 1, string.indexOf(')')).split(",");
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int width = Integer.parseInt(args[2]);
        int height = Integer.parseInt(args[3]);

        Class<?> clazz = typeOf(string);
        Constructor<?> constructor = clazz.getConstructor(int.class, int.class, int.class, int.class);
        return (GameObject) constructor.newInstance(x, y, width, height);
    }

    /**
     * Converts string to object. Currently only supports primitive types and strings.
     * @apiNote Only primitive types and strings are supported, objects in objects are not currently implemented.
     * @param string String to convert to object.
     * @return The objectified string.
     * @throws IllegalArgumentException if string was unable to be converted.
     * @implNote I am so happy that I managed to do this.
     */
    public static @NonNull Object convertString(@NonNull String string) throws InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        Class<?> type = typeOf(string);
        if (type.isAssignableFrom(GameObject.class)) {
            return objectifyString(string);
        }
        String[] args = string.substring(string.indexOf('(') + 1, string.indexOf(')')).split(",");
        Object[] convertedArgs = new Object[args.length];
        Object result = null;
        boolean correctInstance;
        for (Constructor<?> constructor : type.getConstructors()) {
            if (constructor.getParameterCount() == args.length) {
                Parameter[] parameters = constructor.getParameters();
                correctInstance = true;
                for (int i = 0, parametersLength = parameters.length; i < parametersLength; i++) {
                    Parameter parameter = parameters[i];
                    try {
                        if (parameter.getType() == int.class) {
                            convertedArgs[i] = Integer.parseInt(args[i]);
                        } else if (parameter.getType() == float.class) {
                            convertedArgs[i] = Float.parseFloat(args[i]);
                        } else if (parameter.getType() == boolean.class) {
                            convertedArgs[i] = Boolean.parseBoolean(args[i]);
                        } else if (parameter.getType() == String.class) {
                            convertedArgs[i] = returnStringToOriginalForm(args[i].trim());
                        }
                    } catch (Exception e) {
                        // incorrect parameter switching to next constructor
                        e.printStackTrace();
                        correctInstance = false;
                        break;
                    }
                }
                if (!correctInstance) {
                    continue;
                }
                result = constructor.newInstance(convertedArgs);
            }
        }

        if (result == null)
            throw new IllegalArgumentException("Could not convert string to object of type " + type.getName());
        return result;
    }

    /**
     * Generates prepend string from type. Such as package.subpackage.ClassName(
     * @param type The type to get the prepend string from.
     * @return Prepend string from type. Such as package.subpackage.ClassName(
     */
    public static String genBasicObjectPrepend(Class<?> type) {
        return type.getPackageName() + "." + type.getSimpleName() + "(";
    }

    /**
     * Stringifies an object. Using reflections.
     * @implSpec This should be used only on those classes that are annotated with {@link PLFAccess}
     * @param object Object that will be converted to string.
     * @return Empty string if there is no constructors, otherwise the string representation.
     * @see PLFAccess
     * @throws IllegalAccessException If the object is not accessible. | If the annotation is used badly.
     */
    public static String stringify(Object object) throws IllegalAccessException, InvocationTargetException {
        // search for constructor with @PLFAccess
        Class<?> clazz = object.getClass();
        Constructor<?>[] constructors = Arrays.stream(clazz.getConstructors()).filter((c) -> c.isAnnotationPresent(PLFAccess.class)).toArray(Constructor[]::new);
        if(constructors.length == 0)
            return "";

        if (constructors.length != 1)
            throw new IllegalArgumentException("Object should only have one constructor annotated with PLFAccess");
        Constructor<?> constructor = constructors[0];
        PLFAccess access = constructor.getAnnotation(PLFAccess.class);
        StringBuilder builder = new StringBuilder(clazz.getPackageName());
        builder.append('.').append(clazz.getSimpleName()).append('(');
        assert constructor.getParameters().length == access.paramNames().length;
        switch (access.mode()) {
            case FIELDS -> {
                for (int i = 0; i < constructor.getParameters().length; i++) {
                    Parameter parameter = constructor.getParameters()[i];
                    String paramName = access.paramNames()[i];
                    boolean found = false;
                    for (Field field : clazz.getFields()) {
                        if (field.getName().equals(paramName) && field.getType() == parameter.getType()) {
                            found = true;

                            builder.append(field.get(object));
                            if (i != constructor.getParameters().length - 1)
                                builder.append(',');
                            break;
                        }
                    }
                    if (!found)
                        throw new IllegalArgumentException("Object doesn't have the right fields as there are in constructor");
                }
            }
            case GETTERS -> {
                for (int i = 0; i < constructor.getParameters().length; i++) {
                    Parameter parameter = constructor.getParameters()[i];
                    String paramName = access.paramNames()[i];
                    boolean found = false;
                    String translatedName = applyAccessGetterLogic(paramName, access.getterPrefix());
                    for (Method method : clazz.getMethods()) {
                        if (method.getName().equals(translatedName) && method.getReturnType() == parameter.getType()) {
                            found = true;

                            builder.append(method.invoke(object));
                            if (i != constructor.getParameters().length - 1)
                                builder.append(',');
                            break;
                        }
                    }
                    if (!found)
                        throw new IllegalArgumentException("Object doesn't have the right fields as there are in constructor");
                }
            }
            case CUSTOM_GETTERS -> {
                for (int i = 0; i < constructor.getParameters().length; i++) {
                    Parameter parameter = constructor.getParameters()[i];
                    String paramName = access.paramNames()[i];
                    boolean found = false;
                    String prefix;
                    if (access.specialGetterPrefixes().length > i)
                        prefix = access.specialGetterPrefixes()[i];
                    else
                        prefix = "";
                    prefix = prefix.isBlank() ? access.getterPrefix() : prefix;
                    String translatedName = applyAccessGetterLogic(paramName, prefix);
                    for (Method method : clazz.getMethods()) {
                        if (method.getName().equals(translatedName) && method.getReturnType() == parameter.getType()) {
                            found = true;

                            builder.append(method.invoke(object));
                            if (i != constructor.getParameters().length - 1)
                                builder.append(',');
                            break;
                        }
                    }
                    if (!found)
                        throw new IllegalArgumentException("Object doesn't have the right fields as there are in constructor");
                }
            }
        }

        builder.append(")");
        return builder.toString();
    }

    /**
     * Used for getters and custom getters for {@link PLFAccess}.
     * @see PLFAccess
     * @param name The name of the variable. Such as {@code x};
     * @param logic The logic to apply to the name. Such as {@code get⌃} (⌃ if the next char is upper case). Please see {@link PLFAccess#getterPrefix()} for more information.
     * @return The translated name. Such as {@code getX}.
     */
    public static String applyAccessGetterLogic(String name, String logic) {
        return logic.substring(0, logic.length() - 1) + (logic.endsWith("⌃") ? name.replaceFirst("[A-z]", String.valueOf(name.toCharArray()[0]).toUpperCase()) : (logic.endsWith("↑") ? name.toUpperCase() : name));
    }

    /**
     * List of illegal characters for {@link #makeStringSuitable(String)}.
     */
    public static final char[] ILLEGAL_STRING_CHARS = new char[]{',', '(', ')', '\\', '}'};

    /**
     * Converts a string to a string that is suitable for use in a string literal. This is done by replacing illegal characters with escape sequence.
     * @param string The string to make suitable.
     * @return Suitable string.
     * @see #returnStringToOriginalForm(String)
     * @see #ILLEGAL_STRING_CHARS
     */
    public static String makeStringSuitable(String string) {
        char[] chars = string.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char a : chars) {
            if (contains(ILLEGAL_STRING_CHARS, a)) {
                // convert to escape sequence
                builder.append("\\u").append(Integer.toHexString(a)).append('}');
            } else
                builder.append(a);
        }

        return builder.toString();
    }

    /**
     * Reverts {@link #makeStringSuitable(String)}
     * @param string String to unescape.
     * @return Original string.
     * @see #makeStringSuitable(String)
     */
    public static String returnStringToOriginalForm(String string) {
        int index = string.indexOf("\\u");
        while (index != -1) {
            int end = string.substring(index + 2).indexOf('}') + index + 2;
            String ch = string.substring(index + 2, end);
            try {
                int a = Integer.parseInt(ch, 16);
                char c = (char) a;
                string = string.replaceFirst(Pattern.quote(string.substring(index, end + 1)), String.valueOf(c));
            } catch (Exception ignored) {
                break;
            }
            index = string.indexOf("\\u");
        }

        return string;
    }

    /**
     * Returns type of string version of object.
     * @param ob The object to get the type of.
     * @return The class that the object is type of.
     * @throws ClassNotFoundException If there is no class matching that object.
     */
    public static Class<?> typeOf(String ob) throws ClassNotFoundException {
        return Class.forName(ob.substring(0, ob.indexOf('(')));
    }
}
