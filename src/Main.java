import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Main {
    enum indicesArchivoEntrada {RESULTADO, CONFIGURACION}

//    enum indicesArchivoPronosticos {PARTICIPANTE, EQUIPO_1, GANA_1, EMPATA, GANA_2, EQUIPO_2}

    enum indicesArchivoResultados {FASE, RONDA, EQUIPO_1, GOLES_EQUIPO_1, GOLES_EQUIPO_2, EQUIPO_2}

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

        int ptoGanado = 3;
        int ptoEmpatado = 1;
        int ptoPerdido = 0;
        int ptoExtra = 5;
        Configuracion configuracion = null;
        String archConfig = args[indicesArchivoEntrada.CONFIGURACION.ordinal()];
        try {
            BufferedReader br = new BufferedReader(new FileReader(archConfig));
            String linea = br.readLine();
            if (linea != null) {
                String[] datos = linea.split(";");
                if (datos.length == 4) {
                    ptoGanado = Integer.parseInt(datos[0]);
                    ptoEmpatado = Integer.parseInt(datos[1]);
                    ptoPerdido = Integer.parseInt(datos[2]);
                    ptoExtra = Integer.parseInt(datos[3]);
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            // Captura de excepción por fichero no encontrado
            System.out.println("Error: Fichero no encontrado");
            ex.printStackTrace();
        } catch(Exception ex) {
            // Captura de cualquier otra excepción
            System.out.println("Error de lectura del fichero");
            ex.printStackTrace();
        } finally {
            configuracion = new Configuracion(ptoGanado, ptoEmpatado, ptoPerdido, ptoExtra);
        }
//        Configuracion configuracion = new Configuracion(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
//        MySQL mySQL = new MySQL();
//        MySQL mySQL = new MySQL("com.mysql.jdbc.Driver", "jdbc:mysql://sql10.freemysqlhosting.net:3306/", "sql10612293",
//                "sql10612293", "ACwUKDKvbY");
        MySQL mySQL = new MySQL("jdbc:mariadb://sql10.freemysqlhosting.net:3306/", "sql10612293", "sql10612293", "ACwUKDKvbY");
//        Configuracion configuracion = new Configuracion();

        //Formato archivo resultados.csv:
        // fase1;ronda1;equipo1;golesEquipo1;golesEquipo2;equipo2
        // fase1;ronda1;equipo3;golesEquipo3;golesEquipo4;equipo4
        // fase1;ronda2;equipo5;golesEquipo5;golesEquipo6;equipo6
        // fase2;ronda1;equipo5;golesEquipo5;golesEquipo6;equipo6
        // fase2;ronda1;equipo5;golesEquipo5;golesEquipo6;equipo6
        String archivoResultados = args[indicesArchivoEntrada.RESULTADO.ordinal()]; //"resultados.csv";
        Path path = Paths.get(archivoResultados);
        List<Fase> fases = cargarArchivoResultados(path);

        int totalPartidos = 0;
        for (Fase fase : fases) {
            for (Ronda ronda : fase.getRondas()) {
                totalPartidos += ronda.getPartidos().size();
            }
        }

//        //Formato archivo pronosticos.csv:
//        // participante1;equipo1;gana1;empata;gana2;equipo2
//        // participante1;equipo3;gana3;empata;gana4;equipo4
//        // participante2;equipo5;gana5;empata;gana6;equipo6
//        String archivoPronosticos = "pronosticos.csv";
//        List<Participante> participantes = cargarArchivoPronosticos(Paths.get(archivoPronosticos));

        mySQL.openConn();
        List<Participante> participantes = cargarPronosticosParticipantes(mySQL);
        mySQL.closeConn();

//        //Uso lista auxiliar de participantes para luego mostrar los resultados por pantalla.
//        int indicePronostico;
//        List<Pronostico> pronosticos;
//        List<Ronda> rondas;
//        List<Partido> partidos;
//        for (Participante participante : participantes) {
//            indicePronostico = 0;
//            pronosticos = participante.getPronosticos();
//            for (Fase fase: fases) {
//                rondas = fase.getRondas();
//                for (Ronda ronda : rondas) {
//                    partidos = ronda.getPartidos();
//                    for (int i = 0; i < partidos.size(); i++) {
//                        Partido partido = partidos.get(i);
//                        Pronostico pronostico = pronosticos.get(indicePronostico);
//                        pronostico.setNroFase(fase.getNro());
//                        pronostico.setNroRonda(ronda.getNro());
//                        pronostico.setPartido(partido);
//                        indicePronostico++;
//                    }
//                }
//            }
//        }

        //Muestro por pantalla los resultados
        System.out.println("\n");
        int aciertosPronostico = 0;
        int puntos = 0;
        int puntosPronostico = 0;
        int puntosFase = 0;
        int puntosRonda = 0;
        double pronosticoAcertado = 0;
        List<Pronostico> pronosticos;
        List<Ronda> rondas;
        for (Participante participante : participantes) {
            aciertosPronostico = 0;
            puntosPronostico = 0;
            pronosticos = participante.getPronosticos();
            for (Pronostico pronostico : pronosticos) {
                puntos = pronostico.puntos(configuracion);
                puntosPronostico += puntos;
                if (puntos > 0) {
                    aciertosPronostico++;
                }
            }
            puntosFase = 0;
            for (Fase fase : fases) {
                List<Pronostico> pronosticosRondasFase = new ArrayList<>();
                rondas = fase.getRondas();
                puntosRonda = 0;
                for (Ronda ronda : rondas) {
                    List<Pronostico> pronosticosRonda = new ArrayList<>();
                    for (Pronostico pronostico : pronosticos) {
                        if (pronostico.getNroFase().equals(fase.getNro()) && pronostico.getNroRonda().equals(ronda.getNro())) {
                            pronosticosRonda.add(pronostico);
                            pronosticosRondasFase.add(pronostico);
                        }
                    }
                    puntosRonda += ronda.puntos(configuracion.getPtoExtra(), pronosticosRonda);
                }
                puntosFase += fase.puntos(configuracion.getPtoExtra(), pronosticosRondasFase);
            }

            pronosticoAcertado = (double)aciertosPronostico / totalPartidos * 100;
            System.out.println(participante.getNombre() + "\nPuntos pronosticos: " + puntosPronostico + "\nPronosticos acertados: " + pronosticoAcertado + "%\nPuntos rondas: " +
                    puntosRonda + "\nPuntos fase: " + puntosFase + "\nTotal puntos: " + (puntosPronostico + puntosRonda + puntosFase) + "\n---------------\n");
        }

    }

//    public static List<Participante> cargarArchivoPronosticos(Path path) {
//        try {
//            boolean inicio = true;
//            List<Participante> participantes = new ArrayList<>();
//            Participante participante = new Participante();
//            String nombreParticipanteActual = "";
//            List<Pronostico> pronosticos = new ArrayList<>();
//            for (String linea : Files.readAllLines(path)) {
//                String[] datos = linea.split(";"); //Formato: participante;equipo1;gana1;empata;gana2;equipo2 por linea
//                if (inicio) {
//                    nombreParticipanteActual = datos[indicesArchivoPronosticos.PARTICIPANTE.ordinal()];
//                    participante.setNombre(nombreParticipanteActual);
//                    inicio = false;
//                } else if (!nombreParticipanteActual.equals(datos[indicesArchivoPronosticos.PARTICIPANTE.ordinal()])) {
//                    participante.setPronosticos(pronosticos);
//                    participantes.add(participante);
//                    pronosticos = new ArrayList<>();
//                    nombreParticipanteActual = datos[indicesArchivoPronosticos.PARTICIPANTE.ordinal()];
//                    participante = new Participante(nombreParticipanteActual);
//                }
//                Partido partido = new Partido(new Equipo(datos[indicesArchivoPronosticos.EQUIPO_1.ordinal()]), 0,
//                        new Equipo(datos[indicesArchivoPronosticos.EQUIPO_2.ordinal()]), 0);
//                Equipo equipo = new Equipo(datos[indicesArchivoPronosticos.EQUIPO_1.ordinal()]);
//                ResultadoEmun resultadoEmun = new ResultadoEmun(ResultadoEmun.estados.EMPATE);
//                boolean gana1 = datos[indicesArchivoPronosticos.GANA_1.ordinal()].toLowerCase().equals("x");
//                boolean gana2 = datos[indicesArchivoPronosticos.GANA_2.ordinal()].toLowerCase().equals("x");
//                if (gana1 || gana2) {
//                    resultadoEmun.setEstado(ResultadoEmun.estados.GANADOR);
//                    if (gana2) {
//                        equipo.setNombre(datos[indicesArchivoPronosticos.EQUIPO_2.ordinal()]);
//                    }
//                }
//                Pronostico pronostico = new Pronostico(partido, equipo, resultadoEmun);
//                pronosticos.add(pronostico);
//            }
//            if (pronosticos.size() > 0) {
//                participante.setPronosticos(pronosticos);
//                participantes.add(participante);
//            }
//            return participantes;
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }

    public static List<Fase> cargarArchivoResultados(Path path) {
        try {
            boolean inicio = true;
            String nroFaseActual = "";
            String nroRondaActual = "";
            Fase Fase = new Fase();
            Ronda ronda = new Ronda();
            List<Fase> Fases = new ArrayList<>();
            List<Ronda> rondas = new ArrayList<>();
            List<Partido> partidos = new ArrayList<>();
            for (String linea : Files.readAllLines(path)) {
                String[] datos = linea.split(","); //Formato: Fase;ronda;equipo1;golesEquipo1;golesEquipo2;equipo2 por linea
                if (datos.length == 6 && isNumeric(datos[indicesArchivoResultados.GOLES_EQUIPO_1.ordinal()]) && isNumeric(datos[indicesArchivoResultados.GOLES_EQUIPO_2.ordinal()])) {
                    if (inicio) {
                        nroFaseActual = datos[indicesArchivoResultados.FASE.ordinal()];
                        Fase.setNro(nroFaseActual);
                        nroRondaActual = datos[indicesArchivoResultados.RONDA.ordinal()];
                        ronda.setNro(nroRondaActual);
                        inicio = false;
                    } else if (!nroFaseActual.equals(datos[indicesArchivoResultados.FASE.ordinal()])) {
                        Fase.setNro(nroFaseActual);
                        Fase.setRondas(rondas);
                        Fases.add(Fase);
                        nroFaseActual = datos[indicesArchivoResultados.FASE.ordinal()];
                        nroRondaActual = datos[indicesArchivoResultados.RONDA.ordinal()];
                        ronda = new Ronda(nroRondaActual);
                        partidos = new ArrayList<>();
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

                Fase.setRondas(rondas);
                Fases.add(Fase);
            }
            return Fases;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static List<Participante> cargarPronosticosParticipantes(MySQL prmMySQL) {
        List<Participante> participantes = new ArrayList<>();
        try {
            boolean inicio = true;
            Participante participante = new Participante();
            String nombreParticipanteActual = "";
            List<Pronostico> pronosticos = new ArrayList<>();
            String query = "SELECT R.FASE, R.RONDA, E1.EQUIPO AS EQUIPO_1, R.GOLES_1, R.GOLES_2, E2.EQUIPO AS EQUIPO_2, P.NOMBRE, P.GANADOR ";
            query += "FROM RESULTADOS R ";
            query += "JOIN EQUIPOS E1 on R.ID_EQUIPO_1 = E1.ID_EQUIPO ";
            query += "JOIN EQUIPOS E2 on R.ID_EQUIPO_2 = E2.ID_EQUIPO ";
            query += "JOIN PRONOSTICOS P on P.ID_RESULTADO = R.ID_RESULTADO";
            ResultSet rs = prmMySQL.execQuery(query);
            while (rs.next()) {
                if (inicio) {
                    nombreParticipanteActual = rs.getString("NOMBRE");
                    participante.setNombre(nombreParticipanteActual);
                    inicio = false;
                } else if (!nombreParticipanteActual.equals(rs.getString("NOMBRE"))) {
                    participante.setPronosticos(pronosticos);
                    participantes.add(participante);
                    pronosticos = new ArrayList<>();
                    nombreParticipanteActual = rs.getString("NOMBRE");
                    participante = new Participante(nombreParticipanteActual);
                }
                Partido partido = new Partido(new Equipo(rs.getString("EQUIPO_1")), Integer.parseInt(rs.getString("GOLES_1")),
                        new Equipo(rs.getString("EQUIPO_2")), Integer.parseInt(rs.getString("GOLES_2")));
                Equipo equipo = new Equipo(rs.getString("EQUIPO_1"));
                ResultadoEmun resultadoEmun = new ResultadoEmun(ResultadoEmun.estados.EMPATE);
                if (rs.getInt("GANADOR") > 0) {
                    resultadoEmun.setEstado(ResultadoEmun.estados.GANADOR);
                    if (rs.getInt("GANADOR") > 1) {
                        equipo.setNombre(rs.getString("EQUIPO_2"));
                    }
                }
                Pronostico pronostico = new Pronostico(rs.getString("FASE"), rs.getString("RONDA"), partido, equipo, resultadoEmun);
                pronosticos.add(pronostico);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            return participantes;
        }
    }
}