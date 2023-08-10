package com.github.laefye.minichat.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

public class Segment {
    private String format = "";

    // TODO: use in future
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

    public Component evaluate(Player player, Component message) {
        return MiniMessage.miniMessage().deserialize(format)
                .replaceText(
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
}
