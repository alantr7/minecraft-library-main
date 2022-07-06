package com.alant7_.util.nms;

import org.bukkit.Bukkit;

public class NMSUtil {

    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

}
