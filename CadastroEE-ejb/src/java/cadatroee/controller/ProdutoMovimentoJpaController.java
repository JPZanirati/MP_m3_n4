/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadatroee.controller;

import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import cadastroee.model.Movimento;
import cadastroee.model.Produto;
import cadastroee.model.ProdutoMovimento;
import cadatroee.controller.exceptions.NonexistentEntityException;
import cadatroee.controller.exceptions.PreexistingEntityException;
import cadatroee.controller.exceptions.RollbackFailureException;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.UserTransaction;

/**
 *
 * @author JPZanirati
 */
public class ProdutoMovimentoJpaController implements Serializable {

    public ProdutoMovimentoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProdutoMovimento produtoMovimento) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Movimento idMovimento = produtoMovimento.getIdMovimento();
            if (idMovimento != null) {
                idMovimento = em.getReference(idMovimento.getClass(), idMovimento.getIdMovimento());
                produtoMovimento.setIdMovimento(idMovimento);
            }
            Produto idProduto = produtoMovimento.getIdProduto();
            if (idProduto != null) {
                idProduto = em.getReference(idProduto.getClass(), idProduto.getIdProduto());
                produtoMovimento.setIdProduto(idProduto);
            }
            em.persist(produtoMovimento);
            if (idMovimento != null) {
                idMovimento.getProdutoMovimentoCollection().add(produtoMovimento);
                idMovimento = em.merge(idMovimento);
            }
            if (idProduto != null) {
                idProduto.getProdutoMovimentoCollection().add(produtoMovimento);
                idProduto = em.merge(idProduto);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProdutoMovimento(produtoMovimento.getIdPM()) != null) {
                throw new PreexistingEntityException("ProdutoMovimento " + produtoMovimento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProdutoMovimento produtoMovimento) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProdutoMovimento persistentProdutoMovimento = em.find(ProdutoMovimento.class, produtoMovimento.getIdPM());
            Movimento idMovimentoOld = persistentProdutoMovimento.getIdMovimento();
            Movimento idMovimentoNew = produtoMovimento.getIdMovimento();
            Produto idProdutoOld = persistentProdutoMovimento.getIdProduto();
            Produto idProdutoNew = produtoMovimento.getIdProduto();
            if (idMovimentoNew != null) {
                idMovimentoNew = em.getReference(idMovimentoNew.getClass(), idMovimentoNew.getIdMovimento());
                produtoMovimento.setIdMovimento(idMovimentoNew);
            }
            if (idProdutoNew != null) {
                idProdutoNew = em.getReference(idProdutoNew.getClass(), idProdutoNew.getIdProduto());
                produtoMovimento.setIdProduto(idProdutoNew);
            }
            produtoMovimento = em.merge(produtoMovimento);
            if (idMovimentoOld != null && !idMovimentoOld.equals(idMovimentoNew)) {
                idMovimentoOld.getProdutoMovimentoCollection().remove(produtoMovimento);
                idMovimentoOld = em.merge(idMovimentoOld);
            }
            if (idMovimentoNew != null && !idMovimentoNew.equals(idMovimentoOld)) {
                idMovimentoNew.getProdutoMovimentoCollection().add(produtoMovimento);
                idMovimentoNew = em.merge(idMovimentoNew);
            }
            if (idProdutoOld != null && !idProdutoOld.equals(idProdutoNew)) {
                idProdutoOld.getProdutoMovimentoCollection().remove(produtoMovimento);
                idProdutoOld = em.merge(idProdutoOld);
            }
            if (idProdutoNew != null && !idProdutoNew.equals(idProdutoOld)) {
                idProdutoNew.getProdutoMovimentoCollection().add(produtoMovimento);
                idProdutoNew = em.merge(idProdutoNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = produtoMovimento.getIdPM();
                if (findProdutoMovimento(id) == null) {
                    throw new NonexistentEntityException("The produtoMovimento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ProdutoMovimento produtoMovimento;
            try {
                produtoMovimento = em.getReference(ProdutoMovimento.class, id);
                produtoMovimento.getIdPM();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produtoMovimento with id " + id + " no longer exists.", enfe);
            }
            Movimento idMovimento = produtoMovimento.getIdMovimento();
            if (idMovimento != null) {
                idMovimento.getProdutoMovimentoCollection().remove(produtoMovimento);
                idMovimento = em.merge(idMovimento);
            }
            Produto idProduto = produtoMovimento.getIdProduto();
            if (idProduto != null) {
                idProduto.getProdutoMovimentoCollection().remove(produtoMovimento);
                idProduto = em.merge(idProduto);
            }
            em.remove(produtoMovimento);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProdutoMovimento> findProdutoMovimentoEntities() {
        return findProdutoMovimentoEntities(true, -1, -1);
    }

    public List<ProdutoMovimento> findProdutoMovimentoEntities(int maxResults, int firstResult) {
        return findProdutoMovimentoEntities(false, maxResults, firstResult);
    }

    private List<ProdutoMovimento> findProdutoMovimentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProdutoMovimento.class));
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

    public ProdutoMovimento findProdutoMovimento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProdutoMovimento.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdutoMovimentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProdutoMovimento> rt = cq.from(ProdutoMovimento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
