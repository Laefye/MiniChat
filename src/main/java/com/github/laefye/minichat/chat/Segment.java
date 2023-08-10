package com.github.laefye.minichat.chat;

import com.github.laefye.minichat.Request;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
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

    private String command = null;

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

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
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
                )
                .replaceText(
                        TextReplacementConfig.builder()
                                .match(Pattern.compile("(%.+%)"))
                                .replacement(builder -> request.getPlugin().hasPlaceholderAPI()
                                        ? builder.content(PlaceholderAPI.setPlaceholders(
                                                        request.getPlayer(),
                                                        builder.content()
                                                        )) : Component.text())
                                .build()
                );
    }

    private String format(Request request, String in) {
        return in.replaceAll("\\{player\\}", request.getPlayer().getName());
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
        if (command != null) {
            component = component.clickEvent(ClickEvent.suggestCommand(format(request, command)));
        }
        return component;
    }
}
