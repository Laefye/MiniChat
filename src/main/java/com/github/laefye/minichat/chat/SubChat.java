package com.github.laefye.minichat.chat;

import com.github.laefye.minichat.MiniChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class SubChat {
    private final String name;
    private final MiniChat plugin;
    private String world = "#all";
    private int range = -1;
    private String prefix = null;
    private ArrayList<Segment> segments = new ArrayList<>();
    private boolean isDefault = false;

    public SubChat(MiniChat plugin, String name) {
        this.name = name;
        this.plugin = plugin;
    }

    public String getName() {
        return name;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getWorld() {
        return world;
    }

    public void setSegments(ArrayList<Segment> segments) {
        this.segments = segments;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public boolean canRead(Player player) {
        return player.hasPermission("minichat.chat." + name);
    }

    public boolean canWrite(Player player) {
        return player.hasPermission("minichat.chat." + name + ".write");
    }

    public boolean isNear(Player sender, Player receiver) {
        return sender.getLocation().distance(receiver.getLocation()) <= range;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Component compose(Player player, Component message) {
        if (prefix != null) {
            message = message.replaceText(
                    TextReplacementConfig.builder()
                            .match(Pattern.quote(prefix))
                            .replacement("")
                            .once()
                            .build()
            );
        }
        var done = Component.text();
        for (var segment : segments) {
            done.append(segment.evaluate(player, message));
        }
        return done.build();
    }

    public Set<Player> getAudience(Player player) {
        var audience = new HashSet<Player>();
        if (world.equals("#current")) {
            if (range < 0) {
                audience.addAll(player.getWorld().getPlayers().stream().filter(this::canRead).toList());
            } else {
                audience.addAll(
                        player.getWorld().getPlayers().stream()
                                .filter(this::canRead)
                                .filter(receiver -> isNear(player, receiver))
                                .toList()
                );
            }
        }
        if (world.equals("#all")) {
            audience.addAll(plugin.getServer().getOnlinePlayers().stream().filter(this::canRead).toList());
        }
        return audience;
    }
}
