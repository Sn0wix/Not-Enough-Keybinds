package net.sn0wix_.notEnoughKeybinds.config;

import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;

import java.io.File;

public abstract class NotEKConfigTemplate {
    public File getFile() {
        return new File(getCommonPath() + getCommonPath() + getFileName());
    }

    public String getCustomPath() {
        return "";
    }

    public String getCommonPath() {
        return "config"+ File.separator + NotEnoughKeybinds.MOD_ID;
    }

    public abstract String getFileName();
}
