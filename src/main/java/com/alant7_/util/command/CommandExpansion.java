package com.alant7_.util.command;

import com.alant7_.util.command.tabcomplete.TabCompleter;

public interface CommandExpansion {

    default void init() {}

    default void init(CommandExecutor executor) {}

}
