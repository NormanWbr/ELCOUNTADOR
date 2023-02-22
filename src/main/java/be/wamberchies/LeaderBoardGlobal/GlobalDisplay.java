package be.wamberchies.LeaderBoardGlobal;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class GlobalDisplay {

    DiscordApi api;

    final Long LEADERBOARDCHANNEL = 1078027007692116008L;
    final Long LEADERBOARDMESSAGE = 1078027324118814820L;
    TextChannel channel;

    public GlobalDisplay(DiscordApi insertApi){
        this.api = insertApi;
        channel = api
                .getTextChannelById(LEADERBOARDCHANNEL)
                .orElseThrow();
    }



    public void display(LeaderboardGlobal leaderboardGlobal) {

        EmbedBuilder global = new EmbedBuilder()
                .setAuthor("Leaderboard Global")
                .setDescription("Meilleurs scores globaux")
                .addField("Résultats:", leaderboardGlobal.toString());

        Message message = channel.getMessageById(LEADERBOARDMESSAGE).join();

        if (message == null) {
            channel.sendMessage(global);
        } else {
            message.edit(global);
        }

    }

}
