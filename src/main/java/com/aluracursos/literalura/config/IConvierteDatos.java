package com.aluracursos.literalura.config;

public interface IConvierteDatos {
    <T> T convertirDatosJsonAJava(String json, Class<T> clase);
}
