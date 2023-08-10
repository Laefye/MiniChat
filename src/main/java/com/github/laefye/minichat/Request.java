package com.github.laefye.minichat;

import io.papermc.paper.event.player.AbstractChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Request {
    private Component message;
    private final MiniChat plugin;
    private final Player player;

    public Component getMessage() {
        return message;
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public Player getPlayer() {
        return player;
    }

    public MiniChat getPlugin() {
        return plugin;
    }

    public Request(Component message, Player player, MiniChat plugin) {
        this.message = message;
        this.plugin = plugin;
        this.player = player;
    }

    public static Request of(AbstractChatEvent event, MiniChat plugin) {
        return new Request(event.message(), event.getPlayer(), plugin);
    }
}
