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
@Table(name = "produto", catalog = "LOJA", schema = "LOJA")
@NamedQueries({
    @NamedQuery(name = "Produto_1.findAll", query = "SELECT p FROM Produto_1 p"),
    @NamedQuery(name = "Produto_1.findByIdProduto", query = "SELECT p FROM Produto_1 p WHERE p.idProduto = :idProduto"),
    @NamedQuery(name = "Produto_1.findByNomeProduto", query = "SELECT p FROM Produto_1 p WHERE p.nomeProduto = :nomeProduto"),
    @NamedQuery(name = "Produto_1.findByQtd", query = "SELECT p FROM Produto_1 p WHERE p.qtd = :qtd"),
    @NamedQuery(name = "Produto_1.findByValorUnitario", query = "SELECT p FROM Produto_1 p WHERE p.valorUnitario = :valorUnitario"),
    @NamedQuery(name = "Produto_1.findByTipo", query = "SELECT p FROM Produto_1 p WHERE p.tipo = :tipo")})
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idProduto")
    private Integer idProduto;
    @Basic(optional = false)
    @Column(name = "nomeProduto")
    private String nomeProduto;
    @Basic(optional = false)
    @Column(name = "qtd")
    private int qtd;
    @Basic(optional = false)
    @Column(name = "valorUnitario")
    private float valorUnitario;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @JoinColumn(name = "idJuridica", referencedColumnName = "idJuridica")
    @ManyToOne(optional = false)
    private PessoaJuridica idJuridica;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProduto")
    private Collection<ProdutoMovimento> produtoMovimentoCollection;

    public Produto() {
    }

    public Produto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public Produto(Integer idProduto, String nomeProduto, int qtd, long valorUnitario, String tipo) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.qtd = qtd;
        this.valorUnitario = valorUnitario;
        this.tipo = tipo;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public float getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(long valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public PessoaJuridica getIdJuridica() {
        return idJuridica;
    }

    public void setIdJuridica(PessoaJuridica idJuridica) {
        this.idJuridica = idJuridica;
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
        hash += (idProduto != null ? idProduto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produto)) {
            return false;
        }
        Produto other = (Produto) object;
        if ((this.idProduto == null && other.idProduto != null) || (this.idProduto != null && !this.idProduto.equals(other.idProduto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cadastroee.model.Produto_1[ idProduto=" + idProduto + " ]";
    }
    
}
