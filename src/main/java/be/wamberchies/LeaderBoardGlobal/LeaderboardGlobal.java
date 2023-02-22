package be.wamberchies.LeaderBoardGlobal;

import org.javacord.api.DiscordApi;

import java.io.FileNotFoundException;
import java.util.Map;

public class LeaderboardGlobal  {


    DiscordApi api;

    // Score & ID de l'utilisateur
    // Enregistre le dernier score et l'utilisateur qui l'a atteint
    LeaderboardData leaderboard;

    public LeaderboardGlobal(DiscordApi api) {
        this.api = api;

        try {
            leaderboard = Serializateur.deserialize(LeaderboardData.getSaveFilepath());
        } catch (FileNotFoundException e) {
            leaderboard = new LeaderboardData();
        }
    }

    /**
     * Add a score to the leaderboard
     * If the score is lower than the lowest score, it will not be added
     * If the leaderboard is full, the lowest score will be removed
     * @param score score to add
     * @param userID ID of the user who got the score
     * @return true if the score was added, false otherwise
     */
    public boolean addScore(int score, long userID) {

        int MAX_SCORE_STORED = 5;
        if (leaderboard.size() < MAX_SCORE_STORED || score > leaderboard.firstKey()){
            leaderboard.put(score, userID);

            if (leaderboard.size() > MAX_SCORE_STORED) {
                leaderboard.remove(leaderboard.firstKey());
            }

            Serializateur.serialize(leaderboard, LeaderboardData.getSaveFilepath());
            return true;
        }

        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Integer, Long> entry: leaderboard.entrySet()) {
            String username = api.getUserById(entry.getValue()).join().getName();
            sb.append(
                    "Meilleur chaîne : %d, cassée par %s\n".formatted(entry.getKey(), username)
            );
        }

        sb.append("\n");

        for (Map.Entry<Long, Integer> entry: leaderboard.userPoints.entrySet()) {
            String username = api.getUserById(entry.getKey()).join().getName();
            sb.append(
                    "%s a atteint un score de %d\n".formatted(username, entry.getValue())
            );
        }
        
        return sb.toString();
    }

    /**
     * Add points to a user
     * A negative value can be used to remove points
     * @param userId ID of the user to add points to
     * @param pointsToAdd points to add
     */
    public void addPointsToUser(long userId, int pointsToAdd) {
        leaderboard.addPointsToUser(userId, pointsToAdd);
        Serializateur.serialize(leaderboard, LeaderboardData.getSaveFilepath());
    }
}
