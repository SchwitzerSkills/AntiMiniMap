package de.phillip.antiMiniMap.listeners;
import de.phillip.antiMiniMap.AntiMiniMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinListener implements Listener, PluginMessageListener {

    private static final String FORGE_CHANNEL = "fml:hs";
    private static final String FABRIC_CHANNEL = "fabric:registry/sync";
    private static final String FORGE_CHANNEL_LEGACY = "fml:hsl";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(AntiMiniMap.instance.getConfig().getBoolean("active")) {
            checkForMods(player);
        }
    }

    boolean hasForge = false;
    boolean hasFabric = false;
    boolean hasOtherMods = false;

    private void checkForMods(Player player) {

        new BukkitRunnable() {
            @Override
            public void run() {
                if(AntiMiniMap.instance.getConfig().getBoolean("logger")) {
                    Bukkit.getConsoleSender().sendMessage("Final player " + player.getName() + " plugin channels: " + player.getListeningPluginChannels());
                }

                for (String channel : player.getListeningPluginChannels()) {
                    String lowerChannel = channel.toLowerCase();
                    if (lowerChannel.equals(FORGE_CHANNEL.toLowerCase()) || lowerChannel.equals(FORGE_CHANNEL_LEGACY.toLowerCase())) {
                        hasForge = true;
                    } else if (lowerChannel.equals(FABRIC_CHANNEL.toLowerCase())) {
                        hasFabric = true;
                    } else if (lowerChannel.contains("forge") || lowerChannel.contains("fml") ||
                            lowerChannel.contains("fabric") || lowerChannel.contains("mod")) {
                        hasOtherMods = true;
                    }
                }

                if (hasForge) {
                    if(AntiMiniMap.instance.getConfig().getBoolean("logger")){
                        AntiMiniMap.instance.getLogger().info(player.getName() + " has Forge mods installed.");
                    }
                    if(AntiMiniMap.instance.getConfig().getBoolean("otherKickCommand")){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), AntiMiniMap.instance.getConfig().getString("kickCommand").replace("/", "").replace("%kickMessage%", AntiMiniMap.instance.getConfig().getString("kickMessage").replace("&", "§").replace("%player%", player.getName())).replace("%player%", player.getName()));
                    } else {
                        player.kickPlayer(AntiMiniMap.instance.getConfig().getString("kickMessage").replace("&", "§").replace("%player%", player.getName()));
                    }
                }
                if (hasFabric) {
                    if(AntiMiniMap.instance.getConfig().getBoolean("logger")) {
                        AntiMiniMap.instance.getLogger().info(player.getName() + " has Fabric mods installed.");
                    }
                    if(AntiMiniMap.instance.getConfig().getBoolean("otherKickCommand")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), AntiMiniMap.instance.getConfig().getString("kickCommand").replace("/", "").replace("%kickMessage%", AntiMiniMap.instance.getConfig().getString("kickMessage").replace("&", "§").replace("%player%", player.getName())).replace("%player%", player.getName()));
                    } else {
                        player.kickPlayer(AntiMiniMap.instance.getConfig().getString("kickMessage").replace("&", "§").replace("%player%", player.getName()));
                    }
                }
                if (hasOtherMods) {
                    if(AntiMiniMap.instance.getConfig().getBoolean("logger")) {
                        AntiMiniMap.instance.getLogger().info(player.getName() + " has other mods installed.");
                    }
                    if(AntiMiniMap.instance.getConfig().getBoolean("otherKickCommand")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), AntiMiniMap.instance.getConfig().getString("kickCommand").replace("/", "").replace("%kickMessage%", AntiMiniMap.instance.getConfig().getString("kickMessage").replace("&", "§").replace("%player%", player.getName())).replace("%player%", player.getName()));
                    } else {
                        player.kickPlayer(AntiMiniMap.instance.getConfig().getString("kickMessage").replace("&", "§").replace("%player%", player.getName()));
                    }
                }
                if (!hasForge && !hasFabric && !hasOtherMods) {
                    if(AntiMiniMap.instance.getConfig().getBoolean("logger")) {
                        AntiMiniMap.instance.getLogger().info(player.getName() + " does not have any detected mods.");
                    }
                }

                hasForge = false;
                hasFabric = false;
                hasOtherMods = false;
            }
        }.runTaskLater(AntiMiniMap.instance, 20L);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        String lowerChannel = channel.toLowerCase();
        if (lowerChannel.equals(FORGE_CHANNEL.toLowerCase()) || lowerChannel.equals(FORGE_CHANNEL_LEGACY.toLowerCase())) {
            if(AntiMiniMap.instance.getConfig().getBoolean("logger")) {
                AntiMiniMap.instance.getLogger().info(player.getName() + " sent a Forge mod message.");
            }
        } else if (lowerChannel.equals(FABRIC_CHANNEL.toLowerCase())) {
            if(AntiMiniMap.instance.getConfig().getBoolean("logger")) {
                AntiMiniMap.instance.getLogger().info(player.getName() + " sent a Fabric mod message.");
            }
        } else if (lowerChannel.contains("forge") || lowerChannel.contains("fml") ||
                lowerChannel.contains("fabric") || lowerChannel.contains("mod")) {
            if(AntiMiniMap.instance.getConfig().getBoolean("logger")) {
                AntiMiniMap.instance.getLogger().info(player.getName() + " sent a message from an unknown mod channel: " + channel);
            }
        }
    }
}