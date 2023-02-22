package be.wamberchies.LeaderBoardGlobal;

import org.javacord.api.DiscordApi;

import java.io.Serializable;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class LeaderboardGlobal implements Serializable {

    private final int MAX_SCORE_STORED = 5;

    DiscordApi api;

    // Score & ID de l'utilisateur
    // Enregistre le dernier score et l'utilisateur qui l'a atteint
    SortedMap<Integer, Long> leaderboard = new TreeMap<>();

    public LeaderboardGlobal(DiscordApi api) {
        this.api = api;
    }

    public void addScore(int score, long userID) {

        if (score > leaderboard.firstKey()){
            leaderboard.put(score, userID);

            if (leaderboard.size() > MAX_SCORE_STORED) {
                leaderboard.remove(leaderboard.firstKey());
            }
        }
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
        
        return sb.toString();
    }
}
