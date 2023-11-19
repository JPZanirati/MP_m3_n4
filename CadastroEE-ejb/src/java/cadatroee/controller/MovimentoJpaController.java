/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadatroee.controller;

import cadastroee.model.Movimento;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import cadastroee.model.PessoaFisica;
import cadastroee.model.ProdutoMovimento;
import cadatroee.controller.exceptions.IllegalOrphanException;
import cadatroee.controller.exceptions.NonexistentEntityException;
import cadatroee.controller.exceptions.PreexistingEntityException;
import cadatroee.controller.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.UserTransaction;

/**
 *
 * @author JPZanirati
 */
public class MovimentoJpaController implements Serializable {

    public MovimentoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Movimento movimento) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (movimento.getProdutoMovimentoCollection() == null) {
            movimento.setProdutoMovimentoCollection(new ArrayList<ProdutoMovimento>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PessoaFisica idFisica = movimento.getIdFisica();
            if (idFisica != null) {
                idFisica = em.getReference(idFisica.getClass(), idFisica.getIdFisica());
                movimento.setIdFisica(idFisica);
            }
            Collection<ProdutoMovimento> attachedProdutoMovimentoCollection = new ArrayList<ProdutoMovimento>();
            for (ProdutoMovimento produtoMovimentoCollectionProdutoMovimentoToAttach : movimento.getProdutoMovimentoCollection()) {
                produtoMovimentoCollectionProdutoMovimentoToAttach = em.getReference(produtoMovimentoCollectionProdutoMovimentoToAttach.getClass(), produtoMovimentoCollectionProdutoMovimentoToAttach.getIdPM());
                attachedProdutoMovimentoCollection.add(produtoMovimentoCollectionProdutoMovimentoToAttach);
            }
            movimento.setProdutoMovimentoCollection(attachedProdutoMovimentoCollection);
            em.persist(movimento);
            if (idFisica != null) {
                idFisica.getMovimentoCollection().add(movimento);
                idFisica = em.merge(idFisica);
            }
            for (ProdutoMovimento produtoMovimentoCollectionProdutoMovimento : movimento.getProdutoMovimentoCollection()) {
                Movimento oldIdMovimentoOfProdutoMovimentoCollectionProdutoMovimento = produtoMovimentoCollectionProdutoMovimento.getIdMovimento();
                produtoMovimentoCollectionProdutoMovimento.setIdMovimento(movimento);
                produtoMovimentoCollectionProdutoMovimento = em.merge(produtoMovimentoCollectionProdutoMovimento);
                if (oldIdMovimentoOfProdutoMovimentoCollectionProdutoMovimento != null) {
                    oldIdMovimentoOfProdutoMovimentoCollectionProdutoMovimento.getProdutoMovimentoCollection().remove(produtoMovimentoCollectionProdutoMovimento);
                    oldIdMovimentoOfProdutoMovimentoCollectionProdutoMovimento = em.merge(oldIdMovimentoOfProdutoMovimentoCollectionProdutoMovimento);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMovimento(movimento.getIdMovimento()) != null) {
                throw new PreexistingEntityException("Movimento " + movimento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Movimento movimento) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Movimento persistentMovimento = em.find(Movimento.class, movimento.getIdMovimento());
            PessoaFisica idFisicaOld = persistentMovimento.getIdFisica();
            PessoaFisica idFisicaNew = movimento.getIdFisica();
            Collection<ProdutoMovimento> produtoMovimentoCollectionOld = persistentMovimento.getProdutoMovimentoCollection();
            Collection<ProdutoMovimento> produtoMovimentoCollectionNew = movimento.getProdutoMovimentoCollection();
            List<String> illegalOrphanMessages = null;
            for (ProdutoMovimento produtoMovimentoCollectionOldProdutoMovimento : produtoMovimentoCollectionOld) {
                if (!produtoMovimentoCollectionNew.contains(produtoMovimentoCollectionOldProdutoMovimento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProdutoMovimento " + produtoMovimentoCollectionOldProdutoMovimento + " since its idMovimento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idFisicaNew != null) {
                idFisicaNew = em.getReference(idFisicaNew.getClass(), idFisicaNew.getIdFisica());
                movimento.setIdFisica(idFisicaNew);
            }
            Collection<ProdutoMovimento> attachedProdutoMovimentoCollectionNew = new ArrayList<ProdutoMovimento>();
            for (ProdutoMovimento produtoMovimentoCollectionNewProdutoMovimentoToAttach : produtoMovimentoCollectionNew) {
                produtoMovimentoCollectionNewProdutoMovimentoToAttach = em.getReference(produtoMovimentoCollectionNewProdutoMovimentoToAttach.getClass(), produtoMovimentoCollectionNewProdutoMovimentoToAttach.getIdPM());
                attachedProdutoMovimentoCollectionNew.add(produtoMovimentoCollectionNewProdutoMovimentoToAttach);
            }
            produtoMovimentoCollectionNew = attachedProdutoMovimentoCollectionNew;
            movimento.setProdutoMovimentoCollection(produtoMovimentoCollectionNew);
            movimento = em.merge(movimento);
            if (idFisicaOld != null && !idFisicaOld.equals(idFisicaNew)) {
                idFisicaOld.getMovimentoCollection().remove(movimento);
                idFisicaOld = em.merge(idFisicaOld);
            }
            if (idFisicaNew != null && !idFisicaNew.equals(idFisicaOld)) {
                idFisicaNew.getMovimentoCollection().add(movimento);
                idFisicaNew = em.merge(idFisicaNew);
            }
            for (ProdutoMovimento produtoMovimentoCollectionNewProdutoMovimento : produtoMovimentoCollectionNew) {
                if (!produtoMovimentoCollectionOld.contains(produtoMovimentoCollectionNewProdutoMovimento)) {
                    Movimento oldIdMovimentoOfProdutoMovimentoCollectionNewProdutoMovimento = produtoMovimentoCollectionNewProdutoMovimento.getIdMovimento();
                    produtoMovimentoCollectionNewProdutoMovimento.setIdMovimento(movimento);
                    produtoMovimentoCollectionNewProdutoMovimento = em.merge(produtoMovimentoCollectionNewProdutoMovimento);
                    if (oldIdMovimentoOfProdutoMovimentoCollectionNewProdutoMovimento != null && !oldIdMovimentoOfProdutoMovimentoCollectionNewProdutoMovimento.equals(movimento)) {
                        oldIdMovimentoOfProdutoMovimentoCollectionNewProdutoMovimento.getProdutoMovimentoCollection().remove(produtoMovimentoCollectionNewProdutoMovimento);
                        oldIdMovimentoOfProdutoMovimentoCollectionNewProdutoMovimento = em.merge(oldIdMovimentoOfProdutoMovimentoCollectionNewProdutoMovimento);
                    }
                }
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
                Integer id = movimento.getIdMovimento();
                if (findMovimento(id) == null) {
                    throw new NonexistentEntityException("The movimento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Movimento movimento;
            try {
                movimento = em.getReference(Movimento.class, id);
                movimento.getIdMovimento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ProdutoMovimento> produtoMovimentoCollectionOrphanCheck = movimento.getProdutoMovimentoCollection();
            for (ProdutoMovimento produtoMovimentoCollectionOrphanCheckProdutoMovimento : produtoMovimentoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Movimento (" + movimento + ") cannot be destroyed since the ProdutoMovimento " + produtoMovimentoCollectionOrphanCheckProdutoMovimento + " in its produtoMovimentoCollection field has a non-nullable idMovimento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PessoaFisica idFisica = movimento.getIdFisica();
            if (idFisica != null) {
                idFisica.getMovimentoCollection().remove(movimento);
                idFisica = em.merge(idFisica);
            }
            em.remove(movimento);
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

    public List<Movimento> findMovimentoEntities() {
        return findMovimentoEntities(true, -1, -1);
    }

    public List<Movimento> findMovimentoEntities(int maxResults, int firstResult) {
        return findMovimentoEntities(false, maxResults, firstResult);
    }

    private List<Movimento> findMovimentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Movimento.class));
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

    public Movimento findMovimento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Movimento.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Movimento> rt = cq.from(Movimento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
