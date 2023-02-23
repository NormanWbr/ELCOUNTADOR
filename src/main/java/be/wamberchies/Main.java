package be.wamberchies;

import be.wamberchies.leaderboard.LeaderboardDisplay;
import be.wamberchies.utils.config.ConfigManager;
import be.wamberchies.leaderboard.Leaderboard;
import be.wamberchies.utils.commands.MessageManager;
import be.wamberchies.utils.game.CountadorPlay;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

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

        Leaderboard leaderboard = new Leaderboard(api);

        LeaderboardDisplay leaderboardDisplay = new LeaderboardDisplay(api);

        System.out.println("Le bot est en ligne!");

        api.addMessageCreateListener(
                event -> {

                    LocalDateTime now = LocalDateTime.now();
                    DayOfWeek dayOfWeek = now.getDayOfWeek();
                    int hour = now.getHour();

                    if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY && hour >= 9 && hour < 17) {
                        System.out.println("L'heure actuelle est entre 9h et 17h du lundi au vendredi.");
                    }

                    String messageContent = event.getMessageContent();

                    TextChannel channel = event.getChannel();

                    MessageAuthor author = event.getMessageAuthor();

                    Message message = event.getMessage();

                    if (channel.getId() == COUNTADORCHANNELID) {

                        if (CountadorPlay.play(channel, author, message, messageContent, leaderboard, leaderboardDisplay)){
                            System.out.println("Le chiffre est bon!");
                        }

                    }

                    if (author.isServerAdmin() && messageContent.startsWith(configManager.getToml().getString("bot.prefix"))) {
                        MessageManager.createMessage(event, leaderboard);
                        message.delete();
                        leaderboardDisplay.display(leaderboard);
                    }

                });
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
