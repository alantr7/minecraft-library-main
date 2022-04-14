package com.alant7_.util.data.managers.players;

public abstract class OfflinePlayerData {

    private boolean isNew = false;

    public final boolean isNew() {
        return isNew;
    }

    void setNew(boolean b) {
        isNew = b;
    }

}
