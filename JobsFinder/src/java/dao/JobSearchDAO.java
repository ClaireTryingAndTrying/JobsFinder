/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import hibernate.HibernateUtils;
import java.util.ArrayList;
import java.util.List;
import model.Job;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author yueningli
 */
public class JobSearchDAO {

    Session session = null;

    public JobSearchDAO() {
        this.session = HibernateUtils.getSessionFactory().openSession();
    }

    /**
     * get search condition
     *
     * @param keyword
     * @param location
     * @param cName
     *
     */
    public List<Job> searchByCondition(String keyword, String location, String cName) {
        Criteria c = session.createCriteria(Job.class);
        //
        if (keyword != null) {
            c.add(Restrictions.or(Restrictions.like("title", "%" + keyword + "%"), Restrictions.like("description", "%" + keyword + "%")));
        }
        if (cName != null) {
            c.add(Restrictions.like("cname", "%" + cName + "%"));
        }
        if (location != null) {
            c.add(Restrictions.like("location", "%" + location + "%"));
        }
        List<Job> jobList = new ArrayList<Job>();
        jobList = (List<Job>) c.list();
        return jobList;
    }

    public void close() {
        session.close();
    }
}
