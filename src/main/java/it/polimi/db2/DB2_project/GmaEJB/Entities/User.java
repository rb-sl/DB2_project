package it.polimi.db2.DB2_project.GmaEJB.Entities;

import javax.persistence.*;
import java.util.List;

@Table(name = "user")
@NamedQuery(name = "User.login",
        query = "SELECT u FROM User u WHERE u.username=?1 AND u.password=?2")
@NamedQuery(name = "User.findByName",
        query = "SELECT u FROM User u WHERE u.username=?1")
@NamedQuery(name = "User.getLeaderboard",
        query = "SELECT u FROM User u WHERE NOT u.isAdmin ORDER BY u.points DESC")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "password", nullable = false)
    private String password;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "isAdmin", nullable = false)
    private Boolean isAdmin = false;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "isBanned", nullable = false)
    private Boolean isBanned;

    public Boolean getBanned() {
        return isBanned;
    }

    public void setBanned(Boolean banned) {
        isBanned = banned;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}