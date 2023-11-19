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
import cadastroee.model.Pessoa;
import cadastroee.model.Movimento;
import cadastroee.model.PessoaFisica;
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
public class PessoaFisicaJpaController implements Serializable {

    public PessoaFisicaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PessoaFisica pessoaFisica) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (pessoaFisica.getMovimentoCollection() == null) {
            pessoaFisica.setMovimentoCollection(new ArrayList<Movimento>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Pessoa idPessoa = pessoaFisica.getIdPessoa();
            if (idPessoa != null) {
                idPessoa = em.getReference(idPessoa.getClass(), idPessoa.getIdPessoa());
                pessoaFisica.setIdPessoa(idPessoa);
            }
            Collection<Movimento> attachedMovimentoCollection = new ArrayList<Movimento>();
            for (Movimento movimentoCollectionMovimentoToAttach : pessoaFisica.getMovimentoCollection()) {
                movimentoCollectionMovimentoToAttach = em.getReference(movimentoCollectionMovimentoToAttach.getClass(), movimentoCollectionMovimentoToAttach.getIdMovimento());
                attachedMovimentoCollection.add(movimentoCollectionMovimentoToAttach);
            }
            pessoaFisica.setMovimentoCollection(attachedMovimentoCollection);
            em.persist(pessoaFisica);
            if (idPessoa != null) {
                idPessoa.getPessoaFisicaCollection().add(pessoaFisica);
                idPessoa = em.merge(idPessoa);
            }
            for (Movimento movimentoCollectionMovimento : pessoaFisica.getMovimentoCollection()) {
                PessoaFisica oldIdFisicaOfMovimentoCollectionMovimento = movimentoCollectionMovimento.getIdFisica();
                movimentoCollectionMovimento.setIdFisica(pessoaFisica);
                movimentoCollectionMovimento = em.merge(movimentoCollectionMovimento);
                if (oldIdFisicaOfMovimentoCollectionMovimento != null) {
                    oldIdFisicaOfMovimentoCollectionMovimento.getMovimentoCollection().remove(movimentoCollectionMovimento);
                    oldIdFisicaOfMovimentoCollectionMovimento = em.merge(oldIdFisicaOfMovimentoCollectionMovimento);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPessoaFisica(pessoaFisica.getIdFisica()) != null) {
                throw new PreexistingEntityException("PessoaFisica " + pessoaFisica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PessoaFisica pessoaFisica) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PessoaFisica persistentPessoaFisica = em.find(PessoaFisica.class, pessoaFisica.getIdFisica());
            Pessoa idPessoaOld = persistentPessoaFisica.getIdPessoa();
            Pessoa idPessoaNew = pessoaFisica.getIdPessoa();
            Collection<Movimento> movimentoCollectionOld = persistentPessoaFisica.getMovimentoCollection();
            Collection<Movimento> movimentoCollectionNew = pessoaFisica.getMovimentoCollection();
            List<String> illegalOrphanMessages = null;
            for (Movimento movimentoCollectionOldMovimento : movimentoCollectionOld) {
                if (!movimentoCollectionNew.contains(movimentoCollectionOldMovimento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Movimento " + movimentoCollectionOldMovimento + " since its idFisica field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPessoaNew != null) {
                idPessoaNew = em.getReference(idPessoaNew.getClass(), idPessoaNew.getIdPessoa());
                pessoaFisica.setIdPessoa(idPessoaNew);
            }
            Collection<Movimento> attachedMovimentoCollectionNew = new ArrayList<Movimento>();
            for (Movimento movimentoCollectionNewMovimentoToAttach : movimentoCollectionNew) {
                movimentoCollectionNewMovimentoToAttach = em.getReference(movimentoCollectionNewMovimentoToAttach.getClass(), movimentoCollectionNewMovimentoToAttach.getIdMovimento());
                attachedMovimentoCollectionNew.add(movimentoCollectionNewMovimentoToAttach);
            }
            movimentoCollectionNew = attachedMovimentoCollectionNew;
            pessoaFisica.setMovimentoCollection(movimentoCollectionNew);
            pessoaFisica = em.merge(pessoaFisica);
            if (idPessoaOld != null && !idPessoaOld.equals(idPessoaNew)) {
                idPessoaOld.getPessoaFisicaCollection().remove(pessoaFisica);
                idPessoaOld = em.merge(idPessoaOld);
            }
            if (idPessoaNew != null && !idPessoaNew.equals(idPessoaOld)) {
                idPessoaNew.getPessoaFisicaCollection().add(pessoaFisica);
                idPessoaNew = em.merge(idPessoaNew);
            }
            for (Movimento movimentoCollectionNewMovimento : movimentoCollectionNew) {
                if (!movimentoCollectionOld.contains(movimentoCollectionNewMovimento)) {
                    PessoaFisica oldIdFisicaOfMovimentoCollectionNewMovimento = movimentoCollectionNewMovimento.getIdFisica();
                    movimentoCollectionNewMovimento.setIdFisica(pessoaFisica);
                    movimentoCollectionNewMovimento = em.merge(movimentoCollectionNewMovimento);
                    if (oldIdFisicaOfMovimentoCollectionNewMovimento != null && !oldIdFisicaOfMovimentoCollectionNewMovimento.equals(pessoaFisica)) {
                        oldIdFisicaOfMovimentoCollectionNewMovimento.getMovimentoCollection().remove(movimentoCollectionNewMovimento);
                        oldIdFisicaOfMovimentoCollectionNewMovimento = em.merge(oldIdFisicaOfMovimentoCollectionNewMovimento);
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
                Integer id = pessoaFisica.getIdFisica();
                if (findPessoaFisica(id) == null) {
                    throw new NonexistentEntityException("The pessoaFisica with id " + id + " no longer exists.");
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
            PessoaFisica pessoaFisica;
            try {
                pessoaFisica = em.getReference(PessoaFisica.class, id);
                pessoaFisica.getIdFisica();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoaFisica with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Movimento> movimentoCollectionOrphanCheck = pessoaFisica.getMovimentoCollection();
            for (Movimento movimentoCollectionOrphanCheckMovimento : movimentoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PessoaFisica (" + pessoaFisica + ") cannot be destroyed since the Movimento " + movimentoCollectionOrphanCheckMovimento + " in its movimentoCollection field has a non-nullable idFisica field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pessoa idPessoa = pessoaFisica.getIdPessoa();
            if (idPessoa != null) {
                idPessoa.getPessoaFisicaCollection().remove(pessoaFisica);
                idPessoa = em.merge(idPessoa);
            }
            em.remove(pessoaFisica);
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

    public List<PessoaFisica> findPessoaFisicaEntities() {
        return findPessoaFisicaEntities(true, -1, -1);
    }

    public List<PessoaFisica> findPessoaFisicaEntities(int maxResults, int firstResult) {
        return findPessoaFisicaEntities(false, maxResults, firstResult);
    }

    private List<PessoaFisica> findPessoaFisicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PessoaFisica.class));
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

    public PessoaFisica findPessoaFisica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PessoaFisica.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoaFisicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PessoaFisica> rt = cq.from(PessoaFisica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
