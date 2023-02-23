package be.wamberchies.leaderboard;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class LeaderboardData implements Serializable {

    @Serial
    private static final long SerialversionUID = 1L;
    private static final String SAVE_FILEPATH = "Sauvegarde/LeaderboardData.ser";

    SortedMap<Integer, Long> leaderboard = new TreeMap<>();
    SortedMap<Long, Integer> userPoints = new TreeMap<>();

    LeaderboardData() {
    }

    public static String getSaveFilepath() {
        return SAVE_FILEPATH;
    }

    /**
     * Add a score to the leaderboard
     * @param score score to add
     * @param userID ID of the user who got the score
     */
    public void put(int score, long userID) {
        leaderboard.put(score, userID);

        save();
    }

    /**
     * Remove a score from the leaderboard
     * @param score score to remove
     */
    public void remove(int score) {
        leaderboard.remove(score);
        save();
    }

    /**
     * Get the size of the leaderboard
     * @return size of the leaderboard
     */
    public int size() {
        return leaderboard.size();
    }

    public int firstKey() {
        return leaderboard.firstKey();
    }

    public SortedMap<Integer, Long> getLeaderboard() {
        return leaderboard;
    }

    public Iterable<? extends Map.Entry<Integer, Long>> entrySet() {
        return leaderboard.entrySet();
    }

    public void addPointsToUser(long userId, int pointsToAdd) {
        if (userPoints.containsKey(userId)) {
            userPoints.put(userId, userPoints.get(userId) + pointsToAdd);
        } else {
            userPoints.put(userId, pointsToAdd);
        }

        save();
    }

    public void resetLeaderboard() {
        userPoints = new TreeMap<>();
        leaderboard = new TreeMap<>();
        save();
    }

    public void setPoints(Long userId, int setScore) {
        userPoints.put(userId, setScore);
        save();
    }

    public void save() {
        Serializateur.serialize(this, SAVE_FILEPATH);
    }
}
