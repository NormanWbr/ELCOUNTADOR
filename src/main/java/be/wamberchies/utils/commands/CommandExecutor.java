package be.wamberchies.utils.commands;

import be.wamberchies.leaderboard.Leaderboard;
import org.javacord.api.event.message.MessageCreateEvent;

public interface CommandExecutor {

        void run(MessageCreateEvent event, Command command , String[] args, Leaderboard leaderboardGlobal);

}
