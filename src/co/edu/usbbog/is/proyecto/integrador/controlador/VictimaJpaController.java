/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.is.proyecto.integrador.controlador;

import co.edu.usbbog.is.proyecto.integrador.controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.usbbog.is.proyecto.integrador.modelo.Testimonio;
import co.edu.usbbog.is.proyecto.integrador.modelo.Victima;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author orion
 */
public class VictimaJpaController implements Serializable {

    public VictimaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Victima victima) {
        if (victima.getTestimonioList() == null) {
            victima.setTestimonioList(new ArrayList<Testimonio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Testimonio> attachedTestimonioList = new ArrayList<Testimonio>();
            for (Testimonio testimonioListTestimonioToAttach : victima.getTestimonioList()) {
                testimonioListTestimonioToAttach = em.getReference(testimonioListTestimonioToAttach.getClass(), testimonioListTestimonioToAttach.getIdTestimonio());
                attachedTestimonioList.add(testimonioListTestimonioToAttach);
            }
            victima.setTestimonioList(attachedTestimonioList);
            em.persist(victima);
            for (Testimonio testimonioListTestimonio : victima.getTestimonioList()) {
                Victima oldIdUsuarioOfTestimonioListTestimonio = testimonioListTestimonio.getIdUsuario();
                testimonioListTestimonio.setIdUsuario(victima);
                testimonioListTestimonio = em.merge(testimonioListTestimonio);
                if (oldIdUsuarioOfTestimonioListTestimonio != null) {
                    oldIdUsuarioOfTestimonioListTestimonio.getTestimonioList().remove(testimonioListTestimonio);
                    oldIdUsuarioOfTestimonioListTestimonio = em.merge(oldIdUsuarioOfTestimonioListTestimonio);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Victima victima) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Victima persistentVictima = em.find(Victima.class, victima.getId());
            List<Testimonio> testimonioListOld = persistentVictima.getTestimonioList();
            List<Testimonio> testimonioListNew = victima.getTestimonioList();
            List<Testimonio> attachedTestimonioListNew = new ArrayList<Testimonio>();
            for (Testimonio testimonioListNewTestimonioToAttach : testimonioListNew) {
                testimonioListNewTestimonioToAttach = em.getReference(testimonioListNewTestimonioToAttach.getClass(), testimonioListNewTestimonioToAttach.getIdTestimonio());
                attachedTestimonioListNew.add(testimonioListNewTestimonioToAttach);
            }
            testimonioListNew = attachedTestimonioListNew;
            victima.setTestimonioList(testimonioListNew);
            victima = em.merge(victima);
            for (Testimonio testimonioListOldTestimonio : testimonioListOld) {
                if (!testimonioListNew.contains(testimonioListOldTestimonio)) {
                    testimonioListOldTestimonio.setIdUsuario(null);
                    testimonioListOldTestimonio = em.merge(testimonioListOldTestimonio);
                }
            }
            for (Testimonio testimonioListNewTestimonio : testimonioListNew) {
                if (!testimonioListOld.contains(testimonioListNewTestimonio)) {
                    Victima oldIdUsuarioOfTestimonioListNewTestimonio = testimonioListNewTestimonio.getIdUsuario();
                    testimonioListNewTestimonio.setIdUsuario(victima);
                    testimonioListNewTestimonio = em.merge(testimonioListNewTestimonio);
                    if (oldIdUsuarioOfTestimonioListNewTestimonio != null && !oldIdUsuarioOfTestimonioListNewTestimonio.equals(victima)) {
                        oldIdUsuarioOfTestimonioListNewTestimonio.getTestimonioList().remove(testimonioListNewTestimonio);
                        oldIdUsuarioOfTestimonioListNewTestimonio = em.merge(oldIdUsuarioOfTestimonioListNewTestimonio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = victima.getId();
                if (findVictima(id) == null) {
                    throw new NonexistentEntityException("The victima with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Victima victima;
            try {
                victima = em.getReference(Victima.class, id);
                victima.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The victima with id " + id + " no longer exists.", enfe);
            }
            List<Testimonio> testimonioList = victima.getTestimonioList();
            for (Testimonio testimonioListTestimonio : testimonioList) {
                testimonioListTestimonio.setIdUsuario(null);
                testimonioListTestimonio = em.merge(testimonioListTestimonio);
            }
            em.remove(victima);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Victima> findVictimaEntities() {
        return findVictimaEntities(true, -1, -1);
    }

    public List<Victima> findVictimaEntities(int maxResults, int firstResult) {
        return findVictimaEntities(false, maxResults, firstResult);
    }

    private List<Victima> findVictimaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Victima.class));
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

    public Victima findVictima(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Victima.class, id);
        } finally {
            em.close();
        }
    }

    public int getVictimaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Victima> rt = cq.from(Victima.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
