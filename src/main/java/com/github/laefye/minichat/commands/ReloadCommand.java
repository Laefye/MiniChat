package com.github.laefye.minichat.commands;

import com.github.laefye.minichat.MiniChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReloadCommand extends AbstractCommand {
    public ReloadCommand(MiniChat plugin) {
        super(plugin);
    }

    @Override
    public String permissionName() {
        return "minichat.command.reload";
    }

    @Override
    public String argument() {
        return "reload";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        plugin.reloadMiniChatConfig();
        sender.sendMessage("Config reloaded");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
