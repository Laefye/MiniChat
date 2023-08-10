package com.github.laefye.minichat.chat;

import com.github.laefye.minichat.Request;
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

    private Component format(Request request, Component in) {
        var vaultChat = request.getPlugin().getVaultChat();
        return in.replaceText(
                TextReplacementConfig.builder()
                        .match(Pattern.compile("\\{player\\}"))
                        .replacement(request.getPlayer().displayName())
                        .build()
                )
                .replaceText(
                        TextReplacementConfig.builder()
                                .match(Pattern.compile("\\{message\\}"))
                                .replacement(request.getMessage())
                                .build()
                )
                .replaceText(
                        TextReplacementConfig.builder()
                                .match(Pattern.compile("\\{prefix\\}"))
                                .replacement(vaultChat != null ? vaultChat.getPlayerPrefix(request.getPlayer()) : "")
                                .build()
                )
                .replaceText(
                        TextReplacementConfig.builder()
                                .match(Pattern.compile("\\{suffix\\}"))
                                .replacement(vaultChat != null ? vaultChat.getPlayerSuffix(request.getPlayer()) : "")
                                .build()
                );
    }

    private Component getLore(Request request) {
        var components = lore.stream().map(s -> format(request, miniMessage.deserialize(s))).toList();
        return Component.join(
                JoinConfiguration.builder()
                        .separator(Component.newline())
                        .build(),
                components
        );
    }

    public Component evaluate(Request request) {
        var component = format(request, miniMessage.deserialize(format));
        if (lore != null) {
            component = component.hoverEvent(HoverEvent.showText(getLore(request)));
        }
        return component;
    }
}
