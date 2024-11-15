package com.aluracursos.literalura.models.records;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("id") Long numero,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<Autor> autor,
        @JsonAlias("subjects") List<String> genero,
        @JsonAlias("languages") List<String> idioma,
        @JsonAlias("formats") Map<String, String> imagen,
        @JsonAlias("download_count") Long cantidadDescargas
) {
        public String getUrlImagen() {
                // Obtén la URL de la imagen en formato JPEG, si no existe devuelve "Sin imagen"
                return imagen.getOrDefault("image/jpeg", "Sin imagen");
        }
}
