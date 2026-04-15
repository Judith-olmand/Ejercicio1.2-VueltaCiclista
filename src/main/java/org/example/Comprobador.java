package org.example;

import java.sql.*;

public class Comprobador {
    public static boolean numeroEtapa(Connection conexion, int numero){
        String comprobarNum = "SELECT NUMERO FROM ETAPA WHERE NUMERO = ?";
        try (PreparedStatement ps = conexion.prepareStatement(comprobarNum)) {
            ps.setInt(1, numero);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("El número de etapa ya existe");
                return true;
            }else {
                System.out.println("Número de etapa disponible");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener el numero de etapa: ");
            return  false;
        }
    }

//    public static boolean participacionCiclistaEtapa(Connection conexion, int numeroEtapa, int IDCiclista ){
//        String comprobarID = "SELECT ID_CICLISTA " +
//                "FROM PARTICIPACION " +
//                "WHERE NUMERO_ETAPA = ? AND ID_CICLISTA = ?";
//        try (PreparedStatement ps = conexion.prepareStatement(comprobarID)) {
//            ps.setInt(1, numeroEtapa);
//            ps.setInt(2, IDCiclista);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return  true;
//            } else {
//                return  false;
//            }
//        } catch (SQLException e){
//            System.out.println("Error al obtener el id del ciclista: ");
//            return  false;
//        }
//    }


    public static int numeroCiclistas(Connection conexion){
        try {
            Statement st = conexion.createStatement();
            String consultaNumCiclistas = "SELECT COUNT(*) AS NUM_CICLISTAS FROM CICLISTA";
            ResultSet rs = st.executeQuery(consultaNumCiclistas);
            rs.next();
            return rs.getInt(1);
        }catch (SQLException e){
            System.out.println("Error al obtener el id maximo de los ciclistas: ");
            return 0;
        }
    }
}
