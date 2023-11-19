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
@Table(name = "pessoa_juridica", catalog = "LOJA", schema = "LOJA")
@NamedQueries({
    @NamedQuery(name = "PessoaJuridica_1.findAll", query = "SELECT p FROM PessoaJuridica_1 p"),
    @NamedQuery(name = "PessoaJuridica_1.findByIdJuridica", query = "SELECT p FROM PessoaJuridica_1 p WHERE p.idJuridica = :idJuridica"),
    @NamedQuery(name = "PessoaJuridica_1.findByCnpj", query = "SELECT p FROM PessoaJuridica_1 p WHERE p.cnpj = :cnpj"),
    @NamedQuery(name = "PessoaJuridica_1.findByRazaoSocial", query = "SELECT p FROM PessoaJuridica_1 p WHERE p.razaoSocial = :razaoSocial")})
public class PessoaJuridica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idJuridica")
    private Integer idJuridica;
    @Basic(optional = false)
    @Column(name = "cnpj")
    private String cnpj;
    @Basic(optional = false)
    @Column(name = "razaoSocial")
    private String razaoSocial;
    @JoinColumn(name = "idPessoa", referencedColumnName = "idPessoa")
    @ManyToOne
    private Pessoa idPessoa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idJuridica")
    private Collection<Produto> produtoCollection;

    public PessoaJuridica() {
    }

    public PessoaJuridica(Integer idJuridica) {
        this.idJuridica = idJuridica;
    }

    public PessoaJuridica(Integer idJuridica, String cnpj, String razaoSocial) {
        this.idJuridica = idJuridica;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }

    public Integer getIdJuridica() {
        return idJuridica;
    }

    public void setIdJuridica(Integer idJuridica) {
        this.idJuridica = idJuridica;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public Pessoa getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Pessoa idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Collection<Produto> getProdutoCollection() {
        return produtoCollection;
    }

    public void setProdutoCollection(Collection<Produto> produtoCollection) {
        this.produtoCollection = produtoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idJuridica != null ? idJuridica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PessoaJuridica)) {
            return false;
        }
        PessoaJuridica other = (PessoaJuridica) object;
        if ((this.idJuridica == null && other.idJuridica != null) || (this.idJuridica != null && !this.idJuridica.equals(other.idJuridica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cadastroee.model.PessoaJuridica_1[ idJuridica=" + idJuridica + " ]";
    }
    
}
