package be.wamberchies.commands.compteur;

import be.wamberchies.Main;
import be.wamberchies.utils.Comptor;
import be.wamberchies.utils.commands.Command;
import be.wamberchies.utils.commands.CommandExecutor;
import org.javacord.api.event.message.MessageCreateEvent;

public class CommandSetComptor implements CommandExecutor {

    @Override
    public void run(MessageCreateEvent event, Command command, String[] args) {
        Comptor comptor = Main.getComptor();

        int numberToSet = Integer.parseInt(args[0]);
        comptor.setComptor(numberToSet);

        event.getChannel().sendMessage("Le compteur est à " + numberToSet + "!");
    }
}
