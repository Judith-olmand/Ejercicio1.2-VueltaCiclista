package org.example;

import java.sql.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class InsertarParticipacion {
    public static void nuevaParticipacion(Connection conexion, int etapa){
        int numeroMaximo;
        do {
            numeroMaximo = Comprobador.numeroCiclistas(conexion);
        }while (numeroMaximo == 0);

        Set<Integer> noRepatir =  new HashSet<>();
        int posicion;

        try (Statement st = conexion.createStatement()){
            String consultaIdCiclista = "SELECT ID_CICLISTA FROM CICLISTA";
            ResultSet rs = st.executeQuery(consultaIdCiclista);
            while (rs.next()){
                int idCiclista = rs.getInt("ID_CICLISTA");
                Random random = new Random();
                do {
                    posicion = random.nextInt(numeroMaximo)+1;
                    System.out.println(posicion);

                }while (noRepatir.contains(posicion));
                noRepatir.add(posicion);
                if (posicion <= 5){
                    try {
                        String insert = "INSERT INTO PARTICIPACION (ID_CICLISTA, " +
                                "NUMERO_ETAPA, POSICION, PUNTOS) VALUES (?, ?, ?, ?)";
                        PreparedStatement ps = conexion.prepareStatement(insert);
                        ps.setInt(1, idCiclista);
                        ps.setInt(2, etapa);
                        ps.setInt(3, posicion);
                        ps.setInt(4, 0);
                        ps.executeUpdate();

                    }catch (SQLException e){
                        System.out.println("Error la insertar participacion");
                    }
                }
            }
            System.out.println("Participaciones añadidas");
            try {
                String updatePuntos = "UPDATE PARTICIPACION\n" +
                        "SET puntos = CASE posicion\n" +
                        "    WHEN 1 THEN 100\n" +
                        "    WHEN 2 THEN 90\n" +
                        "    WHEN 3 THEN 80\n" +
                        "    WHEN 4 THEN 70\n" +
                        "    WHEN 5 THEN 60\n" +
                        "    ELSE 0\n" +
                        "END";
                ResultSet rs2 =st.executeQuery(updatePuntos);
                rs2.next();
                System.out.println("Puntos actualizados");
            } catch (SQLException e){
                System.out.println("Error al actualizar puntos");
            }
        }catch (SQLException e){
            System.out.println("Error al obtener el id del ciclista: ");
        }
    }
}
