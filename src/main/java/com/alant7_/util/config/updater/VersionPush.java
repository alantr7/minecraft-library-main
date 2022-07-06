package com.alant7_.util.config.updater;

public class VersionPush {

    private final ConfigUpdater config;

    private final int version;

    private boolean isUpdated;

    public VersionPush(ConfigUpdater config, int version) {
        this.config = config;
        this.version = version;
        this.isUpdated = config.getVersion() >= version;
    }

    public VersionPush perform(UpdateAction<UpdateAction.UpdateActionAdd> action, String... keys) {
        if (isUpdated)
            return this;

        for (String key : keys)
            action.getExecutor().perform(config.getInternal(), config.getCurrent(), key);

        return this;
    }

    public VersionPush perform(UpdateAction<UpdateAction.UpdateActionMove> action, String keyold, String keynew) {
        if (isUpdated)
            return this;

        action.getExecutor().perform(config.getCurrent(), keyold, keynew);
        return this;
    }

    public VersionPush perform(UpdateAction<UpdateAction.UpdateActionRemove> action, String key) {
        if (isUpdated)
            return this;

        action.getExecutor().perform(config.getCurrent(), key);
        return this;
    }

    void commit() {
        if (!isUpdated) {
            config.getCurrent().set("FileVersion", version);
            isUpdated = true;
        }
    }

}
