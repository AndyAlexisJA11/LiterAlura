package com.aluracursos.literalura.models;

import com.aluracursos.literalura.dtos.Genero;
import com.aluracursos.literalura.models.records.DatosLibro;
import jakarta.persistence.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long libroId;

    @Column(unique = true)
    private String titulo; // Verifica en la BD que este valor sea único

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    private String idioma;
    private String imagen;
    private Long cantidadDescargas;

    // Constructor de copia
    public Libro(Libro libro) {
        this.id = libro.id;
        this.libroId = libro.libroId;
        this.titulo = libro.titulo;
        this.autor = libro.autor;
        this.genero = libro.genero;
        this.idioma = libro.idioma;
        this.imagen = libro.imagen;
        this.cantidadDescargas = libro.cantidadDescargas;
    }

    public Libro() { }

    // Constructor desde DatosLibro
    public Libro(DatosLibro datosLibro) {
        this.libroId = datosLibro.numero();
        this.titulo = datosLibro.titulo();

        if (datosLibro.autor() != null && !datosLibro.autor().isEmpty()) {
            this.autor = new Autor(datosLibro.autor().get(0));
        } else {
            this.autor = null;
        }

        this.genero = generarGenero(datosLibro.genero());
        this.idioma = modificarIdioma(datosLibro.idioma());
        this.imagen = datosLibro.getUrlImagen();
        this.cantidadDescargas = datosLibro.cantidadDescargas();
    }

    private Genero generarGenero(List<String> generos) {
        if (generos == null || generos.isEmpty()) {
            return Genero.DESCONOCIDO;
        }

        Optional<String> firstGenero = generos.stream()
                .map(g -> {
                    int index = g.indexOf("--");
                    return index != -1 ? g.substring(index + 2).trim() : null;
                })
                .filter(Objects::nonNull)
                .findFirst();

        return firstGenero.map(Genero::fromString).orElse(Genero.DESCONOCIDO);
    }

    private String modificarIdioma(List<String> idiomas) {
        if (idiomas == null || idiomas.isEmpty()) {
            return "Desconocido";
        }
        return idiomas.get(0);
    }

    private String modificarImagen(Map<String, String> media) {
        if (media == null || !media.containsKey("default") || media.get("default").isEmpty()) {
            return "Sin imagen";
        }
        return media.get("default");
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Long getCantidadDescargas() {
        return cantidadDescargas;
    }

    public void setCantidadDescargas(Long cantidadDescargas) {
        this.cantidadDescargas = cantidadDescargas;
    }

    @Override
    public String toString() {
        return  "  \nid=" + id +
                ", \nlibroId=" + libroId +
                ", \ntitulo='" + titulo + '\'' +
                ", \nautor=" + (autor != null ? autor.getNombre() : "N/A") +
                ", \ngenero=" + genero +
                ", \nidioma=" + idioma +
                ", \nimagen=" + imagen +
                ", \ncantidadDescargas=" + cantidadDescargas;
    }
}
