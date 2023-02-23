package be.wamberchies.commands;

import be.wamberchies.leaderboard.Leaderboard;
import be.wamberchies.utils.commands.Command;
import be.wamberchies.utils.commands.CommandExecutor;
import org.javacord.api.event.message.MessageCreateEvent;

public class CommandResetAll implements CommandExecutor {

    @Override
    public void run(MessageCreateEvent event, Command command, String[] args, Leaderboard leaderboardGlobal ) {
        leaderboardGlobal.resetLeaderboard();
    }
}
