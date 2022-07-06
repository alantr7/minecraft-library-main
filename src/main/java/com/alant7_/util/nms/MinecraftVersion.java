package com.alant7_.util.nms;

public enum MinecraftVersion {

    ANY("any"), NOT_FOUND("not_found"),

    v1_18_R1("1.18.1"),
    v1_18_R2("1.18.2"),

    v1_19_R1("1.19"),

    ;

    private final String name;

    MinecraftVersion(String name) {
        this.name = name;
    }

    public String toMinecraft() {
        return name;
    }

}
