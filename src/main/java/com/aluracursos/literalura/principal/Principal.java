package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.config.ConsumoAPI;
import com.aluracursos.literalura.config.ConvierteDatos;
import com.aluracursos.literalura.models.Autor;
import com.aluracursos.literalura.models.Libro;
import com.aluracursos.literalura.models.LibroRespuestaApi;
import com.aluracursos.literalura.models.records.DatosLibro;
import com.aluracursos.literalura.repository.IAutorRepository;
import com.aluracursos.literalura.repository.ILibroRepository;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class Principal {
    private Scanner entrada = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books?search=";
    private ConvierteDatos convertir = new ConvierteDatos();
    private List<Libro> datosLibro = new ArrayList<>();
    private ILibroRepository libroRepository;
    private IAutorRepository autorRepository;

    public Principal(ILibroRepository libroRepository, IAutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }
    public void consumo() throws Exception {
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    ***************************
                    Ingrese la Opcion que desee
                    ***************************
                    *****Libreria Alura AndyJ11*****
                    1 - Agregar Libro por Nombre
                    2 - Libros buscados
                    3 - Buscar libro por Nombre
                    4 - Buscar todos los Autores de libros buscados
                    5 - Buscar Autores por año
                    6 - Buscar Libros por Idioma
                    7 - Top 10 Libros mas Descargados
                    8 - Buscar Autor por Nombre
                    0 - Salir""";
            try {
                System.out.println(menu);
                opcion = entrada.nextInt();
                entrada.nextLine();
            }catch (InputMismatchException e){
                System.out.println("Ingrese una opcion valida");
                entrada.nextLine();
                continue;
            }
            switch (opcion){
                case 1 :
                    buscarLibroEnLaWeb();
                    break;
                case 2:
                    librosBuscados();
                    break;
                case 3:
                    buscarLibroPorNombre();
                    break;
                case 4:
                    buscarAutores();
                    break;
                case 5:
                    buscarAutorPorAnio();
                    break;
                case 6:
                    buscarLibrosPorIdioma();
                    break;
                case 7:
                    top10LibrosMasDescargados();
                    break;
                case 8:
                    buscarAutorPorNombre();
                    break;
                case 0:
                    opcion = 0;
                    System.out.println("Gracias por usar LiteraLura :) ");
                    break;
                default:
                    System.out.println("Ingrese una opcion valida");
                    consumo();
                    break;
            }
        }
    }
    private Libro getDatosLibro() throws Exception {
        System.out.println("Ingrese el nombre del libro");
        var nombreDelLibro = entrada.nextLine().toLowerCase();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreDelLibro.replace(" ", "%20"));
        LibroRespuestaApi datos = convertir.convertirDatosJsonAJava(json, LibroRespuestaApi.class);

        if (datos != null && datos.getResultadoLibros() != null && !datos.getResultadoLibros().isEmpty()) {
            DatosLibro primerLibro = datos.getResultadoLibros().get(0);
            return new Libro(primerLibro);
        } else {
            System.out.println("No se encontró un libro");
            return null;
        }
    }


    private void buscarLibroEnLaWeb() throws Exception {
        // Verificar que el libro tenga título antes de buscarlo
        Libro libro = getDatosLibro();
        if (libro == null || libro.getTitulo() == null || libro.getTitulo().isEmpty()) {
            System.out.println("Libro no encontrado o título vacío, intente de nuevo");
            return;
        }

        try {
            // Verificar si el libro ya existe en la base de datos
            boolean libroExists = libroRepository.existsByTitulo(libro.getTitulo());
            if (libroExists) {
                System.out.println("El libro ya existe en la base de datos");
            } else {
                // Guardar el libro si no existe
                libroRepository.save(libro);
                System.out.println("Libro guardado: " + libro.toString());
            }
        } catch (InvalidDataAccessApiUsageException e) {
            System.out.println("Error al acceder a la base de datos: No se puede persistir el libro.");
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    private void librosBuscados() {
        try {
            // Recuperar todos los libros desde la base de datos
            List<Libro> libros = libroRepository.findAll();
            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros en la base de datos.");
            } else {
                System.out.println("Libros encontrados en la base de datos:");
                for (Libro libro : libros) {
                    System.out.println(libro.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar libros en la base de datos: " + e.getMessage());
        }
    }


    private void buscarLibroPorNombre() {
        System.out.println("Ingrese titulo del libro que quiere buscar: ");
        var titulo = entrada.nextLine();
        Libro libroBuscado = libroRepository.findByTituloContainsIgnoreCase(titulo);
        if (libroBuscado != null) {
            System.out.println("el libro buscado fue: " + libroBuscado);
        } else {
            System.out.println("el libro no se encontro");
        }
    }

    private void buscarAutores(){
        List<com.aluracursos.literalura.models.Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()){
            System.out.println("No se encontraron libros en la base de datos. \n" );
        }else {
            System.out.println("Los autores encontrados fueron:  \n");
            Set<String> autoresUnicos = new HashSet<>();
            for (com.aluracursos.literalura.models.Autor autor : autores) {
                if (autoresUnicos.add(autor.getNombre())){
                    System.out.println(autor.getNombre());
                }
            }
        }
    }

    private void buscarLibrosPorIdioma(){
        System.out.println("Ingrese idioma que desea buscar: \n");
        System.out.println("""
                Puede elegir: 
                es - para Español
                en - para ingles""");
        var idioma = entrada.nextLine();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()){
            System.out.println("No se encontraron libros en la base de datos");
        }else {
            System.out.println("Libros segun idioma encontrados en la base de datos: ");
            for (Libro libro : librosPorIdioma){
                System.out.println(libro.toString());
            }
        }
    }

    private void buscarAutorPorAnio(){
        System.out.println("Indica el año para buscar que autores estan vivos: \n");
        var anioBuscado = entrada.nextInt();
        entrada.nextLine();

        List<com.aluracursos.literalura.models.Autor> autoresVivos = autorRepository.findByCumpleaniosLessThanOrFechaFallecimientoGreaterThanEqual(anioBuscado, anioBuscado);

        if (autoresVivos.isEmpty()){
            System.out.println("No se encontraron autores que estuvieron vivos en el año: " + anioBuscado);
        }else {
            System.out.println("Los autores que estaban vivos en el año: " + anioBuscado + " son: ");
            Set<String> autoresUnicos = new HashSet<>();

            for (Autor autor : autoresVivos){
                if (autor.getCumpleanios() != null && autor.getFechaFallecimiento() != null){
                    if (autor.getCumpleanios() <= anioBuscado && autor.getFechaFallecimiento() >= anioBuscado){
                        if (autoresUnicos.add(autor.getNombre())){
                            System.out.println("Autor: " + autor.getNombre());
                        }
                    }
                }
            }
        }

    }

    private void top10LibrosMasDescargados() {
        // Llama al repositorio para obtener el top 10 de libros más descargados sin el uso de Pageable
        List<Libro> top10Libros = libroRepository.findTop10ByCantidadDescargas();

        // Verifica si hay resultados
        if (!top10Libros.isEmpty()) {
            int index = 1;
            for (Libro libro : top10Libros) {
                System.out.printf("Libro %d: %s Autor: %s Descargas: %d\n",
                        index, libro.getTitulo(), libro.getAutor() != null ? libro.getAutor().getNombre() : "Autor desconocido", libro.getCantidadDescargas());
                index++;
            }
        } else {
            System.out.println("No se encontraron libros en el top 10.");
        }
    }

    private void buscarAutorPorNombre() {
        System.out.println("Ingrese el nombre del escritor del libro: ");
        var escritor = entrada.nextLine();
        Optional<Autor> escritorBuscado = autorRepository.findFirstByNombreContainsIgnoreCase(escritor);

        if (escritorBuscado.isPresent()) {
            System.out.println("\nEl escritor buscado fue: " + escritorBuscado.get().getNombre());
        } else {
            System.out.println("\nEl escritor con el nombre: " + escritor + " no se encontró. \n");
        }
    }

}
