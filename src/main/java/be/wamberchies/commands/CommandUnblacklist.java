package be.wamberchies.commands;

import be.wamberchies.utils.commands.Command;
import be.wamberchies.utils.commands.CommandExecutor;
import be.wamberchies.utils.game.CountadorPlay;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.List;

public class CommandUnblacklist implements CommandExecutor {
    @Override
    public void run(MessageCreateEvent event, Command command, String[] args) {
        List<Long> blacklist = CountadorPlay.getBlacklist();

        blacklist.remove(event.getMessage().getMentionedUsers().get(0).getId());
        System.out.println("Unblacklisted user " + event.getMessage().getMentionedUsers().get(0).getId());

        CountadorPlay.setBlacklist(blacklist);
    }
}
