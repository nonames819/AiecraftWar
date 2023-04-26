package edu.hitsz.database;

/**
 * @author hitsz
 */
public class Score implements Comparable<Score> {
    private int score;
    private String player;
    private String time;
    private String difficulty = "TO_BE_SET";

    public Score(int score, String player, String time, String difficulty) {
        this.score = score;
        this.player = player;
        this.time = time;
        this.difficulty = difficulty;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return score +"\t" + player + "\t" + time + "\t" + difficulty;
    }

    @Override
    public int compareTo(Score o) {
        return o.score-this.score;
    }
}
