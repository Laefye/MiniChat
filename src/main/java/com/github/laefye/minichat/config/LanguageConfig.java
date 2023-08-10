package com.github.laefye.minichat.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Map;

public class LanguageConfig {
    private final Map<String, String> phrases;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public LanguageConfig(Map<String, String> phrases) {
        this.phrases = phrases;
    }

    public Component getPhrase(String name) {
        if (!phrases.containsKey(name)) {
            return Component.text(name);
        }
        return miniMessage.deserialize(phrases.get(name));
    }
}
