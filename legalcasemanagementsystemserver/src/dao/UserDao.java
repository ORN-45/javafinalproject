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
             List<User> userList= ss.createQuery("select  ord from user ord").list();
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
    Session session = HibernateUtil.getSessionFactory().openSession();
    List<User> users = session.createQuery("FROM User u WHERE u.status = 'Active'").list();
    session.close();
    return users;
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

}
