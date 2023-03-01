package be.wamberchies.utils.commands;

import be.wamberchies.Main;
import be.wamberchies.commands.*;
import be.wamberchies.commands.compteur.CommandSetComptor;
import be.wamberchies.commands.leaderboard.CommandAddPoints;
import be.wamberchies.commands.leaderboard.CommandRemovePoint;
import be.wamberchies.commands.leaderboard.CommandResetAll;
import be.wamberchies.commands.leaderboard.CommandSetPoints;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Arrays;

public class MessageManager {

    private static CommandRegistry registry = new CommandRegistry();

    static {
        registry.addCommand(new Command(
                "addpoints",
                "Ajouter des points à un utilisateur",
                new CommandAddPoints(),
                "addPoints"
        ));
        registry.addCommand(new Command(
                "removepoints",
                "Retirer des points à un utilisateur",
                new CommandRemovePoint(),
                "removePoints"
        ));
        registry.addCommand(new Command(
                "setpoints",
                "Set les points d'un utilisateur",
                new CommandSetPoints(),
                "setPoints"
        ));
        registry.addCommand(new Command(
                "resetall",
                "Set le rang d'un utilisateur",
                new CommandResetAll(),
                "resetAll"
        ));
        registry.addCommand(new Command(
                "clear",
                "Clear le channel",
                new CommandClear(),
                "clear"
        ));
        registry.addCommand(new Command(
                "setComptor",
                "Fixe le compteur au nombre spécifié",
                new CommandSetComptor(),
                "setComptor"
        ));
        registry.addCommand(new Command(
                "blacklist",
                "Black list un utilisateur",
                new CommandBlacklist(),
                "blacklist"
        ));
        registry.addCommand(new Command(
                "unblacklist",
                "Unblack list un utilisateur",
                new CommandUnblacklist(),
                "unblacklist"
        ));
    }

    private static final String PREFIX = Main.getConfigManager().getToml().getString("bot.prefix");

    public static void createMessage(MessageCreateEvent event) {
        if (event.getMessageContent().startsWith(PREFIX)){
            String[] args = event.getMessageContent().split(" ");
            String commandName = args[0].substring(PREFIX.length());
            args = args.length == 1 ? new String[0] : Arrays.copyOfRange(args, 1, args.length);

            String[] finalArgs = args;
            registry.getByAlias(commandName).ifPresent((command) -> {
                command.getExecutor().run(event, command, finalArgs);
            });
        }

    }

}
