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

        Set<Integer> noRepetir =  new HashSet<>();
        int posicion;

        try (Statement st = conexion.createStatement()){
            String consultaIdCiclista = "SELECT ID_CICLISTA FROM CICLISTA";
            ResultSet rs = st.executeQuery(consultaIdCiclista);
            while (rs.next()){
                int idCiclista = rs.getInt("ID_CICLISTA");
                Random random = new Random();
                do {
                    posicion = random.nextInt(numeroMaximo)+1;
                    //System.out.println(posicion);

                }while (noRepetir.contains(posicion));
                noRepetir.add(posicion);
                if (posicion <= 5){

                    int puntos;
                    switch (posicion) {
                        case 1:
                            puntos = 100;
                            break;
                        case 2:
                            puntos = 90;
                            break;
                        case 3:
                            puntos = 80;
                            break;
                        case 4:
                            puntos = 70;
                            break;
                        case 5:
                            puntos = 60;
                            break;
                        default:
                            puntos = 0;
                        break;
                    }

                    String insert = "INSERT INTO PARTICIPACION (ID_CICLISTA, " +
                            "NUMERO_ETAPA, POSICION, PUNTOS) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement ps = conexion.prepareStatement(insert)){
                        ps.setInt(1, idCiclista);
                        ps.setInt(2, etapa);
                        ps.setInt(3, posicion);
                        ps.setInt(4, puntos);
                        ps.executeUpdate();

                    }catch (SQLException e){
                        System.out.println("Error la insertar participacion");
                    }
                }
            }
            rs.close();
            System.out.println("Participaciones añadidas");
//            try (Statement st2 = conexion.createStatement()){
//                String updatePuntos = "UPDATE PARTICIPACION " +
//                        "SET puntos = CASE posicion " +
//                        "    WHEN 1 THEN 100 " +
//                        "    WHEN 2 THEN 90 " +
//                        "    WHEN 3 THEN 80 " +
//                        "    WHEN 4 THEN 70 " +
//                        "    WHEN 5 THEN 60 " +
//                        "    ELSE 0 " +
//                        "END";
//                st2.executeUpdate(updatePuntos);
//                System.out.println("Puntos actualizados");
//            } catch (SQLException e){
//                System.out.println("Error al actualizar puntos" + e.getMessage());
//            }
        }catch (SQLException e){
            System.out.println("Error al obtener el id del ciclista: ");
        }
    }
}
