package com.alant7_.util.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String base();

    String[] params();

    String permission() default "";

    String permissionMessage() default "§cSorry, you do not have permission.";

    boolean op() default false;

    boolean requirePlayer() default false;

    String requirePlayerMessage() default "§cThis command is for players only.";

    int priority() default Priority.AUTO;

    String ANY = "§";

    class Priority {

        public static final int AUTO = 0;

        public static final int LOWER = 1;

        public static final int NORMAL = 2;

        public static final int HIGHER = 3;

    }

}
