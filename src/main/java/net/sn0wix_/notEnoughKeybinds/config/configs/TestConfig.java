package net.sn0wix_.notEnoughKeybinds.config.configs;

import net.sn0wix_.notEnoughKeybinds.config.NotEKConfigTemplate;

public class TestConfig extends NotEKConfigTemplate {
    public boolean bl = true;

    @Override
    public String getFileName() {
        return "testConfig.json";
    }
}
