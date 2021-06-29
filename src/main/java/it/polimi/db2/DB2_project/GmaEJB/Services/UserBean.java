package it.polimi.db2.DB2_project.GmaEJB.Services;

import it.polimi.db2.DB2_project.GmaEJB.Entities.User;
import it.polimi.db2.DB2_project.GmaEJB.Entities.UserLeaderboard;
import it.polimi.db2.DB2_project.Hasher;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class UserBean {
    @PersistenceContext
    private EntityManager em;

    public User checkCredentials(String username, String password) throws NoSuchAlgorithmException {
        List<User> user = null;

        try {
            // Query to find the user given their credentials
            user = em.createNamedQuery("User.login", User.class)
                    .setParameter(1, username)
                    .setParameter(2, Hasher.SHA2(password))
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        if(user == null || user.isEmpty())
            return null;
        return user.get(0);
    }

    public Boolean isNameTaken(String username) {
        List<User> userList = em.createNamedQuery("User.findByName", User.class)
                .setParameter(1, username)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList();

        return !(userList == null || userList.isEmpty());
    }

    public User createUser(String user, String password, String email) {
        User newUser = new User();

        newUser.setUsername(user);
        try {
            newUser.setPassword(Hasher.SHA2(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        newUser.setEmail(email);
        newUser.setPoints(0);
        newUser.setIsAdmin(false);
        newUser.setBanned(false);

        em.persist(newUser);

        return newUser;
    }

    public void banUser(User u){
        u.setBanned(true);
        em.merge(u);
    }

    public List<UserLeaderboard> retrieveLeaderboard() {
        return em.createNamedQuery("User.getLeaderboard", User.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList().stream().map(u-> new UserLeaderboard(u.getUsername(), u.getPoints())).
                collect(Collectors.toList());
    }
}
