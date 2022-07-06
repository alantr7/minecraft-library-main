package com.alant7_.util.command.tabcomplete;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.function.BiFunction;

class DynamicSuggestion extends Suggestion<BiFunction<CommandSender, String[], List<String>>> {

    DynamicSuggestion(BiFunction<CommandSender, String[], List<String>> commandSenderStringBiFunction) {
        super(commandSenderStringBiFunction);
    }

    @Override
    public int hashCode() {
        return 2;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DynamicSuggestion || obj == Suggestion.DYNAMIC;
    }

}
