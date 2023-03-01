package be.wamberchies.commands;

import be.wamberchies.utils.commands.Command;
import be.wamberchies.utils.commands.CommandExecutor;
import be.wamberchies.utils.game.CountadorPlay;
import be.wamberchies.utils.serializateur.Serializateur;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.List;

public class CommandBlacklist implements CommandExecutor {
    @Override
    public void run(MessageCreateEvent event, Command command, String[] args) {

        List<Long> blacklist = CountadorPlay.getBlacklist();

        blacklist.add(event.getMessage().getMentionedUsers().get(0).getId());
        System.out.println("Blacklisted user " + event.getMessage().getMentionedUsers().get(0).getId());

        CountadorPlay.setBlacklist(blacklist);

        Serializateur.serialize(blacklist, "Sauvegarde/Blacklist.ser");
    }
}
