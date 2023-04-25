package pl.pomoku.pomokupluginsrepository.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static pl.pomoku.pomokupluginsrepository.text.Text.strToComp;

public abstract class EzCommand implements CommandExecutor {
    private final CommandInfo commandInfo;

    public EzCommand() {
        commandInfo = getClass().getDeclaredAnnotation(CommandInfo.class);
        Objects.requireNonNull(commandInfo, "Komenda wymaga adnotacji CommandInfo");
    }

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!commandInfo.perm().isEmpty()) {
            if (!sender.hasPermission(commandInfo.perm())) {
                sender.sendMessage(strToComp("<red>Nie masz uprawnień do wykonania tej komendy."));
                return true;
            }
        }
        if (commandInfo.requiresPlayer()) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(strToComp("<red>Musisz być graczem, aby wykonać tą komendę."));
                return true;
            }
            execute((Player) sender, args);
            return true;
        }

        execute(sender, args);
        return true;
    }

    public void execute(Player p, String[] args) {
    }

    public void execute(CommandSender sender, String[] args) {
    }
}
