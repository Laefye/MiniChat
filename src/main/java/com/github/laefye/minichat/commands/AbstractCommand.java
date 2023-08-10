package com.github.laefye.minichat.commands;

import com.github.laefye.minichat.MiniChat;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {
    public abstract String permissionName();

    public abstract String argument();

    protected final MiniChat plugin;

    public AbstractCommand(MiniChat plugin) {
        this.plugin = plugin;
    }
}
