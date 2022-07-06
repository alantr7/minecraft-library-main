package com.alant7_.util.command.v2;

import net.md_5.bungee.api.chat.BaseComponent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ExecutedCommand {

    public boolean match(Consumer<ExecutedCommand> action, String... toMatch) {

        return false;

    }

    public boolean match(Consumer<ExecutedCommand> action, Supplier<Boolean> toMatch) {
        return false;
    }

    public void respond(String command) {

    }

    public void respond(BaseComponent... components) {

    }

}
