package be.wamberchies.commands.leaderboard;

import be.wamberchies.Main;
import be.wamberchies.leaderboard.Leaderboard;
import be.wamberchies.utils.commands.Command;
import be.wamberchies.utils.commands.CommandExecutor;
import org.javacord.api.event.message.MessageCreateEvent;

public class CommandResetAll implements CommandExecutor {

    @Override
    public void run(MessageCreateEvent event, Command command, String[] args) {
        Leaderboard leaderboard = Main.getLeaderboard();

        leaderboard.resetLeaderboard();
        System.out.println("Leaderboard resetted by " + event.getMessageAuthor().getDisplayName());
    }
}
