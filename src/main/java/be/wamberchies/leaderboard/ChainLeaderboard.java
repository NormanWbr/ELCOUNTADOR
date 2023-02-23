package be.wamberchies.leaderboard;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ChainLeaderboard implements Serializable {
    private int score;
    private long userId;

    public ChainLeaderboard(int score, long userId) {
        this.score = score;
        this.userId = userId;
    }
}
