package be.wamberchies.leaderboard;

import be.wamberchies.Main;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.time.LocalDateTime;

public class LeaderboardDisplay {


    DiscordApi api;

    TextChannel channel;

    final Long LEADERBOARDCHANNEL = Main.getConfigManager().getToml().getLong("bot.leaderboardChannel");


    public LeaderboardDisplay(DiscordApi insertApi){
        this.api = insertApi;
        channel = api
                .getTextChannelById(LEADERBOARDCHANNEL)
                .orElseThrow();
    }



    public void display(Leaderboard leaderboardGlobal) {

        EmbedBuilder global = new EmbedBuilder()
                .setAuthor("Leaderboard Global")
                .setDescription("Meilleurs scores globaux")
                .addField("Résultats:", leaderboardGlobal.toString())
                .setColor(Color.GREEN);

        Message message = channel.getMessages(1).join().stream().findFirst().orElse(message = null);

        if (message == null) {
            channel.sendMessage(global);
        } else {
            message.edit(global);
        }

    }

}
