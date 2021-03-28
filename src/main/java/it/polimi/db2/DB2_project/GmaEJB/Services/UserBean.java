package it.polimi.db2.DB2_project.GmaEJB.Services;

import it.polimi.db2.DB2_project.GmaEJB.Entities.User;

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

        // Hashing of the password with SHA2 224
        MessageDigest sha224 = MessageDigest.getInstance("SHA-224");
        byte[] passBytes = password.getBytes();
        byte[] passHash = sha224.digest(passBytes);

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, passHash);

        // Convert message digest into hex value
        String hashtext = no.toString(16);

        // Add preceding 0s to make it 32 bit
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }

        try {
            // Query to find the user given their credentials
            user = em.createNamedQuery("User.login", User.class)
                    .setParameter(1, username)
                    .setParameter(2, hashtext)
                    .getResultList();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        if(user == null || user.isEmpty())
            return null;
        return user.get(0);
    }
}
