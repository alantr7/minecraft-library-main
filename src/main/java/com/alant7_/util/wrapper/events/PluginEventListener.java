package com.alant7_.util.wrapper.events;

import com.alant7_.util.BukkitPlugin;
import com.alant7_.util.reflections.Pair;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PluginEventListener implements Listener {

    private final BukkitPlugin plugin;

    private final Listener LISTENER = new Listener() {};

    public PluginEventListener(BukkitPlugin plugin) {
        this.plugin = plugin;
        init();
    }

    public void init() {}

    public <T extends Event> void on(Class<T> eventClass, Consumer<T> callback) {
        on(eventClass, callback, EventPriority.NORMAL, false);
    }

    public <T extends Event> void on(Class<T> eventClass, Consumer<T> callback, EventPriority priority) {
        on(eventClass, callback, priority, false);
    }

    public <T extends Event> void on(Class<T> eventClass, Consumer<T> callback, EventPriority priority, boolean ignoreCancelled) {
        try {
            var handlerListResult = getHandlerList(eventClass);
            var handlerList = handlerListResult.getValue();

            EventExecutor consumer = handlerListResult.getKey() == eventClass
                    ? (listener, event) -> callback.accept((T) event)
                    : (listener, event) -> {
                if (eventClass.isInstance(event))
                    callback.accept(eventClass.cast(event));
            };

            handlerList.register(
                    new RegisteredListener(
                            LISTENER,
                            consumer,
                            priority,
                            plugin,
                            ignoreCancelled
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Pair<Class<?>, HandlerList> getHandlerList(Class<?> clazz) {
        try {
            return Pair.of(clazz, (HandlerList) clazz.getDeclaredMethod("getHandlerList").invoke(null));
        } catch (NoSuchMethodException e1) {
            return clazz != Object.class ? getHandlerList(clazz.getSuperclass()) : null;
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

}
