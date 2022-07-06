package com.alant7_.util.command.v2;

import com.alant7_.util.command.v2.ExecutedCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class CommandBuilder {

    private final String base;

    private final List<BiConsumer<ExecutedCommand, String[]>> consumers = new ArrayList<>();

    public CommandBuilder(String base) {
        this.base = base;
    }

    public void perform(BiConsumer<ExecutedCommand, String[]> consumer) {

    }

}
