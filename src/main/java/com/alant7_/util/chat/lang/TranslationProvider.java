package com.alant7_.util.chat.lang;

import com.alant7_.util.BukkitPlugin;
import com.alant7_.util.Formatter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.function.UnaryOperator;

public class TranslationProvider {

    private final ConfigurationSection yaml;

    private final Map<String, List<String>> cache = new LinkedHashMap<>();

    public TranslationProvider(ConfigurationSection yaml) {
        this.yaml = yaml;
    }

    public TranslationProvider(File file) {
        this(YamlConfiguration.loadConfiguration(file));
    }

    public ConfigurationSection getYaml() {
        return yaml;
    }

    public void sendTranslation(CommandSender sender, String msg, UnaryOperator<String> transformer) {
        var list = getCached(msg);
        if (transformer != Language.NO_TRANSFORMER) {
            list.forEach(sender::sendMessage);
        } else {
            list.forEach(line -> sender.sendMessage(transformer.apply(line)));
        }
    }

    private List<String> getCached(String key) {
        var cached = cache.get(key.toLowerCase(Locale.ROOT));
        if (cached != null) {
            return cached;
        }

        var object = yaml.get(key);
        var list = new LinkedList<String>();

        if (object instanceof String string) {
            list.add(string);
        } else if (object instanceof List<?> list1) {
            list1.forEach(d -> {
                if (d instanceof String string)
                    list.add(string);
            });
        }

        list.replaceAll(Formatter::formatColors);

        cache.put(key.toLowerCase(Locale.ROOT), list);
        return list;
    }

    public void emptyCache() {
        cache.clear();
    }

}
