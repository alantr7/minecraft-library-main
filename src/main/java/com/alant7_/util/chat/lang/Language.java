package com.alant7_.util.chat.lang;

import org.bukkit.command.CommandSender;
import java.util.function.UnaryOperator;

public class Language {

    private static TranslationProvider provider;

    public static final UnaryOperator<String> NO_TRANSFORMER = input -> input;

    public static void setProvider(TranslationProvider provider) {
        Language.provider = provider;
    }

    public static void sendTranslation(CommandSender sender, String lang) {
        sendTranslation(sender, lang, NO_TRANSFORMER);
    }

    public static void sendTranslation(CommandSender sender, String lang, UnaryOperator<String> transformer) {
        provider.sendTranslation(sender, lang, transformer);
    }

}
