package com.hardcore.main.hardcore;

public enum Nerf {

    HEALTH("LESS_HEALTH"),

    HEAD("NO_HEAD_ARMOR"),

    CHEST("NO_CHEST_ARMOR"),

    LEG("NO_LEG_ARMOR"),

    FOOT("NO_FOOT_ARMOR");

    private final String value;

    Nerf(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
