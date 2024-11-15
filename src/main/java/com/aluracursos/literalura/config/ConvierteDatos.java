package com.aluracursos.literalura.config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ConvierteDatos implements IConvierteDatos {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public <T> T convertirDatosJsonAJava(String json, Class<T> clase) {
        if (json == null || json.isBlank()) {
            throw new RuntimeException("La respuesta de la API está vacía o es nula");
        }
        try {
            return objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException( e);
        }
    }
}


