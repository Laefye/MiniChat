package com.github.laefye.minichat.commands;

import com.github.laefye.minichat.MiniChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MiniChatCommand implements CommandExecutor, TabCompleter {
    private final MiniChat plugin;
    private ArrayList<AbstractCommand> commands = new ArrayList<>();

    public MiniChatCommand(MiniChat plugin) {
        this.plugin = plugin;
        commands.add(new ReloadCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 0) {
            var c = commands.stream().filter(abstractCommand -> abstractCommand.argument().equals(args[0])).findFirst().orElse(null);
            if (c != null && sender.hasPermission(c.permissionName())) {
                return c.onCommand(sender, command, label, skipArgs(args));
            } else {
                sender.sendMessage(plugin.getLanguageConfig().getPhrase("permission"));
            }
        }
        return true;
    }

    private String[] skipArgs(String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> completes = new ArrayList<>();
            for (var c : commands) {
                if (sender.hasPermission(c.permissionName()) && c.argument().startsWith(args[0])) {
                    completes.add(c.argument());
                }
            }
            return completes;
        } else {
            var c = commands.stream().filter(abstractCommand -> abstractCommand.argument().equals(args[0])).findFirst().orElse(null);
            if (c != null && sender.hasPermission(c.permissionName())) {
                return c.onTabComplete(sender, command, label, skipArgs(args));
            }
        }
        return null;
    }
}
