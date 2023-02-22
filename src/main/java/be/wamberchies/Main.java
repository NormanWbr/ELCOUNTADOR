package be.wamberchies;

import be.wamberchies.LeaderBoardGlobal.GlobalDisplay;
import be.wamberchies.LeaderBoardGlobal.LeaderboardGlobal;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;

public class Main {

    public static void main(String[] args) {

        DiscordApi api = new DiscordApiBuilder()
                .setToken("")
                .setAllIntents()
                .login()
                .join();

        api.updateActivity(ActivityType.WATCHING,"le compteur \uD83D\uDC40");

        final Long COUNTADORCHANNELID = 1077880179097092136L;
        final long COUNTADORADMINCHANNELID = 1078085640366866532L;

        LeaderboardGlobal leaderboardGlobal = new LeaderboardGlobal(api);

        GlobalDisplay globalDisplay = new GlobalDisplay(api);

        System.out.println("Le bot est en ligne!");

        api.addMessageCreateListener(
                event -> {

                    String messageContent = event.getMessageContent();

                    TextChannel channel = event.getChannel();

                    MessageAuthor author = event.getMessageAuthor();

                    Message message = event.getMessage();

                    if (channel.getId() == COUNTADORCHANNELID) {

                        if (messageContent.equals("!clear") && author.isServerAdmin()) {
                            channel.deleteMessages(channel.getMessages(100).join());
                            channel.sendMessage("1");
                        }

                        if (!author.isYourself() && !messageContent.matches("[0-9]+")) {
                            message.delete();
                        } else if (!author.isYourself()) {
                            int nbrNow = Integer.parseInt(messageContent);

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

                                    leaderboardGlobal.addScore(nbrBefore, author.getId());
                                    leaderboardGlobal.addPointsToUser(author.getId(), -nbrBefore + 1);

                                } else {
                                    leaderboardGlobal.addPointsToUser(author.getId(), 1);
                                }

                                globalDisplay.display(leaderboardGlobal);

                            } else if (nbrNow != 1) {
                                message.delete();
                            }
                        }

                    }

                    if (channel.getId() == COUNTADORADMINCHANNELID && author.isServerAdmin()) {

                    }

                });
    }
}
