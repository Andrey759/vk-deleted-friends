package ru.friends.model;

public enum Gender {
        UNDEFINED((byte)0),
        FEMALE((byte)1),
        MALE((byte)2);

    private byte value;

    private Gender(byte value) {
        this.value = value;
    }

    public byte getByte() {
        return value;
    }

    public static Gender getGender(int value) {
        return value == 2 ? MALE : value == 1 ? FEMALE : UNDEFINED;
    }

    public static Gender getGender(byte value) {
        return value == (byte)2 ? MALE : value == (byte)1 ? FEMALE : UNDEFINED;
    }

    public static Gender getGender(boolean value) {
        return value ? MALE : FEMALE;
    }

    public static Gender getGender(String value) {
        return "2".equals(value) ? MALE : "1".equals(value) ? FEMALE : UNDEFINED;
    }

    @Override
    public String toString() {
        return this.getClass().isInstance(Gender.MALE) ? "Муж." : this.getClass().isInstance(Gender.MALE) ? "Жен." : "Транс.";
    }

}