package be.wamberchies.LeaderBoardGlobal;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class GlobalDisplay {

    DiscordApi api;

    final Long LEADERBOARDGLOBAL = 1077967028616429618L;
    TextChannel channel;

    public GlobalDisplay(DiscordApi insertApi){
        this.api = insertApi;
        channel = api
                .getTextChannelById(LEADERBOARDGLOBAL)
                .orElseThrow();
    }



    public void display(LeaderboardGlobal leaderboardGlobal) {

        EmbedBuilder global = new EmbedBuilder()
                .setAuthor("Leaderboard Global")
                .setDescription("Meilleurs scores globaux")
                .addField("Résultats:", leaderboardGlobal.toString());

        Message message = channel.getMessages(1).join().stream().findFirst().orElse(message = null);

        if (message == null) {
            channel.sendMessage(global);
        } else {
            message.edit(global);
        }
    }

}
