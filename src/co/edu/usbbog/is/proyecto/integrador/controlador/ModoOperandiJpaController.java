/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.is.proyecto.integrador.controlador;

import co.edu.usbbog.is.proyecto.integrador.controlador.exceptions.NonexistentEntityException;
import co.edu.usbbog.is.proyecto.integrador.controlador.exceptions.PreexistingEntityException;
import co.edu.usbbog.is.proyecto.integrador.modelo.ModoOperandi;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.usbbog.is.proyecto.integrador.modelo.Testimonio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author orion
 */
public class ModoOperandiJpaController implements Serializable {

    public ModoOperandiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ModoOperandi modoOperandi) throws PreexistingEntityException, Exception {
        if (modoOperandi.getTestimonioList() == null) {
            modoOperandi.setTestimonioList(new ArrayList<Testimonio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Testimonio> attachedTestimonioList = new ArrayList<Testimonio>();
            for (Testimonio testimonioListTestimonioToAttach : modoOperandi.getTestimonioList()) {
                testimonioListTestimonioToAttach = em.getReference(testimonioListTestimonioToAttach.getClass(), testimonioListTestimonioToAttach.getIdTestimonio());
                attachedTestimonioList.add(testimonioListTestimonioToAttach);
            }
            modoOperandi.setTestimonioList(attachedTestimonioList);
            em.persist(modoOperandi);
            for (Testimonio testimonioListTestimonio : modoOperandi.getTestimonioList()) {
                ModoOperandi oldDetalladoOfTestimonioListTestimonio = testimonioListTestimonio.getDetallado();
                testimonioListTestimonio.setDetallado(modoOperandi);
                testimonioListTestimonio = em.merge(testimonioListTestimonio);
                if (oldDetalladoOfTestimonioListTestimonio != null) {
                    oldDetalladoOfTestimonioListTestimonio.getTestimonioList().remove(testimonioListTestimonio);
                    oldDetalladoOfTestimonioListTestimonio = em.merge(oldDetalladoOfTestimonioListTestimonio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findModoOperandi(modoOperandi.getDetalle()) != null) {
                throw new PreexistingEntityException("ModoOperandi " + modoOperandi + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ModoOperandi modoOperandi) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ModoOperandi persistentModoOperandi = em.find(ModoOperandi.class, modoOperandi.getDetalle());
            List<Testimonio> testimonioListOld = persistentModoOperandi.getTestimonioList();
            List<Testimonio> testimonioListNew = modoOperandi.getTestimonioList();
            List<Testimonio> attachedTestimonioListNew = new ArrayList<Testimonio>();
            for (Testimonio testimonioListNewTestimonioToAttach : testimonioListNew) {
                testimonioListNewTestimonioToAttach = em.getReference(testimonioListNewTestimonioToAttach.getClass(), testimonioListNewTestimonioToAttach.getIdTestimonio());
                attachedTestimonioListNew.add(testimonioListNewTestimonioToAttach);
            }
            testimonioListNew = attachedTestimonioListNew;
            modoOperandi.setTestimonioList(testimonioListNew);
            modoOperandi = em.merge(modoOperandi);
            for (Testimonio testimonioListOldTestimonio : testimonioListOld) {
                if (!testimonioListNew.contains(testimonioListOldTestimonio)) {
                    testimonioListOldTestimonio.setDetallado(null);
                    testimonioListOldTestimonio = em.merge(testimonioListOldTestimonio);
                }
            }
            for (Testimonio testimonioListNewTestimonio : testimonioListNew) {
                if (!testimonioListOld.contains(testimonioListNewTestimonio)) {
                    ModoOperandi oldDetalladoOfTestimonioListNewTestimonio = testimonioListNewTestimonio.getDetallado();
                    testimonioListNewTestimonio.setDetallado(modoOperandi);
                    testimonioListNewTestimonio = em.merge(testimonioListNewTestimonio);
                    if (oldDetalladoOfTestimonioListNewTestimonio != null && !oldDetalladoOfTestimonioListNewTestimonio.equals(modoOperandi)) {
                        oldDetalladoOfTestimonioListNewTestimonio.getTestimonioList().remove(testimonioListNewTestimonio);
                        oldDetalladoOfTestimonioListNewTestimonio = em.merge(oldDetalladoOfTestimonioListNewTestimonio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = modoOperandi.getDetalle();
                if (findModoOperandi(id) == null) {
                    throw new NonexistentEntityException("The modoOperandi with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ModoOperandi modoOperandi;
            try {
                modoOperandi = em.getReference(ModoOperandi.class, id);
                modoOperandi.getDetalle();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The modoOperandi with id " + id + " no longer exists.", enfe);
            }
            List<Testimonio> testimonioList = modoOperandi.getTestimonioList();
            for (Testimonio testimonioListTestimonio : testimonioList) {
                testimonioListTestimonio.setDetallado(null);
                testimonioListTestimonio = em.merge(testimonioListTestimonio);
            }
            em.remove(modoOperandi);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ModoOperandi> findModoOperandiEntities() {
        return findModoOperandiEntities(true, -1, -1);
    }

    public List<ModoOperandi> findModoOperandiEntities(int maxResults, int firstResult) {
        return findModoOperandiEntities(false, maxResults, firstResult);
    }

    private List<ModoOperandi> findModoOperandiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ModoOperandi.class));
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

    public ModoOperandi findModoOperandi(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ModoOperandi.class, id);
        } finally {
            em.close();
        }
    }

    public int getModoOperandiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ModoOperandi> rt = cq.from(ModoOperandi.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
