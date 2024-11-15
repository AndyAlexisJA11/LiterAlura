package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.models.Libro;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ILibroRepository extends JpaRepository<Libro, Long> {

    // Método para verificar si existe un libro con un título específico
    boolean existsByTitulo(String titulo);

    // Método para buscar libros que contengan una cadena en el título (ignora mayúsculas y minúsculas)
    Libro findByTituloContainsIgnoreCase(String titulo);

    // Método para buscar libros por idioma
    List<Libro> findByIdioma(String idioma);

    // Consulta para obtener los 10 libros con más descargas
    @Query("SELECT l FROM Libro l ORDER BY l.cantidadDescargas DESC")

    List<Libro> findTop10ByCantidadDescargas();
}
