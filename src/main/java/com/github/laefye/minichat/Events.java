package com.github.laefye.minichat;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class Events implements Listener {
    private MiniChat plugin;

    public Events(MiniChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);
        var request = Request.of(event, plugin);
        var subChat = plugin.getVaultChat(request);
        if (subChat == null || !subChat.canWrite(event.getPlayer())) {
            event.getPlayer().sendMessage("Don't have permission UwU");
            return;
        }
        var done = subChat.compose(request);
        var players = subChat.getAudience(request);
        for (var player : players) {
            player.sendMessage(done);
        }
        plugin.getLogger().info("chat: " + PlainTextComponentSerializer.plainText().serialize(done));
    }
}
