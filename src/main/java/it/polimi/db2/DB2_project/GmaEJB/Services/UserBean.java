package it.polimi.db2.DB2_project.GmaEJB.Services;

import it.polimi.db2.DB2_project.GmaEJB.Entities.User;
import it.polimi.db2.DB2_project.Hasher;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
                    .getResultList();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        if(user == null || user.isEmpty())
            return null;
        return user.get(0);
    }

    public Boolean isNameTaken(String username) {
        List<User> userList = em.createNamedQuery("User.findByName", User.class).setParameter(1, username).getResultList();

        return !(userList == null || userList.isEmpty());
    }

    public void createUser(String user, String password, String email) {
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

        em.persist(newUser);
    }
}
