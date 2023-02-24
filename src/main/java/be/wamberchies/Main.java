package be.wamberchies;

import be.wamberchies.leaderboard.Leaderboard;
import be.wamberchies.leaderboard.LeaderboardDisplay;
import be.wamberchies.utils.commands.MessageManager;
import be.wamberchies.utils.config.ConfigManager;
import be.wamberchies.utils.game.CountadorPlay;
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

        Leaderboard leaderboard = new Leaderboard(api);

        LeaderboardDisplay leaderboardDisplay = new LeaderboardDisplay(api);

        System.out.println("Le bot est en ligne!");

        api.addMessageCreateListener(
                event -> {

                    String messageContent = event.getMessageContent();

                    TextChannel channel = event.getChannel();

                    MessageAuthor author = event.getMessageAuthor();

                    Message message = event.getMessage();

                    if (channel.getId() == COUNTADORCHANNELID) {

                        CountadorPlay.playNormal(channel, author, message, messageContent, leaderboard, leaderboardDisplay);

                    }

                    if (author.isServerAdmin() && messageContent.startsWith(configManager.getToml().getString("bot.prefix"))) {
                        MessageManager.createMessage(event, leaderboard);
                        leaderboardDisplay.display(leaderboard);
                    }

                });
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
