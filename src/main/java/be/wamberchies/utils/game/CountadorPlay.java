package be.wamberchies.utils.game;

import be.wamberchies.leaderboard.Leaderboard;
import be.wamberchies.leaderboard.LeaderboardDisplay;
import be.wamberchies.utils.Comptor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CountadorPlay implements Serializable {
    private static List<Long> blacklist = new ArrayList<>();

    public static void play(Comptor comptor, TextChannel channel, MessageAuthor author, Message message, String messageContent, Leaderboard leaderboard, LeaderboardDisplay leaderboardDisplay) {

        if (!author.isYourself()) {
            if ((!messageContent.matches("[0-9]+") || author.getId() == 261090059631984641L)) {
                message.delete();
            } else {
                int nbrNow = Integer.parseInt(messageContent);

                Message messageBefore = message.getMessagesBeforeAsStream().findFirst().orElse(null);

                if (messageBefore != null) {

                    MessageAuthor authorBefore = messageBefore.getAuthor();

                    if (author.getId() == authorBefore.getId()) {
                        message.delete();
                    } else if (!comptor.isBefore(nbrNow)) {

                        channel.sendMessage(author.getName() + " à réinitialisé le compteur! Il était à " + comptor.getComptor() + "!");
                        channel.sendMessage("Le compteur est à 0!");
                        leaderboard.addScore(comptor.getComptor(), author.getId());
                        leaderboard.addPointsToUser(author.getId(), -comptor.getComptor() + 1);
                        comptor.reset();

                    } else {
                        leaderboard.addPointsToUser(author.getId(), 1);
                        comptor.increment();
                    }

                    leaderboardDisplay.display(leaderboard);

                }
            }
        }
    }

    public static void delivery(MessageAuthor author, int nbrBefore) {

        String message = """      
                ───────▄▌▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
                ───▄▄██▌█ BEEP BEEP
                ▄▄▄▌▐██▌█ -%d POINTS DELIVERY
                ███████▌█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌
                ▀(⊙)▀▀▀▀▀▀▀(⊙)(⊙)▀▀▀▀▀▀▀▀▀▀(⊙)▀▀
                """;

        User user = author.asUser().get();

        user.sendMessage(String.format(message, nbrBefore));

    }

    public static void setBlacklist(List<Long> blacklist) {
        CountadorPlay.blacklist = blacklist;
    }

    public static List<Long> getBlacklist() {
        return blacklist;
    }
}