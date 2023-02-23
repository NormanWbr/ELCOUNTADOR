package be.wamberchies;

import be.wamberchies.leaderboard.GlobalDisplay;
import be.wamberchies.leaderboard.Leaderboard;
import be.wamberchies.utils.ConfigManager;
import be.wamberchies.utils.commands.MessageManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;

import java.io.File;

public class Main {

    private static DiscordApi api;
    private static ConfigManager configManager;

    public static void main(String[] args) {

        configManager = new ConfigManager(new File(System.getProperty("user.dir"), "config.toml"));

        api = new DiscordApiBuilder()
                .setToken(configManager.getToml().getString("bot.token"))
                .setAllIntents()
                .login()
                .join();

        api.updateActivity(ActivityType.WATCHING, "le compteur \uD83D\uDC40");

        final Long COUNTADORCHANNELID = configManager.getToml().getLong("bot.countadorChannelId");
        final Long COUNTADORADMINCHANNELID = configManager.getToml().getLong("bot.countadorAdminChannelId");

        Leaderboard leaderboardGlobal = new Leaderboard(api);

        GlobalDisplay globalDisplay = new GlobalDisplay(api);

        System.out.println("Le bot est en ligne!");

        api.addMessageCreateListener(
                event -> {

                    char prefix = '!'; // J'ai déjà commencé à écrire avec la variable préfix pour avoir plus de facilité plus tard à faire la transition vers un bot générique et configurable

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

                    if (channel.getId() == COUNTADORADMINCHANNELID) {
                        MessageManager.createMessage(event, leaderboardGlobal);
                        globalDisplay.display(leaderboardGlobal);
                    }

                });
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
