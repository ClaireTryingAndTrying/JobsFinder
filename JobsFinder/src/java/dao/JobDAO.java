/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import hibernate.HibernateUtils;
import model.Job;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author yueningli
 */
public class JobDAO {

    Session session = null;
    public JobDAO() {
        this.session = HibernateUtils.getSessionFactory().openSession();
    }
    /**
     * get job
     * @param job
     */
    public void saveJob(Job job) {
        try {
            Transaction tx = session.beginTransaction();
            job.setId(String.valueOf(job.hashCode()));
            session.save(job);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void close() {
        session.close();
    }
}