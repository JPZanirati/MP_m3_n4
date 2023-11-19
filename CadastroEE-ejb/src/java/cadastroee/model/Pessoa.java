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
@Table(name = "pessoa", catalog = "LOJA", schema = "LOJA")
@NamedQueries({
    @NamedQuery(name = "Pessoa_1.findAll", query = "SELECT p FROM Pessoa_1 p"),
    @NamedQuery(name = "Pessoa_1.findByIdPessoa", query = "SELECT p FROM Pessoa_1 p WHERE p.idPessoa = :idPessoa"),
    @NamedQuery(name = "Pessoa_1.findByNome", query = "SELECT p FROM Pessoa_1 p WHERE p.nome = :nome"),
    @NamedQuery(name = "Pessoa_1.findByCidade", query = "SELECT p FROM Pessoa_1 p WHERE p.cidade = :cidade"),
    @NamedQuery(name = "Pessoa_1.findByEndereco", query = "SELECT p FROM Pessoa_1 p WHERE p.endereco = :endereco"),
    @NamedQuery(name = "Pessoa_1.findByUf", query = "SELECT p FROM Pessoa_1 p WHERE p.uf = :uf"),
    @NamedQuery(name = "Pessoa_1.findByTelefone", query = "SELECT p FROM Pessoa_1 p WHERE p.telefone = :telefone"),
    @NamedQuery(name = "Pessoa_1.findByTipoPessoa", query = "SELECT p FROM Pessoa_1 p WHERE p.tipoPessoa = :tipoPessoa")})
public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idPessoa")
    private Integer idPessoa;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "cidade")
    private String cidade;
    @Basic(optional = false)
    @Column(name = "endereco")
    private String endereco;
    @Basic(optional = false)
    @Column(name = "uf")
    private String uf;
    @Column(name = "telefone")
    private Integer telefone;
    @Basic(optional = false)
    @Column(name = "tipoPessoa")
    private String tipoPessoa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPessoa")
    private Collection<PessoaFisica> pessoaFisicaCollection;
    @OneToMany(mappedBy = "idPessoa")
    private Collection<PessoaJuridica> pessoaJuridicaCollection;
    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    @ManyToOne
    private Usuario idUser;

    public Pessoa() {
    }

    public Pessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Pessoa(Integer idPessoa, String nome, String cidade, String endereco, String uf, String tipoPessoa) {
        this.idPessoa = idPessoa;
        this.nome = nome;
        this.cidade = cidade;
        this.endereco = endereco;
        this.uf = uf;
        this.tipoPessoa = tipoPessoa;
    }

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Integer getTelefone() {
        return telefone;
    }

    public void setTelefone(Integer telefone) {
        this.telefone = telefone;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public Collection<PessoaFisica> getPessoaFisicaCollection() {
        return pessoaFisicaCollection;
    }

    public void setPessoaFisicaCollection(Collection<PessoaFisica> pessoaFisicaCollection) {
        this.pessoaFisicaCollection = pessoaFisicaCollection;
    }

    public Collection<PessoaJuridica> getPessoaJuridicaCollection() {
        return pessoaJuridicaCollection;
    }

    public void setPessoaJuridicaCollection(Collection<PessoaJuridica> pessoaJuridicaCollection) {
        this.pessoaJuridicaCollection = pessoaJuridicaCollection;
    }

    public Usuario getIdUser() {
        return idUser;
    }

    public void setIdUser(Usuario idUser) {
        this.idUser = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPessoa != null ? idPessoa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pessoa)) {
            return false;
        }
        Pessoa other = (Pessoa) object;
        if ((this.idPessoa == null && other.idPessoa != null) || (this.idPessoa != null && !this.idPessoa.equals(other.idPessoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cadastroee.model.Pessoa_1[ idPessoa=" + idPessoa + " ]";
    }
    
}
