package ru.friends.converter;

public class EnumConverter {

    public static <ENUM extends Enum> ENUM getByNumber(int number, Class<ENUM> enumClass) {
        ENUM[] values = enumClass.getEnumConstants();

        if (number >= 0 && number < values.length) {
            return values[number];
        } else {
            throw new IllegalArgumentException(String.format(
                    "Unknown %s number: %d", enumClass.getSimpleName(), number));
        }
    }

    public static <ENUM extends Enum> ENUM getByName(String name, Class<ENUM> enumClass) {
        ENUM[] values = enumClass.getEnumConstants();
        if (name == null) {
            return values[0];
        }

        for (ENUM value : values) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }

        throw new IllegalArgumentException(String.format(
                "Unknown %s name: %s", enumClass.getSimpleName(), name));
    }

}
