package be.wamberchies.leaderboard;

import be.wamberchies.utils.serializateur.Serializateur;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class LeaderboardData implements Serializable {

    @Serial
    private static final long SerialversionUID = 1L;
    private static final String SAVE_FILEPATH = "Sauvegarde/LeaderboardData.ser";

    List<ChainLeaderboard> chainLeaderboardList = new ArrayList<>();
    Map<Long, Integer> userPointsMap = new HashMap<>();

    public static String getSaveFilepath() {
        return SAVE_FILEPATH;
    }

    /**
     * Add a score to the leaderboard
     * @param score score to add
     * @param userID ID of the user who got the score
     */
    public void put(int score, long userID) {
        chainLeaderboardList.add(
                new ChainLeaderboard(score, userID)
        );

        save();
    }

    /**
     * Get the size of the leaderboard
     * @return size of the leaderboard
     */
    public int size() {
        return chainLeaderboardList.size();
    }

    public List<ChainLeaderboard> getLeaderboard() {
        return chainLeaderboardList;
    }

    public void addPointsToUser(long userId, int pointsToAdd) {
        if (userPointsMap.containsKey(userId)) {
            userPointsMap.put(userId, userPointsMap.get(userId) + pointsToAdd);
        } else {
            userPointsMap.put(userId, pointsToAdd);
        }

        save();
    }

    public void resetLeaderboard() {
        userPointsMap = new TreeMap<>();
        chainLeaderboardList = new ArrayList<>();
        save();
    }

    public void setPoints(Long userId, int setScore) {
        userPointsMap.put(userId, setScore);
        save();
    }

    public void remove(ChainLeaderboard lowestChain) {
        chainLeaderboardList.remove(lowestChain);
        save();
    }

    public ChainLeaderboard getLowestChain() {
        ChainLeaderboard lowestChain = chainLeaderboardList.get(0);

        for (ChainLeaderboard chainLeaderboard : chainLeaderboardList) {
            if (chainLeaderboard.getScore() < lowestChain.getScore()) {
                lowestChain = chainLeaderboard;
            }
        }

        return lowestChain;
    }

    public void save() {
        Serializateur.serialize(this, SAVE_FILEPATH);
    }

    public List<ChainLeaderboard> getSortedChainLeaderboard() {
        List<ChainLeaderboard> sortedChainLeaderboard = new ArrayList<>(chainLeaderboardList);
        sortedChainLeaderboard.sort(Comparator.comparingInt(ChainLeaderboard::getScore).reversed());

        return sortedChainLeaderboard;
    }

    public Map<Long, Integer> getUserPointsSorted() {
        Map<Long, Integer> sortedUserPointsMap = new LinkedHashMap<>();

        userPointsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedUserPointsMap.put(x.getKey(), x.getValue()));

        return sortedUserPointsMap;
    }
}
