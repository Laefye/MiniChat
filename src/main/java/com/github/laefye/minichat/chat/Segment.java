package com.github.laefye.minichat.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

public class Segment {
    private String format = "";

    private ArrayList<String> lore = null;

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setLore(ArrayList<String> lore) {
        this.lore = lore;
    }

    public ArrayList<String> getLore() {
        return lore;
    }

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private Component format(Player player, Component message, Component in) {
        return in.replaceText(
                TextReplacementConfig.builder()
                        .match(Pattern.compile("\\{player\\}"))
                        .replacement(player.displayName())
                        .build()
                )
                .replaceText(
                        TextReplacementConfig.builder()
                                .match(Pattern.compile("\\{message\\}"))
                                .replacement(message)
                                .build()
                );
    }

    private Component getLore(Player player, Component message) {
        var components = lore.stream().map(s -> format(player, message, miniMessage.deserialize(s))).toList();
        return Component.join(
                JoinConfiguration.builder()
                        .separator(Component.newline())
                        .build(),
                components
        );
    }

    public Component evaluate(Player player, Component message) {
        var component = format(player, message, miniMessage.deserialize(format));
        if (lore != null) {
            component = component.hoverEvent(HoverEvent.showText(getLore(player, message)));
        }
        return component;
    }
}
