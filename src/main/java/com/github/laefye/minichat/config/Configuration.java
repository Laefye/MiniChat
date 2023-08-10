package com.github.laefye.minichat.config;

import com.github.laefye.minichat.MiniChat;
import com.github.laefye.minichat.chat.Segment;
import com.github.laefye.minichat.chat.SubChat;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Configuration {
    private final MiniChat plugin;
    private final ArrayList<SubChat> chats = new ArrayList<>();
    public Configuration(MiniChat plugin) {
        this.plugin = plugin;
    }

    private YamlConfiguration checkFile() {
        var file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return config;
    }

    public void load() {
        var config = checkFile();
        var configMap = Huita.getMap(config.getValues(false));
        var chatsMap = (Map<String, Object>) configMap.get("chats");
        for (var chatsEntry : chatsMap.entrySet()) {
            chats.add(getChat(chatsEntry.getKey(), (Map<String, Object>) chatsEntry.getValue()));
        }
    }

    private SubChat getChat(String name, Map<String, Object> chat) {
        var range = (int) chat.getOrDefault("range", -1);
        var world = (String) chat.getOrDefault("world", "#all");
        var prefix = (String) chat.get("prefix");
        var isDefault = (boolean) chat.getOrDefault("default", false);
        var segments = getSegments((ArrayList<Map<String, Object>>) chat.getOrDefault("segments", new ArrayList<>()));
        var ch = new SubChat(plugin, name);
        ch.setRange(range);
        ch.setWorld(world);
        ch.setPrefix(prefix);
        ch.setSegments(segments);
        ch.setDefault(isDefault);
        return ch;
    }

    private ArrayList<Segment> getSegments(ArrayList<Map<String, Object>> segments) {
        var list = new ArrayList<Segment>();
        for (var segment : segments) {
            list.add(getSegment(segment));
        }
        return list;
    }

    private Segment getSegment(Map<String, Object> segment) {
        var format = (String) segment.getOrDefault("format", "");
        var s = new Segment();
        s.setFormat(format);
        return s;
    }

    public ArrayList<SubChat> getChats() {
        return chats;
    }
}
