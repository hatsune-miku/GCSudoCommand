package com.mikutart.gc;

import com.google.gson.Gson;
import com.mikutart.gc.commands.SudoCommand;
import com.mikutart.gc.objects.PluginConfig;
import emu.grasscutter.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * The Grasscutter plugin template.
 * This is the main class for the plugin.
 */
public final class GCSudoPlugin extends Plugin {
    private static GCSudoPlugin instance;

    public static GCSudoPlugin getInstance() {
        return instance;
    }

    private PluginConfig config;

    private void ensureConfiguration() {
        // Get the configuration file.
        File config = new File(this.getDataFolder(), "config.json");

        // Load the configuration.
        try {
            if (!config.exists() && !config.createNewFile()) {
                this.getLogger().error("Failed to create config file.");
            } else {
                try (FileWriter writer = new FileWriter(config)) {
                    InputStream configStream = this.getResource("config.json");
                    if (configStream == null) {
                        this.getLogger().error("Failed to save default config file.");
                    } else {
                        writer.write(
                            new BufferedReader(
                                new InputStreamReader(
                                    configStream,
                                    StandardCharsets.UTF_8
                                )
                            ).lines().collect(
                                Collectors.joining("\n")
                            )
                        );
                        writer.close();

                        this.getLogger().info("Saved default config file.");
                    }
                }
            }

            // Put the configuration into an instance of the config class.
            Gson gson = new Gson().newBuilder().disableHtmlEscaping().create();
            this.config = gson.fromJson(new FileReader(config, StandardCharsets.UTF_8), PluginConfig.class);
        } catch (IOException exception) {
            this.getLogger().error("Failed to create config file.", exception);
        }
    }

    /**
     * This method is called immediately after the plugin is first loaded into system memory.
     */
    @Override
    public void onLoad() {
        instance = this;

        ensureConfiguration();

        getLogger().info("Initializing.");
    }

    /**
     * This method is called before the servers are started, or when the plugin enables.
     */
    @Override
    public void onEnable() {
        // Register commands.
        getHandle().registerCommand(new SudoCommand());

        getLogger().info("Now ENABLED.");
    }

    /**
     * This method is called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        this.getLogger().info("Now DISABLED.");
    }

    public PluginConfig getConfig() {
        return this.config;
    }
}
