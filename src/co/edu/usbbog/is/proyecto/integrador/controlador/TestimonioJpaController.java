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
import co.edu.usbbog.is.proyecto.integrador.modelo.ModoOperandi;
import co.edu.usbbog.is.proyecto.integrador.modelo.Testimonio;
import co.edu.usbbog.is.proyecto.integrador.modelo.Victima;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author orion
 */
public class TestimonioJpaController implements Serializable {

    public TestimonioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Testimonio testimonio) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ModoOperandi detallado = testimonio.getDetallado();
            if (detallado != null) {
                detallado = em.getReference(detallado.getClass(), detallado.getDetalle());
                testimonio.setDetallado(detallado);
            }
            Victima idUsuario = testimonio.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getId());
                testimonio.setIdUsuario(idUsuario);
            }
            em.persist(testimonio);
            if (detallado != null) {
                detallado.getTestimonioList().add(testimonio);
                detallado = em.merge(detallado);
            }
            if (idUsuario != null) {
                idUsuario.getTestimonioList().add(testimonio);
                idUsuario = em.merge(idUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Testimonio testimonio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Testimonio persistentTestimonio = em.find(Testimonio.class, testimonio.getIdTestimonio());
            ModoOperandi detalladoOld = persistentTestimonio.getDetallado();
            ModoOperandi detalladoNew = testimonio.getDetallado();
            Victima idUsuarioOld = persistentTestimonio.getIdUsuario();
            Victima idUsuarioNew = testimonio.getIdUsuario();
            if (detalladoNew != null) {
                detalladoNew = em.getReference(detalladoNew.getClass(), detalladoNew.getDetalle());
                testimonio.setDetallado(detalladoNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getId());
                testimonio.setIdUsuario(idUsuarioNew);
            }
            testimonio = em.merge(testimonio);
            if (detalladoOld != null && !detalladoOld.equals(detalladoNew)) {
                detalladoOld.getTestimonioList().remove(testimonio);
                detalladoOld = em.merge(detalladoOld);
            }
            if (detalladoNew != null && !detalladoNew.equals(detalladoOld)) {
                detalladoNew.getTestimonioList().add(testimonio);
                detalladoNew = em.merge(detalladoNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getTestimonioList().remove(testimonio);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getTestimonioList().add(testimonio);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = testimonio.getIdTestimonio();
                if (findTestimonio(id) == null) {
                    throw new NonexistentEntityException("The testimonio with id " + id + " no longer exists.");
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
            Testimonio testimonio;
            try {
                testimonio = em.getReference(Testimonio.class, id);
                testimonio.getIdTestimonio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The testimonio with id " + id + " no longer exists.", enfe);
            }
            ModoOperandi detallado = testimonio.getDetallado();
            if (detallado != null) {
                detallado.getTestimonioList().remove(testimonio);
                detallado = em.merge(detallado);
            }
            Victima idUsuario = testimonio.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getTestimonioList().remove(testimonio);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(testimonio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Testimonio> findTestimonioEntities() {
        return findTestimonioEntities(true, -1, -1);
    }

    public List<Testimonio> findTestimonioEntities(int maxResults, int firstResult) {
        return findTestimonioEntities(false, maxResults, firstResult);
    }

    private List<Testimonio> findTestimonioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Testimonio.class));
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

    public Testimonio findTestimonio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Testimonio.class, id);
        } finally {
            em.close();
        }
    }

    public int getTestimonioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Testimonio> rt = cq.from(Testimonio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
