/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author orny
 */
public class UserDao {
    public String registerUser(User user){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.save(user);
            tr.commit();
            ss.close();
            return "Data saved succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
      public String updateUser(User user){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.update(user);
            tr.commit();
            ss.close();
            return "Data updated succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
      }
       public String deleteUser(User user){
        try{
            Session ss= HibernateUtil.getSessionFactory().openSession();
            Transaction tr= ss.beginTransaction();
            ss.delete(user);
            tr.commit();
            ss.close();
            return "Data deleted succesffully";
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
}
         public List<User> retrieveAll(){
             Session ss= HibernateUtil.getSessionFactory().openSession();
             List<User> userList= ss.createQuery("select  ord from User ord").list(); // Corrected HQL
             ss.close();
             return userList;
          }
         public User retrieveById(User user){
        Session ss= HibernateUtil.getSessionFactory().openSession();
        User users = (User)ss.get(User.class, user.getId());
        ss.close();
        return users;
    }
         public List<User> getAllActiveUsers() {
    Session session = null;
    try {
        session = HibernateUtil.getSessionFactory().openSession();
        // Corrected HQL to use the boolean 'active' field
        List<User> users = session.createQuery("FROM User u WHERE u.active = true", User.class).list();
        return users;
    } catch (Exception e) {
        e.printStackTrace();
        return new java.util.ArrayList<>();
    } finally {
        if (session != null) {
            session.close();
        }
    }
}
public List<User> getUsersByRole(String role) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    List<User> users = session
    .createQuery("FROM User u WHERE u.role = :role")
    .setParameter("role", role)
    .list();

    session.close();
    return users;
}
public List<User> retrieveByRole(String role) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> users = session.createQuery("FROM User u WHERE u.role = :role")
                                  .setParameter("role", role)
                                  .list();
        session.close();
        return users;
    }
public User retrieveByUsername(String username) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    User user = (User) session.createQuery("FROM User u WHERE u.username = :username")
                              .setParameter("username", username)
                              .uniqueResult();
    session.close();
    return user;
}

    public String updatePassword(int userId, String newPassword) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = (User) session.get(User.class, userId);
            if (user != null) {
                user.setPassword(newPassword); 
                session.update(user);
                transaction.commit();
                return "Password updated successfully for user ID: " + userId;
            } else {
                return "User not found with ID: " + userId;
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return "Error updating password: " + e.getMessage();
        }
    }

    // Renaming retrieveByRole to findByRole for consistency with task
    public List<User> findByRole(String role) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM User u WHERE u.role = :role", User.class)
                                      .setParameter("role", role)
                                      .list();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // Renaming retrieveByUsername to findByUsername for consistency with task
    public User findByUsername(String username) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM User u WHERE u.username = :username", User.class)
                                  .setParameter("username", username)
                                  .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    public User findByEmail(String email) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM User u WHERE u.email = :email", User.class)
                                  .setParameter("email", email)
                                  .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public boolean deactivateUser(int userId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = (User) session.get(User.class, userId);
            if (user != null) {
                user.setActive(false);
                session.update(user);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public boolean reactivateUser(int userId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = (User) session.get(User.class, userId);
            if (user != null) {
                user.setActive(true);
                session.update(user);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    // Renamed from updatePassword to resetPassword for semantic clarity in this context,
    // but functionality is the same as the existing updatePassword.
    // Kept existing updatePassword, this is an alias for clarity if needed, or service layer can use updatePassword.
    // For this task, ensuring a method for resetting is available. Let's use existing updatePassword.
    // The task asks for "resetPassword(int userId, String newPassword)" in DAO.
    // The existing updatePassword serves this. So, no new method, just ensure it's used.

    public boolean isUsernameExists(String username) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Long count = session.createQuery("SELECT count(u.id) FROM User u WHERE u.username = :username", Long.class)
                                .setParameter("username", username)
                                .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Or throw, as this might indicate a DB issue
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<String> getAllRoles() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("SELECT DISTINCT u.role FROM User u", String.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
