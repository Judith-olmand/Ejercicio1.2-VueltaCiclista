package org.example;

import oracle.net.jdbc.TNSAddress.SOException;
import oracle.sql.TRANSDUMP;

import java.sql.*;
import java.time.LocalDate;

public class MostrarResumen {
    public static void mostrarResumen(Connection conexion, int etapa) {
        try {
            String consultaEtapaInsert = "SELECT * FROM ETAPA WHERE NUMERO = ? ";
            PreparedStatement ps = conexion.prepareStatement(consultaEtapaInsert);
            ps.setInt(1, etapa);
            ResultSet rs = ps.executeQuery();
            rs.next();
            LocalDate fecha = rs.getDate(5).toLocalDate();
            System.out.println("ETAPA AÑADIDA: ");
            System.out.printf("%-18s %-25s %-25s %-12s %-12s%n",
                    "Número de etapa","Origen","Destino","Km", "Fecha");
            System.out.println("-".repeat(95));
            System.out.printf("%-18s %-25s %-25s %-18s %-10s%n",
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4),
                    fecha);
            String consultaPuntos = "SELECT * FROM PARTICIPACION WHERE POSICION BETWEEN 1 AND 5 AND NUMERO_ETAPA = ?" ;
            PreparedStatement ps2 = conexion.prepareStatement(consultaPuntos);
            ps2.setInt(1, etapa);
            ResultSet rsPuntos = ps2.executeQuery();
            System.out.println();
            System.out.println("-".repeat(80));
            System.out.printf("%-18s %-18s %-11s %-10s%n",
                    "ID del Ciclista", "Número de etapa", "Posición" , "Puntos");
            System.out.println("-".repeat(75));
            while (rsPuntos.next()) {

                int idCiclista = rsPuntos.getInt(1);
                int numEtapa = rsPuntos.getInt(2);
                int posicion = rsPuntos.getInt(3);
                int puntos = rsPuntos.getInt(4);
                System.out.printf("%-18d %-18d %-11d %-10d%n",
                        idCiclista, numEtapa, posicion, puntos);
//                System.out.println(idCiclista + " " + numEtapa + " " + posicion + " " + puntos);
//                System.out.println("--------------");
//                System.out.println(rsPuntos.getInt(1)
//                + " " + rsPuntos.getInt(2)
//                + " " + rsPuntos.getInt(3)
//                + " " + rsPuntos.getInt(4));
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }
}
