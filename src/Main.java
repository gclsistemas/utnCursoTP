import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    enum indicesArchivoPronosticos {PARTICIPANTE, EQUIPO_1, GANA_1, EMPATA, GANA_2, EQUIPO_2}

    enum indicesArchivoResultados {RONDA, EQUIPO_1, GOLES_EQUIPO_1, GOLES_EQUIPO_2, EQUIPO_2}

    private static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    public static void main(String[] args) {
//        System.out.println("Hello world!");
        //Formato archivo resultados.csv:
        // ronda1;equipo1;golesEquipo1;golesEquipo2;equipo2
        // ronda1;equipo3;golesEquipo3;golesEquipo4;equipo4
        // ronda2;equipo5;golesEquipo5;golesEquipo6;equipo6
        String archivoResultados = "resultados.csv";
        Path path = Paths.get(archivoResultados);
//        Ronda ronda = new Ronda("Ronda 1");
//        cargarArchivoResultados(path, ronda);
        List<Ronda> rondas = cargarArchivoResultados(path);

        //Formato archivo pronosticos.csv:
        // participante1;equipo1;gana1;empata;gana2;equipo2
        // participante1;equipo3;gana3;empata;gana4;equipo4
        // participante2;equipo5;gana5;empata;gana6;equipo6
        String archivoPronosticos = "pronosticos.csv";
//        List<Pronostico> pronosticos =  new ArrayList<>();
//        cargarArchivoPronosticos(Paths.get(archivoPronosticos), pronosticos);
        List<Participante> participantes = cargarArchivoPronosticos(Paths.get(archivoPronosticos));

//        if (ronda.getPartidos().size() == pronosticos.size()) {
//            List<Partido> partidos = ronda.getPartidos();
//
//            for (int i = 0; i < partidos.size(); i++) {
//                Partido partido = partidos.get(i);
//                Pronostico pronostico = pronosticos.get(i);
//                pronostico.setPartido(partido);
//                pronosticos.set(i, pronostico);
//            }
//
//            int sumarPuntos = 0;
//            for (Pronostico pronostico : pronosticos) {
//                sumarPuntos += pronostico.puntos();
//            }
//            System.out.println("Puntos: " + sumarPuntos);
//        } else {
//            System.out.println("La cantidad de resultados no coincide con las de los pronosticos.");
//        }

        //Uso lista auxiliar de participantes para luego mostrar los resultados por pantalla.
//        List<Participante> lstParticipantes = new ArrayList<>();
        for (Participante participante : participantes) {
            int indicePronostico = 0;
            List<Pronostico> pronosticos = participante.getPronosticos();
            for (Ronda ronda : rondas) {
                List<Partido> partidos = ronda.getPartidos();
                for (int i = 0; i < partidos.size(); i++) {
                    Partido partido = partidos.get(i);
                    Pronostico pronostico = pronosticos.get(indicePronostico);
                    pronostico.setPartido(partido);
//                    pronosticos.set(indicePronostico, pronostico);
                    indicePronostico++;
                }
            }
//            participante.setPronosticos(pronosticos);
//            lstParticipantes.add(participante);
        }

        //Muestro por pantalla los resultados
        for (Participante participante : participantes) {
            int puntos = 0;
            List<Pronostico> pronosticos = participante.getPronosticos();
            for (Pronostico pronostico : pronosticos) {
                puntos += pronostico.puntos();
            }
            System.out.println(participante.getNombre() + ": " + puntos + "\n");
        }

    }

//    public static void cargarArchivoPronosticos(Path path, List<Pronostico> pronosticos) {
//        try {
//            for (String linea : Files.readAllLines(path)) {
//                String[] datos = linea.split(";"); //Formato: equipo1;gana1;empata;gana2;equipo2 por linea
//                Partido partido = new Partido(new Equipo(datos[indicesArchivoPronosticos.EQUIPO_1.ordinal()]), 0,
//                        new Equipo(datos[indicesArchivoPronosticos.EQUIPO_2.ordinal()]), 0);
//                Equipo equipo = new Equipo();
//                ResultadoEmun resultadoEmun = new ResultadoEmun(ResultadoEmun.estados.EMPATE);
//                if (datos[indicesArchivoPronosticos.GANA_1.ordinal()].equals("x") || datos[indicesArchivoPronosticos.GANA_2.ordinal()].equals("x")) {
//                    resultadoEmun.setEstado(ResultadoEmun.estados.GANADOR);
//                    if (datos[indicesArchivoPronosticos.GANA_1.ordinal()].equals("x")) {
//                        equipo.setNombre(datos[0]);
//                    } else if (datos[indicesArchivoPronosticos.GANA_2.ordinal()].equals("x")) {
//                        equipo.setNombre(datos[indicesArchivoPronosticos.EQUIPO_2.ordinal()]);
//                    }
//                }
//                Pronostico pronostico = new Pronostico(partido, equipo, resultadoEmun);
//                pronosticos.add(pronostico);
//            }
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
    public static List<Participante> cargarArchivoPronosticos(Path path) {
        try {
            boolean inicio = true;
            List<Participante> participantes = new ArrayList<>();
            Participante participante = new Participante();
            String nombreParticipanteActual = "";
            List<Pronostico> pronosticos = new ArrayList<>();
            for (String linea : Files.readAllLines(path)) {
                String[] datos = linea.split(";"); //Formato: participante;equipo1;gana1;empata;gana2;equipo2 por linea
                if (inicio) {
                    nombreParticipanteActual = datos[indicesArchivoPronosticos.PARTICIPANTE.ordinal()];
                    participante.setNombre(nombreParticipanteActual);
                    inicio = false;
                } else if (!nombreParticipanteActual.equals(datos[indicesArchivoPronosticos.PARTICIPANTE.ordinal()])) {
                    participante.setPronosticos(pronosticos);
                    participantes.add(participante);
                    pronosticos = new ArrayList<>();
                    nombreParticipanteActual = datos[indicesArchivoPronosticos.PARTICIPANTE.ordinal()];
                    participante = new Participante(nombreParticipanteActual);
                }
                Partido partido = new Partido(new Equipo(datos[indicesArchivoPronosticos.EQUIPO_1.ordinal()]), 0,
                        new Equipo(datos[indicesArchivoPronosticos.EQUIPO_2.ordinal()]), 0);
                Equipo equipo = new Equipo(datos[indicesArchivoPronosticos.EQUIPO_1.ordinal()]);
                ResultadoEmun resultadoEmun = new ResultadoEmun(ResultadoEmun.estados.EMPATE);
                boolean gana1 = datos[indicesArchivoPronosticos.GANA_1.ordinal()].toLowerCase().equals("x");
                boolean gana2 = datos[indicesArchivoPronosticos.GANA_2.ordinal()].toLowerCase().equals("x");
                if (gana1 || gana2) {
                    resultadoEmun.setEstado(ResultadoEmun.estados.GANADOR);
                    if (gana2) {
                        equipo.setNombre(datos[indicesArchivoPronosticos.EQUIPO_2.ordinal()]);
                    }
                }
                Pronostico pronostico = new Pronostico(partido, equipo, resultadoEmun);
                pronosticos.add(pronostico);
            }
            if (pronosticos.size() > 0) {
                participante.setPronosticos(pronosticos);
                participantes.add(participante);
            }
            return participantes;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

//    public static void cargarArchivoResultados(Path path, Ronda ronda) {
//        try {
//            List<Partido> partidos = new ArrayList<>();
//            for (String linea : Files.readAllLines(path)) {
//                String[] datos = linea.split(";"); //Formato: equipo1;golesEquipo1;golesEquipo2;equipo2 por linea
//                Partido partido = new Partido(new Equipo(datos[indicesArchivoResultados.EQUIPO_1.ordinal()]),
//                        Integer.parseInt(datos[indicesArchivoResultados.GOLES_EQUIPO_1.ordinal()]),
//                        new Equipo(datos[indicesArchivoResultados.EQUIPO_2.ordinal()]),
//                        Integer.parseInt(datos[indicesArchivoResultados.GOLES_EQUIPO_2.ordinal()]));
//                partidos.add(partido);
//            }
//            ronda.setPartidos(partidos);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
    public static List<Ronda> cargarArchivoResultados(Path path) {
    try {
        boolean inicio = true;
        String nroRondaActual = "";
        Ronda ronda = new Ronda();
        List<Ronda> rondas = new ArrayList<>();
        List<Partido> partidos = new ArrayList<>();
        for (String linea : Files.readAllLines(path)) {
            String[] datos = linea.split(";"); //Formato: ronda;equipo1;golesEquipo1;golesEquipo2;equipo2 por linea
            if (datos.length == 5 && isNumeric(datos[indicesArchivoResultados.GOLES_EQUIPO_1.ordinal()]) && isNumeric(datos[indicesArchivoResultados.GOLES_EQUIPO_2.ordinal()])) {
                if (inicio) {
                    nroRondaActual = datos[indicesArchivoResultados.RONDA.ordinal()];
                    ronda.setNro(nroRondaActual);
                    inicio = false;
                } else if (!nroRondaActual.equals(datos[indicesArchivoResultados.RONDA.ordinal()])) {
                    ronda.setPartidos(partidos);
                    rondas.add(ronda);
                    nroRondaActual = datos[indicesArchivoResultados.RONDA.ordinal()];
                    ronda = new Ronda(nroRondaActual);
                    partidos = new ArrayList<>();
                }
                Partido partido = new Partido(new Equipo(datos[indicesArchivoResultados.EQUIPO_1.ordinal()]),
                        Integer.parseInt(datos[indicesArchivoResultados.GOLES_EQUIPO_1.ordinal()]),
                        new Equipo(datos[indicesArchivoResultados.EQUIPO_2.ordinal()]),
                        Integer.parseInt(datos[indicesArchivoResultados.GOLES_EQUIPO_2.ordinal()]));
                partidos.add(partido);
            }
        }
        if (partidos.size() > 0) {
            ronda.setPartidos(partidos);
            rondas.add(ronda);
        }
        return rondas;
    } catch (IOException e) {
        System.out.println(e.getMessage());
        throw new RuntimeException(e);
    }
}
}