package com.github.laefye.minichat;

import com.github.laefye.minichat.commands.MiniChatCommand;
import com.github.laefye.minichat.config.Configuration;
import com.github.laefye.minichat.chat.SubChat;
import com.github.laefye.minichat.config.LanguageConfig;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.chat.Chat;

import java.util.ArrayList;

public final class MiniChat extends JavaPlugin {
    private ArrayList<SubChat> chats;
    private LanguageConfig languageConfig;

    @Override
    public void onEnable() {
        reloadMiniChatConfig();
        getServer().getPluginManager().registerEvents(new Events(this), this);
        setupVaultChat();
        setupPlaceholderAPI();
        var command = new MiniChatCommand(this);
        getCommand("minichat").setExecutor(command);
        getCommand("minichat").setTabCompleter(command);
    }

    private boolean placeholderAPI = false;

    public void setupPlaceholderAPI() {
        placeholderAPI = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (placeholderAPI) {
            getLogger().info("PlaceholderAPI installed");
        }
    }

    public boolean hasPlaceholderAPI() {
        return placeholderAPI;
    }

    private Chat chat = null;

    public Chat getVaultChat() {
        return chat;
    }

    private void setupVaultChat() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        getLogger().info("Vault installed");
    }

    public void reloadMiniChatConfig() {
        var configuration = new Configuration(this);
        configuration.load();
        chats = configuration.getChats();
        for (var chat : chats) {
            getLogger().info("Loaded: " + chat.getName() + (chat.isDefault() ? " (default)" : ""));
        }
        languageConfig = configuration.getLanguageConfig();
    }

    public ArrayList<SubChat> getChats() {
        return chats;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public SubChat getDefaultChat() {
        return chats.stream().filter(SubChat::isDefault).findFirst().orElse(chats.stream().findAny().orElse(null));
    }

    public SubChat getVaultChat(Request request) {
        var plain = PlainTextComponentSerializer.plainText().serialize(request.getMessage());
        for (var chat : chats) {
            if (chat.getPrefix() != null && plain.startsWith(chat.getPrefix())) {
                return chat;
            }
        }
        return getDefaultChat();
    }

    public LanguageConfig getLanguageConfig() {
        return languageConfig;
    }
}
