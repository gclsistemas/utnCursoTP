import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    enum indicesArchivoPronosticos {EQUIPO_1, GANA_1, EMPATA, GANA_2, EQUIPO_2}

    enum indicesArchivoResultados {EQUIPO_1, GOLES_EQUIPO_1, GOLES_EQUIPO_2, EQUIPO_2}

    public static void main(String[] args) {
//        System.out.println("Hello world!");
        //Formato archivo resultados.csv:
        // equipo1;golesEquipo1;golesEquipo2;equipo2
        // equipo3;golesEquipo3;golesEquipo4;equipo4
        // equipo5;golesEquipo5;golesEquipo6;equipo6
        String archivoResultados = "resultados.csv";
        Path path = Paths.get(archivoResultados);
        Ronda ronda = new Ronda("Ronda 1");
        cargarArchivoResultados(path, ronda);

        //Formato archivo pronosticos.csv:
        // equipo1;gana1;empata;gana2;equipo2
        // equipo3;gana3;empata;gana4;equipo4
        // equipo5;gana5;empata;gana6;equipo6
        String archivoPronosticos = "pronosticos.csv";
        List<Pronostico> pronosticos =  new ArrayList<>();
        cargarArchivoPronosticos(Paths.get(archivoPronosticos), pronosticos);

        if (ronda.getPartidos().size() == pronosticos.size()) {
            List<Partido> partidos = ronda.getPartidos();

            for (int i = 0; i < partidos.size(); i++) {
                Partido partido = partidos.get(i);
                Pronostico pronostico = pronosticos.get(i);
                pronostico.setPartido(partido);
                pronosticos.set(i, pronostico);
            }

            int sumarPuntos = 0;
            for (Pronostico pronostico : pronosticos) {
                sumarPuntos += pronostico.puntos();
            }
            System.out.println("Puntos: " + sumarPuntos);
        } else {
            System.out.println("La cantidad de resultados no coincide con las de los pronosticos.");
        }
    }

    public static void cargarArchivoPronosticos(Path path, List<Pronostico> pronosticos) {
        try {
            for (String linea : Files.readAllLines(path)) {
                String[] datos = linea.split(";"); //Formato: equipo1;gana1;empata;gana2;equipo2 por linea
                Partido partido = new Partido(new Equipo(datos[indicesArchivoPronosticos.EQUIPO_1.ordinal()]), 0,
                        new Equipo(datos[indicesArchivoPronosticos.EQUIPO_2.ordinal()]), 0);
                Equipo equipo = new Equipo();
                ResultadoEmun resultadoEmun = new ResultadoEmun(ResultadoEmun.estados.EMPATE);
                if (datos[indicesArchivoPronosticos.GANA_1.ordinal()].equals("x") || datos[indicesArchivoPronosticos.GANA_2.ordinal()].equals("x")) {
                    resultadoEmun.setEstado(ResultadoEmun.estados.GANADOR);
                    if (datos[indicesArchivoPronosticos.GANA_1.ordinal()].equals("x")) {
                        equipo.setNombre(datos[0]);
                    } else if (datos[indicesArchivoPronosticos.GANA_2.ordinal()].equals("x")) {
                        equipo.setNombre(datos[indicesArchivoPronosticos.EQUIPO_2.ordinal()]);
                    }
                }
                Pronostico pronostico = new Pronostico(partido, equipo, resultadoEmun);
                pronosticos.add(pronostico);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void cargarArchivoResultados(Path path, Ronda ronda) {
        try {
            List<Partido> partidos = new ArrayList<>();
            for (String linea : Files.readAllLines(path)) {
                String[] datos = linea.split(";"); //Formato: equipo1;golesEquipo1;golesEquipo2;equipo2 por linea
                Partido partido = new Partido(new Equipo(datos[indicesArchivoResultados.EQUIPO_1.ordinal()]),
                        Integer.parseInt(datos[indicesArchivoResultados.GOLES_EQUIPO_1.ordinal()]),
                        new Equipo(datos[indicesArchivoResultados.EQUIPO_2.ordinal()]),
                        Integer.parseInt(datos[indicesArchivoResultados.GOLES_EQUIPO_2.ordinal()]));
                partidos.add(partido);
            }
            ronda.setPartidos(partidos);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}