package be.wamberchies.commands;

import be.wamberchies.leaderboard.Leaderboard;
import be.wamberchies.utils.commands.Command;
import be.wamberchies.utils.commands.CommandExecutor;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.List;

public class CommandSetPoints implements CommandExecutor {
    @Override
    public void run(MessageCreateEvent event, Command command, String[] args, Leaderboard leaderboardGlobal) {
        List<User> users = event.getMessage().getMentionedUsers();

        if (users.size() > 0) {
            Long id = users.get(0).getId();
            try {
                leaderboardGlobal.setPoints(id, Integer.parseInt(args[args.length - 1]));
            } catch (NumberFormatException e) {
                event.getChannel().sendMessage("Format de commande non valide");
            }
        }
    }
}
