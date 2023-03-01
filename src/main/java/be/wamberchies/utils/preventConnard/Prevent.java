package be.wamberchies.utils.preventConnard;

import be.wamberchies.Main;
import be.wamberchies.leaderboard.Leaderboard;
import org.javacord.api.entity.auditlog.AuditLog;
import org.javacord.api.entity.auditlog.AuditLogActionType;
import org.javacord.api.entity.auditlog.AuditLogEntry;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.channel.TextChannelEvent;
import org.javacord.api.event.message.CertainMessageEvent;
import org.javacord.api.event.message.OptionalMessageEvent;

import java.awt.desktop.UserSessionEvent;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Prevent {

    public static void penaltyLawInfriged(OptionalMessageEvent event) {

        if (event.getChannel().getId() == Main.getComptorChannelId()) {
            long channelId = Main.getComptorChannelId();
            Server server = event.getServer().get();
            List<AuditLogEntry> auditLog = server.getAuditLog(5).join().getEntries();
            User user;

            for (AuditLogEntry auditLogEntry : auditLog) {
                if (auditLogEntry.getReason().get().equals(AuditLogActionType.MESSAGE_DELETE.toString())) {
                    user = auditLogEntry.getUser().join();
                    penaltyLawInfrigedChannel(event, user);
                }
            }
        }
    }

    public static void penaltyLawInfriged(CertainMessageEvent event) {
        if (event.getChannel().getId() == Main.getComptorChannelId()){
            User user = event.getMessageAuthor().asUser().get();
            penaltyLawInfrigedChannel(event, user);
        }
    }

    private static void penaltyLawInfrigedChannel(TextChannelEvent event, User user) {

        if (!user.isYourself()) {
            TextChannel channel = event.getChannel();
            Leaderboard leaderboard = Main.getLeaderboard();
            long comptorChannelId = Main.getComptorChannelId();

            System.out.println("Infraction effectuée par " + user.getName());
            leaderboard.setPoints(user.getId(), -2147383648);
            user.sendMessage("Vous avez été sanctionné pour infraction à la loi de la pénalité! (Vous avez perdu tous vos points!)");
            channel.sendMessage("Le compteur est à " + Main.getComptor().getComptor() + "!");
        }
    }

}
