package be.wamberchies.leaderboard;

import be.wamberchies.utils.serializateur.Serializateur;
import org.javacord.api.DiscordApi;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class Leaderboard {


    DiscordApi api;

    // Score & ID de l'utilisateur
    // Enregistre le dernier score et l'utilisateur qui l'a atteint
    LeaderboardData leaderboard;

    public Leaderboard(DiscordApi api) {
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
        if (leaderboard.size() < MAX_SCORE_STORED || score > leaderboard.getLowestChain().getScore()){
            leaderboard.put(score, userID);

            if (leaderboard.size() > MAX_SCORE_STORED) {
                leaderboard.remove(leaderboard.getLowestChain());
            }

            leaderboard.save();
            return true;
        }

        return false;
    }

    /**
     * Add points to a user
     * A negative value can be used to remove points
     * @param userId ID of the user to add points to
     * @param pointsToAdd points to add
     */
    public void addPointsToUser(long userId, int pointsToAdd) {
        leaderboard.addPointsToUser(userId, pointsToAdd);
    }

    public void resetLeaderboard() {
        leaderboard.resetLeaderboard();
    }

    public void setPoints(Long userId, int setScore) {
        leaderboard.setPoints(userId, setScore);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        List<ChainLeaderboard> sortedChainLeaderboard = leaderboard.getSortedChainLeaderboard();
        for (ChainLeaderboard chain : sortedChainLeaderboard) {
            sb.append("%d:    chaîne de %d points, cassée par %s\n".formatted(
                    sortedChainLeaderboard.indexOf(chain) + 1,
                    chain.getScore(),
                    api.getUserById(chain.getUserId()).join().getName())
            );
        }

        sb.append("\n");

        Map<Long, Integer> userPointsSorted = leaderboard.getUserPointsSorted();
        int counter = 1;
        for (Map.Entry<Long, Integer> userPoint : userPointsSorted.entrySet()) {
            sb.append("%d:     %s a un score de %d points\n".formatted(
                    counter,
                    api.getUserById(userPoint.getKey()).join().getName(),
                    userPoint.getValue()
            ));
            counter++;
        }

        return sb.toString();
    }
}
