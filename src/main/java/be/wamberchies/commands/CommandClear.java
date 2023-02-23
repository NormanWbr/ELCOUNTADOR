package be.wamberchies.commands;

import be.wamberchies.leaderboard.Leaderboard;
import be.wamberchies.utils.commands.Command;
import be.wamberchies.utils.commands.CommandExecutor;
import org.javacord.api.event.message.MessageCreateEvent;

public class CommandClear implements CommandExecutor {
    @Override
    public void run(MessageCreateEvent event, Command command, String[] args, Leaderboard leaderboardGlobal) {

        if (args.length == 1) {
            try {
                Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                event.getChannel().sendMessage("Nombre invalide");
                return;
            }
            event.getChannel().getMessages(1 + Integer.parseInt(args[0])).thenAccept(messages -> event.getChannel().deleteMessages(messages));
        } else {
            event.getChannel().getMessages(100).thenAccept(messages -> event.getChannel().deleteMessages(messages));
        }

    }

}
