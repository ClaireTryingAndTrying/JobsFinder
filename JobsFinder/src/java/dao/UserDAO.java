/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import hibernate.HibernateUtils;
import java.util.List;
import model.Constant;
import model.User;
import model.UserInfo;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author yueningli
 */
public class UserDAO {

    Session session = null;

    public UserDAO() {
        this.session = HibernateUtils.getSessionFactory().openSession();
    }

    public User login(String name, String password) {
        User userinfo;
        try {
            Query q = session.createQuery("from User as userinfo where userinfo.name = '" + name + "' and userinfo.password = '" + password + "'");
            userinfo = new User();
            userinfo = (User) q.uniqueResult();
            return userinfo;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * check duplicate user
     *
     * @param name
     * @return
     */
    public boolean checkUsernameDuplicate(String name) {
        try {
            Query q = session.createQuery("from User as userinfo where userinfo.name = '" + name + "'");
            if (q.list().size() > 0) {
                return true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * update user
     *
     * @param method
     * @param userinfo
     * @return
     */
    public int insertOrUpdateUser(String method, User userinfo) {
        org.hibernate.Transaction tx = null;
        userinfo.setId(String.valueOf(userinfo.hashCode()));
        try {
            tx = session.beginTransaction();
            if (checkUsernameDuplicate(userinfo.getName()) && method.equals("register")) {
                return Constant.USERNAME_DUPLICATE;
            }
            if (method.equals("update")) {
                String hql = "update User set name=:name, email=:email, password=:password where id=:id ";
                Query q = session.createQuery(hql);
                q.setParameter("name", userinfo.getName());
                q.setParameter("id", userinfo.getId());
                q.setParameter("email", userinfo.getEmail());
                q.setParameter("password", userinfo.getPassword());
                int result = q.executeUpdate();
                tx.commit();
                session.close();
                return result;
            }
            session.saveOrUpdate(userinfo);
            tx.commit();
            session.close();
            return Constant.REG_SUCCESS;
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            tx.rollback();
            return Constant.USER_INSERT_ERROR;
        } catch (Exception e) {
            tx.rollback();
            return Constant.UNKNOWN_ERROR;
        }
    }

    /**
     * Get user
     *
     * @param userinfo
     * @return
     */
    public User getUserInfo(User userinfo) {

        try {
            Query q = session.createQuery("from User as userinfo where userinfo.id=" + userinfo.getId() + "");
            userinfo = new User();
            userinfo = (User) q.uniqueResult();
            return userinfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get detail profile
     *
     * @param id
     * @return
     */
    public UserInfo getUserProfile(String id) {
        try {
            Query q = session.createQuery("from UserInfo as userinfo where userinfo.id='" + id + "'");
            UserInfo userProfile = new UserInfo();
            userProfile = (UserInfo) q.uniqueResult();
            return userProfile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * update detail profile
     *
     * @param userinfo
     * @return
     */
    public int setUserProfile(UserInfo userinfo) {
        org.hibernate.Transaction tx = null;
        try {
            tx = session.beginTransaction();
//            String hql = "update UserInfo set firstname=:firstname, lastname=:lastname, age=:age,skills=:skills  where id=:id ";
//            Query q = session.createQuery(hql);
//            q.setParameter("firstname", userinfo.getFirstname());
//            q.setParameter("id", userinfo.getId());
//            q.setParameter("lastname", userinfo.getLastname());
//            q.setParameter("age", userinfo.getAge());
//            q.setParameter("skills", userinfo.getSkills());
            session.saveOrUpdate(userinfo);
            tx.commit();
            return Constant.REG_SUCCESS;
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            tx.rollback();
            return Constant.USER_INSERT_ERROR;
        } catch (Exception e) {
            tx.rollback();
            return Constant.UNKNOWN_ERROR;
        }
    }

    /**
     * update path
     *
     * @param id
     * @param path
     * @return
     */
    public int setPath(String id, String path) {
        org.hibernate.Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "update UserInfo set resume=:resume  where id='" + id + "'";
            Query q = session.createQuery(hql);
            if (path != null) {
                q.setParameter("resume", path);
            }
            int result = q.executeUpdate();
            tx.commit();
            session.close();
            return result;
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            tx.rollback();
            return Constant.USER_INSERT_ERROR;
        } catch (Exception e) {
            tx.rollback();
            return Constant.UNKNOWN_ERROR;
        }

    }

    /**
     * Get all User
     *
     * @return
     */
    public List<User> getAllUsers() {
        try {
            Query q = session.createQuery("from User");
            List<User> users = q.list();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        session.close();
    }
}
