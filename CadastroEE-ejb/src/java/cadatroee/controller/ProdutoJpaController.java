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
import cadastroee.model.PessoaJuridica;
import cadastroee.model.Produto;
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
public class ProdutoJpaController implements Serializable {

    public ProdutoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Produto produto) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (produto.getProdutoMovimentoCollection() == null) {
            produto.setProdutoMovimentoCollection(new ArrayList<ProdutoMovimento>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PessoaJuridica idJuridica = produto.getIdJuridica();
            if (idJuridica != null) {
                idJuridica = em.getReference(idJuridica.getClass(), idJuridica.getIdJuridica());
                produto.setIdJuridica(idJuridica);
            }
            Collection<ProdutoMovimento> attachedProdutoMovimentoCollection = new ArrayList<ProdutoMovimento>();
            for (ProdutoMovimento produtoMovimentoCollectionProdutoMovimentoToAttach : produto.getProdutoMovimentoCollection()) {
                produtoMovimentoCollectionProdutoMovimentoToAttach = em.getReference(produtoMovimentoCollectionProdutoMovimentoToAttach.getClass(), produtoMovimentoCollectionProdutoMovimentoToAttach.getIdPM());
                attachedProdutoMovimentoCollection.add(produtoMovimentoCollectionProdutoMovimentoToAttach);
            }
            produto.setProdutoMovimentoCollection(attachedProdutoMovimentoCollection);
            em.persist(produto);
            if (idJuridica != null) {
                idJuridica.getProdutoCollection().add(produto);
                idJuridica = em.merge(idJuridica);
            }
            for (ProdutoMovimento produtoMovimentoCollectionProdutoMovimento : produto.getProdutoMovimentoCollection()) {
                Produto oldIdProdutoOfProdutoMovimentoCollectionProdutoMovimento = produtoMovimentoCollectionProdutoMovimento.getIdProduto();
                produtoMovimentoCollectionProdutoMovimento.setIdProduto(produto);
                produtoMovimentoCollectionProdutoMovimento = em.merge(produtoMovimentoCollectionProdutoMovimento);
                if (oldIdProdutoOfProdutoMovimentoCollectionProdutoMovimento != null) {
                    oldIdProdutoOfProdutoMovimentoCollectionProdutoMovimento.getProdutoMovimentoCollection().remove(produtoMovimentoCollectionProdutoMovimento);
                    oldIdProdutoOfProdutoMovimentoCollectionProdutoMovimento = em.merge(oldIdProdutoOfProdutoMovimentoCollectionProdutoMovimento);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProduto(produto.getIdProduto()) != null) {
                throw new PreexistingEntityException("Produto " + produto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Produto produto) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Produto persistentProduto = em.find(Produto.class, produto.getIdProduto());
            PessoaJuridica idJuridicaOld = persistentProduto.getIdJuridica();
            PessoaJuridica idJuridicaNew = produto.getIdJuridica();
            Collection<ProdutoMovimento> produtoMovimentoCollectionOld = persistentProduto.getProdutoMovimentoCollection();
            Collection<ProdutoMovimento> produtoMovimentoCollectionNew = produto.getProdutoMovimentoCollection();
            List<String> illegalOrphanMessages = null;
            for (ProdutoMovimento produtoMovimentoCollectionOldProdutoMovimento : produtoMovimentoCollectionOld) {
                if (!produtoMovimentoCollectionNew.contains(produtoMovimentoCollectionOldProdutoMovimento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProdutoMovimento " + produtoMovimentoCollectionOldProdutoMovimento + " since its idProduto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idJuridicaNew != null) {
                idJuridicaNew = em.getReference(idJuridicaNew.getClass(), idJuridicaNew.getIdJuridica());
                produto.setIdJuridica(idJuridicaNew);
            }
            Collection<ProdutoMovimento> attachedProdutoMovimentoCollectionNew = new ArrayList<ProdutoMovimento>();
            for (ProdutoMovimento produtoMovimentoCollectionNewProdutoMovimentoToAttach : produtoMovimentoCollectionNew) {
                produtoMovimentoCollectionNewProdutoMovimentoToAttach = em.getReference(produtoMovimentoCollectionNewProdutoMovimentoToAttach.getClass(), produtoMovimentoCollectionNewProdutoMovimentoToAttach.getIdPM());
                attachedProdutoMovimentoCollectionNew.add(produtoMovimentoCollectionNewProdutoMovimentoToAttach);
            }
            produtoMovimentoCollectionNew = attachedProdutoMovimentoCollectionNew;
            produto.setProdutoMovimentoCollection(produtoMovimentoCollectionNew);
            produto = em.merge(produto);
            if (idJuridicaOld != null && !idJuridicaOld.equals(idJuridicaNew)) {
                idJuridicaOld.getProdutoCollection().remove(produto);
                idJuridicaOld = em.merge(idJuridicaOld);
            }
            if (idJuridicaNew != null && !idJuridicaNew.equals(idJuridicaOld)) {
                idJuridicaNew.getProdutoCollection().add(produto);
                idJuridicaNew = em.merge(idJuridicaNew);
            }
            for (ProdutoMovimento produtoMovimentoCollectionNewProdutoMovimento : produtoMovimentoCollectionNew) {
                if (!produtoMovimentoCollectionOld.contains(produtoMovimentoCollectionNewProdutoMovimento)) {
                    Produto oldIdProdutoOfProdutoMovimentoCollectionNewProdutoMovimento = produtoMovimentoCollectionNewProdutoMovimento.getIdProduto();
                    produtoMovimentoCollectionNewProdutoMovimento.setIdProduto(produto);
                    produtoMovimentoCollectionNewProdutoMovimento = em.merge(produtoMovimentoCollectionNewProdutoMovimento);
                    if (oldIdProdutoOfProdutoMovimentoCollectionNewProdutoMovimento != null && !oldIdProdutoOfProdutoMovimentoCollectionNewProdutoMovimento.equals(produto)) {
                        oldIdProdutoOfProdutoMovimentoCollectionNewProdutoMovimento.getProdutoMovimentoCollection().remove(produtoMovimentoCollectionNewProdutoMovimento);
                        oldIdProdutoOfProdutoMovimentoCollectionNewProdutoMovimento = em.merge(oldIdProdutoOfProdutoMovimentoCollectionNewProdutoMovimento);
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
                Integer id = produto.getIdProduto();
                if (findProduto(id) == null) {
                    throw new NonexistentEntityException("The produto with id " + id + " no longer exists.");
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
            Produto produto;
            try {
                produto = em.getReference(Produto.class, id);
                produto.getIdProduto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ProdutoMovimento> produtoMovimentoCollectionOrphanCheck = produto.getProdutoMovimentoCollection();
            for (ProdutoMovimento produtoMovimentoCollectionOrphanCheckProdutoMovimento : produtoMovimentoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Produto (" + produto + ") cannot be destroyed since the ProdutoMovimento " + produtoMovimentoCollectionOrphanCheckProdutoMovimento + " in its produtoMovimentoCollection field has a non-nullable idProduto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PessoaJuridica idJuridica = produto.getIdJuridica();
            if (idJuridica != null) {
                idJuridica.getProdutoCollection().remove(produto);
                idJuridica = em.merge(idJuridica);
            }
            em.remove(produto);
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

    public List<Produto> findProdutoEntities() {
        return findProdutoEntities(true, -1, -1);
    }

    public List<Produto> findProdutoEntities(int maxResults, int firstResult) {
        return findProdutoEntities(false, maxResults, firstResult);
    }

    private List<Produto> findProdutoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produto.class));
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

    public Produto findProduto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdutoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produto> rt = cq.from(Produto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
