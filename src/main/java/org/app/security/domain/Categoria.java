package org.app.security.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String titulo;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "subcategoria")
    private Categoria subcategoria;

    @OneToMany(mappedBy = "subcategoria")
    private List<Categoria> subCategorias = new ArrayList();

    /*
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos = new ArrayList();
    */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Categoria getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Categoria subcategoria) {
        this.subcategoria = subcategoria;
    }

    public List<Categoria> getSubCategorias() {
        return subCategorias;
    }

    public void setSubCategorias(List<Categoria> subCategorias) {
        this.subCategorias = subCategorias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;

        Categoria categoria = (Categoria) o;

        return getId() != null ? getId().equals(categoria.getId()) : categoria.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                '}';
    }
}