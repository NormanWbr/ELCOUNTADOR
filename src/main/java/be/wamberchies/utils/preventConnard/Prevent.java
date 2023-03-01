package be.wamberchies.utils.preventConnard;

import be.wamberchies.Main;
import be.wamberchies.leaderboard.Leaderboard;
import be.wamberchies.utils.Comptor;
import org.javacord.api.entity.auditlog.AuditLogActionType;
import org.javacord.api.entity.auditlog.AuditLogEntry;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.channel.TextChannelEvent;
import org.javacord.api.event.message.CertainMessageEvent;
import org.javacord.api.event.message.OptionalMessageEvent;

import java.util.List;

public class Prevent {

    /**
     * This method is used to prevent connard from deleting or editing messages in the comptor channel
     *
     * @param event the event that triggered the method
     * @param <T>   the type of the event
     */
    public static <T extends TextChannelEvent> void penaltyLawInfriged(T event) {
        Comptor comptor = Comptor.loadComptor();
        long channelId = event.getChannel().getId();
        User user = null;

        if (channelId == Main.getComptorChannelId() && comptor.isPenaltyEnabled()) {
            if (event instanceof OptionalMessageEvent) {
                user = getDeleteEventUser((OptionalMessageEvent) event);
            } else if (event instanceof CertainMessageEvent) {
                user = getEditEventUser((CertainMessageEvent) event);
            }
        }

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

    /**
     * This method is used to get the last user who deleted a message
     * @param event the event that triggered the method
     * @return the last user who deleted a message
     */
    private static User getDeleteEventUser(OptionalMessageEvent event) {
        long idEvent = event.getChannel().getId();
        long comptorChannelId = Main.getComptorChannelId();
        User user = null;

        if (idEvent == comptorChannelId) {
            Server server = event.getServer().get();
            List<AuditLogEntry> auditLog = server.getAuditLog(5).join().getEntries();

            String AuditLogActionDelete = AuditLogActionType.MESSAGE_DELETE.toString();

            for (AuditLogEntry auditLogEntry : auditLog) {
                String auditLogActionMessage = auditLogEntry.getType().name();

                if (auditLogActionMessage.equals(AuditLogActionDelete)) {
                    user = auditLogEntry.getUser().join();
                    break;
                }
            }
        }

        return user;
    }

    /**
     * This method is used to get the user who edited a message in the comptor channel
     * @param event the event that triggered the method
     * @return the user who edited the message
     */
    private static User getEditEventUser(CertainMessageEvent event) {
        return event.getMessageAuthor().asUser().get();
    }
}
