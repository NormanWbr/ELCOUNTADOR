package be.wamberchies.commands.leaderboard;

import be.wamberchies.Main;
import be.wamberchies.leaderboard.Leaderboard;
import be.wamberchies.utils.Comptor;
import be.wamberchies.utils.commands.Command;
import be.wamberchies.utils.commands.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;

public class CommandResetAll implements CommandExecutor {

    @Override
    public void run(MessageCreateEvent event, Command command, String[] args) {
        Leaderboard leaderboard = Main.getLeaderboard();
        Comptor comptor = Main.getComptor();
        TextChannel channel = Main.getApi().getChannelById(Main.getComptorChannelId()).get().asTextChannel().orElseThrow();

        channel.sendMessage("Le compteur est à 0");
        leaderboard.resetLeaderboard();
        comptor.reset();
        System.out.println("Leaderboard resetted by " + event.getMessageAuthor().getDisplayName());
    }
}
