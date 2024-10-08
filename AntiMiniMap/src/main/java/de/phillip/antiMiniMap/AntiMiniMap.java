package de.phillip.antiMiniMap;

import de.phillip.antiMiniMap.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AntiMiniMap extends JavaPlugin {

    private static final String FORGE_CHANNEL = "fml:hs";
    private static final String FABRIC_CHANNEL = "fabric:registry/sync";
    private static final String FORGE_CHANNEL_LEGACY = "fml:hsl";
    public static AntiMiniMap instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        instance = this;
        // Plugin startup logic

        getServer().getMessenger().registerOutgoingPluginChannel(this, FORGE_CHANNEL_LEGACY);
        getServer().getMessenger().registerOutgoingPluginChannel(this, FORGE_CHANNEL);
        getServer().getMessenger().registerOutgoingPluginChannel(this, FABRIC_CHANNEL);
        getServer().getMessenger().registerIncomingPluginChannel(this, FORGE_CHANNEL, new JoinListener());
        getServer().getMessenger().registerIncomingPluginChannel(this, FABRIC_CHANNEL, new JoinListener());
        getServer().getMessenger().registerIncomingPluginChannel(this, FORGE_CHANNEL_LEGACY, new JoinListener());
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
