package it.polimi.db2.DB2_project.GmaEJB.Entities;

public class UserLeaderboard {
    private String username;
    private Integer points;

    public UserLeaderboard(String username, Integer points) {
        this.username = username;
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public Integer getPoints() {
        return points;
    }
}
