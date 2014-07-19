/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xcoders.jpacontroller;

import com.xcoders.entity.Member;
import com.xcoders.jpacontroller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author ravindu
 */
public class MemberJpaController implements Serializable {

    public MemberJpaController() {
        this.emf = Persistence.createEntityManagerFactory("PokerServicePU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Member member) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(member);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Member member) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            member = em.merge(member);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = member.getId();
                if (findMember(id) == null) {
                    throw new NonexistentEntityException("The member with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Member member;
            try {
                member = em.getReference(Member.class, id);
                member.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The member with id " + id + " no longer exists.", enfe);
            }
            em.remove(member);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Member> findMemberEntities() {
        return findMemberEntities(true, -1, -1);
    }

    public List<Member> findMemberEntities(int maxResults, int firstResult) {
        return findMemberEntities(false, maxResults, firstResult);
    }

    private List<Member> findMemberEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Member.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Member findMember(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Member.class, id);
        } finally {
            em.close();
        }
    }

    public int getMemberCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Member> rt = cq.from(Member.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public Member findMember(String userName,String password){
        EntityManager em = getEntityManager();
        try{
            Query query = em.createQuery("select m from Member m where m.userName= :un and m.password= :p");
            query.setParameter("un", userName);
            query.setParameter("p", password);
            try{
                return (Member)query.getSingleResult();
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
            
        }finally{
            em.close();
        }
    }
}
