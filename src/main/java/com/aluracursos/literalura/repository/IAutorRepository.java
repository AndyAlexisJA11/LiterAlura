package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAutorRepository extends JpaRepository<com.aluracursos.literalura.models.Autor, Long> {
    List<com.aluracursos.literalura.models.Autor> findAll();

    List<com.aluracursos.literalura.models.Autor> findByCumpleaniosLessThanOrFechaFallecimientoGreaterThanEqual(int aniosBuscado, int aniosBuscado1);
    Optional<Autor> findFirstByNombreContainsIgnoreCase(String escritor);
}
