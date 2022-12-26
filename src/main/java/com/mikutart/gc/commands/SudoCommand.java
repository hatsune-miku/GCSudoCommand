package com.mikutart.gc.commands;

import emu.grasscutter.command.Command;
import emu.grasscutter.command.CommandHandler;
import emu.grasscutter.command.CommandMap;
import emu.grasscutter.game.player.Player;
import com.mikutart.gc.GCSudoPlugin;
import emu.grasscutter.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Commands are comprised of 3 things:
 * 1. The {@link Command} annotation.
 * 2. Implementing the {@link CommandHandler} interface.
 * 3. Implementing the {@link CommandHandler#execute(Player, Player, List)} method.
 *
 * The {@link Command} annotation should contain:
 * 1. A command label. ('example' in this case makes '/example' runnable in-game or on the console)
 * Other optional fields can be found in the {@link Command} annotation interface.
 */

@Command(
        label = "sudo",
        usage = "sudo <command>"
)
public final class SudoCommand implements CommandHandler {
    Plugin plugin = GCSudoPlugin.getInstance();

    /**
     * Called when `/sudo` is run either in-game or on the server console.
     * @param sender The player/console that invoked the command.
     * @param targetPlayer The player that the sender was targeting.
     * @param args The arguments to the command.
     *
     * If {@param sender} is null, the command was run on the server console.
     * {@param targetPlayer} can be null if the sender did not specify a target.
     * For sending feedback to the sender, use {@link CommandHandler#sendMessage(Player, String)}
     */
    @Override
    public void execute(Player sender, Player targetPlayer, List<String> args) {
        String command = String.join(" ", args);
        plugin.getLogger().info(sender.getNickname() + " is running as admin: " + command);

        // Backup permission.
        List<String> permissionReference = sender.getAccount().getPermissions();
        List<String> permissionBackup = new ArrayList<>(permissionReference);

        // Temporarily grant all permissions.
        permissionReference.clear();
        permissionReference.add("*");

        CommandMap.getInstance().invoke(sender, null, command);

        // Restore permissions.
        permissionReference.clear();
        permissionReference.addAll(permissionBackup);

        CommandHandler.sendMessage(sender, "Sudo: command executed.");
    }
}
