package be.wamberchies.utils.game;

import be.wamberchies.leaderboard.Leaderboard;
import be.wamberchies.leaderboard.LeaderboardDisplay;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;

public class CountadorPlay {

    public static void playNormal(TextChannel channel, MessageAuthor author, Message message, String messageContent, Leaderboard leaderboard, LeaderboardDisplay leaderboardDisplay) {

        if ((!author.isYourself() && !messageContent.matches("[0-9]+") || author.getId() == 261090059631984641L)) {
            message.delete();
        } else if (!author.isYourself()) {
            int nbrNow = Integer.parseInt(messageContent);

            //A MODIFIER AVEC LE SERIAL PLUS TARD
            Message messageBefore = message.getMessagesBeforeAsStream().findFirst().orElse(null);

            if (messageBefore != null) {

                MessageAuthor authorBefore = messageBefore.getAuthor();

                String messageContentBefore = messageBefore.getContent();

                int nbrBefore = Integer.parseInt(messageContentBefore);

                if (author.getId() == authorBefore.getId()) {
                    message.delete();
                } else if (nbrNow != nbrBefore + 1) {

                    channel.sendMessage(author.getName() + " à réinitialisé le compteur! Il était à " + nbrBefore + "!");
                    channel.sendMessage("1");
                    leaderboard.addScore(nbrBefore, author.getId());
                    leaderboard.addPointsToUser(author.getId(), -nbrBefore + 1);

                } else {
                    leaderboard.addPointsToUser(author.getId(), 1);
                }

                leaderboardDisplay.display(leaderboard);

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

}