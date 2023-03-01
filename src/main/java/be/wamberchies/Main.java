package be.wamberchies;

import be.wamberchies.leaderboard.Leaderboard;
import be.wamberchies.leaderboard.LeaderboardDisplay;
import be.wamberchies.utils.Comptor;
import be.wamberchies.utils.commands.MessageManager;
import be.wamberchies.utils.config.ConfigManager;
import be.wamberchies.utils.game.CountadorPlay;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.listener.message.MessageCreateListener;

import java.io.File;
import java.util.function.Consumer;

public class Main {

    private static DiscordApi api;
    private static ConfigManager configManager;
    private static Comptor comptor;
    private static Leaderboard leaderboard;

    public static void main(String[] args) {

        configManager = new ConfigManager(new File(System.getProperty("user.dir"), "config.toml"));

        api = new DiscordApiBuilder()
                .setToken(configManager.getToml().getString("bot.token"))
                .setAllIntents()
                .login()
                .join();

        api.updateActivity(ActivityType.WATCHING, "le compteur \uD83D\uDC40");

        final Long COUNTADORCHANNELID = configManager.getToml().getLong("bot.countadorChannelId");

        leaderboard = new Leaderboard(api);
        comptor = Comptor.loadComptor();
        LeaderboardDisplay leaderboardDisplay = new LeaderboardDisplay(api);

        System.out.println("Le bot est en ligne!");

        System.out.println("Le compteur est à " + comptor.getComptor() + "!");

        TextChannel channelCountador = api.getTextChannelById(COUNTADORCHANNELID).get();
        channelCountador.sendMessage("REBOOT: Le compteur est à : " + comptor.getComptor());

        api.addMessageCreateListener(
                event -> {

                    String messageContent = event.getMessageContent();

                    TextChannel channel = event.getChannel();

                    MessageAuthor author = event.getMessageAuthor();

                    Message message = event.getMessage();

                    if (author.isServerAdmin() && messageContent.startsWith(configManager.getToml().getString("bot.prefix"))) {
                        MessageManager.createMessage(event);
                        leaderboardDisplay.display(leaderboard);
                    }

                    if (channel.getId() == COUNTADORCHANNELID && !author.isYourself()) {

                        CountadorPlay.play(comptor, channel, author, message, messageContent, leaderboard, leaderboardDisplay);
                    }

                });

//        api.addMessageEditListener();
//        api.addMessageDeleteListener();

    }

    private final Consumer<MessageCreateListener> checkUser = event -> {

    };

    public static DiscordApi getApi() {
        return api;
    }
    public static ConfigManager getConfigManager() {
        return configManager;
    }
    public static Comptor getComptor() {
        return comptor;
    }
    public static Leaderboard getLeaderboard() {
        return leaderboard;
    }
}
