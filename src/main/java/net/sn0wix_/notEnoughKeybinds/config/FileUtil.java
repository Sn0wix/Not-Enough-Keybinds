package net.sn0wix_.notEnoughKeybinds.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    public static NotEKConfigTemplate readConfig(Class<? extends NotEKConfigTemplate> config) {
        try (FileReader reader = new FileReader(config.newInstance().getFile())) {
            return new Gson().fromJson(reader, config);
        } catch (IOException e) {
            NotEnoughKeybinds.LOGGER.error("Can not read config file!");
            e.printStackTrace();

            try {
                return config.newInstance();
            } catch (InstantiationException | IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeConfig(Class<? extends NotEKConfigTemplate> config) {
        try (FileWriter writer = new FileWriter(config.newInstance().getFile())) {
            new GsonBuilder().setPrettyPrinting().create().toJson(config, writer);
        } catch (IOException e) {
            NotEnoughKeybinds.LOGGER.error("Can not write into config file!");
            e.printStackTrace();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static NotEKConfigTemplate getConfig(NotEKConfigTemplate config, Class<? extends NotEKConfigTemplate> configClass) {
        if (!config.getFile().exists()) {
            try {
                new File(config.getCommonPath() + config.getCommonPath()).mkdirs();
                if (config.getFile().createNewFile()) {
                    writeConfig(configClass);
                }

                return configClass.newInstance();
            } catch (IOException ioException) {
                NotEnoughKeybinds.LOGGER.error("Can not create new config file! Check for permissions.");
                ioException.printStackTrace();
                return null;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return readConfig(configClass);
    }
}
