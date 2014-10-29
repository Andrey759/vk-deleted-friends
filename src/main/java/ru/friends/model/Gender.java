package ru.friends.model;

public enum Gender {
        MALE((byte)0),
        FEMALE((byte)1);

    private byte value;

    private Gender(byte value) {
        this.value = value;
    }

    public byte getByte() {
        return value;
    }

    public static Gender getGender(int value) {
        return value == 1 ? FEMALE : value == 0 ? MALE : null;
    }

    public static Gender getGender(byte value) {
        return value == (byte)1 ? FEMALE : value == (byte)0 ? MALE : null;
    }

    public static Gender getGender(boolean value) {
        return value ? FEMALE : MALE;
    }

    public static Gender getGender(String value) {
        return "1".equals(value) ? FEMALE : "0".equals(value) ? MALE : null;
    }

    }