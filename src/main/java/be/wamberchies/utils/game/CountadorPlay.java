package be.wamberchies.utils.game;

import be.wamberchies.Main;
import be.wamberchies.leaderboard.Leaderboard;
import be.wamberchies.leaderboard.LeaderboardDisplay;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;

public class CountadorPlay {

    public static boolean play(TextChannel channel, MessageAuthor author, Message message, String messageContent, Leaderboard leaderboard, LeaderboardDisplay leaderboardDisplay) {

        boolean play = false;

        if (!author.isYourself() && !messageContent.matches("[0-9]+")) {
            message.delete();
        } else if (!author.isYourself()) {
            int nbrNow = Integer.parseInt(messageContent);

            //A MODIFIER AVEC LE SERIAL PLUS TARD
            Message messageBefore = message.getMessagesBeforeAsStream().findFirst().orElse(null);

            if (messageBefore != null) {

                MessageAuthor authorBefore = messageBefore.getAuthor();

                String messageContentBefore = messageBefore.getContent();

                int nbrBefore = Integer.parseInt(messageContentBefore);

                if (author.getId() == authorBefore.getId() && false) {
                    message.delete();
                } else if (nbrNow != nbrBefore + 1) {
                    message.delete();
                    channel.deleteMessages(message.getMessagesBefore(nbrBefore + 1).join());
                    channel.sendMessage(author.getName() + " à réinitialisé le compteur! Il était à " + nbrBefore + "!");
                    channel.sendMessage("1");

                    leaderboard.addScore(nbrBefore, author.getId());
                    leaderboard.addPointsToUser(author.getId(), -nbrBefore + 1);

                } else {
                    leaderboard.addPointsToUser(author.getId(), 1);
                    play = true;
                }

                leaderboardDisplay.display(leaderboard);

            } else if (nbrNow != 1) {
                message.delete();
            }
        }

    return play;

    }

}