package dao;

import model.User;
import org.hibernate.Session;
import org.hibernate.Query;

public class LoginDao {

    /**
     * Authenticate a user by username and password
     * 
     * @param username The username entered
     * @param password The password entered
     * @return The User object if authentication is successful; otherwise, null
     */
    public User authenticateUser(String username, String password) {
        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM User WHERE username = :username AND password = :password";
            Query query = session.createQuery("FROM User WHERE username = :username AND password = :password");
       query.setParameter("username", username);
       query.setParameter("password", password);
          User foundUser = (User) query.uniqueResult();

        return foundUser;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return user;
        
        
    }
    public String createUser(User user, String role) {
        user.setRole(role); // Set role as intended
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user); // Save the user entity
            transaction.commit();
            return "User created successfully";
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return "Error creating user: " + e.getMessage();
        }
    }
}
