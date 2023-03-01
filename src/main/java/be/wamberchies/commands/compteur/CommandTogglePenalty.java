package be.wamberchies.commands.compteur;

import be.wamberchies.Main;
import be.wamberchies.utils.Comptor;
import be.wamberchies.utils.commands.Command;
import be.wamberchies.utils.commands.CommandExecutor;
import org.javacord.api.event.message.MessageCreateEvent;

public class CommandTogglePenalty implements CommandExecutor {

    @Override
    public void run(MessageCreateEvent event, Command command, String[] args) {
        Comptor comptor = Main.getComptor();

        comptor.togglePenalty();

    }
}
