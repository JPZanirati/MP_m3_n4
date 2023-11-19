/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroee.model;

import java.io.Serializable;
import java.util.Collection;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author JPZanirati
 */
@Entity
@Table(name = "movimento", catalog = "LOJA", schema = "LOJA")
@NamedQueries({
    @NamedQuery(name = "Movimento_1.findAll", query = "SELECT m FROM Movimento_1 m"),
    @NamedQuery(name = "Movimento_1.findByIdMovimento", query = "SELECT m FROM Movimento_1 m WHERE m.idMovimento = :idMovimento"),
    @NamedQuery(name = "Movimento_1.findByQtdPedido", query = "SELECT m FROM Movimento_1 m WHERE m.qtdPedido = :qtdPedido"),
    @NamedQuery(name = "Movimento_1.findByPrecoTotal", query = "SELECT m FROM Movimento_1 m WHERE m.precoTotal = :precoTotal")})
public class Movimento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idMovimento")
    private Integer idMovimento;
    @Basic(optional = false)
    @Column(name = "qtd_pedido")
    private int qtdPedido;
    @Basic(optional = false)
    @Column(name = "precoTotal")
    private float precoTotal;
    @JoinColumn(name = "idFisica", referencedColumnName = "idFisica")
    @ManyToOne(optional = false)
    private PessoaFisica idFisica;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMovimento")
    private Collection<ProdutoMovimento> produtoMovimentoCollection;

    public Movimento() {
    }

    public Movimento(Integer idMovimento) {
        this.idMovimento = idMovimento;
    }

    public Movimento(Integer idMovimento, int qtdPedido, float precoTotal) {
        this.idMovimento = idMovimento;
        this.qtdPedido = qtdPedido;
        this.precoTotal = precoTotal;
    }

    public Integer getIdMovimento() {
        return idMovimento;
    }

    public void setIdMovimento(Integer idMovimento) {
        this.idMovimento = idMovimento;
    }

    public int getQtdPedido() {
        return qtdPedido;
    }

    public void setQtdPedido(int qtdPedido) {
        this.qtdPedido = qtdPedido;
    }

    public float getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(long precoTotal) {
        this.precoTotal = precoTotal;
    }

    public PessoaFisica getIdFisica() {
        return idFisica;
    }

    public void setIdFisica(PessoaFisica idFisica) {
        this.idFisica = idFisica;
    }

    public Collection<ProdutoMovimento> getProdutoMovimentoCollection() {
        return produtoMovimentoCollection;
    }

    public void setProdutoMovimentoCollection(Collection<ProdutoMovimento> produtoMovimentoCollection) {
        this.produtoMovimentoCollection = produtoMovimentoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMovimento != null ? idMovimento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Movimento)) {
            return false;
        }
        Movimento other = (Movimento) object;
        if ((this.idMovimento == null && other.idMovimento != null) || (this.idMovimento != null && !this.idMovimento.equals(other.idMovimento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cadastroee.model.Movimento_1[ idMovimento=" + idMovimento + " ]";
    }
    
}
