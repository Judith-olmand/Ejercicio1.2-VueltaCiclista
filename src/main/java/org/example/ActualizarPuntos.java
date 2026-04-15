package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ActualizarPuntos {
    public static void tablaPuntos(Connection conexion){
        try (Statement st2 = conexion.createStatement()){
            String updatePuntos = "UPDATE PARTICIPACION " +
                    "SET puntos = CASE posicion " +
                    "    WHEN 1 THEN 100 " +
                    "    WHEN 2 THEN 90 " +
                    "    WHEN 3 THEN 80 " +
                    "    WHEN 4 THEN 70 " +
                    "    WHEN 5 THEN 60 " +
                    "    ELSE 0 " +
                    "END";
            st2.executeUpdate(updatePuntos);
            System.out.println("Puntos actualizados");
        } catch (SQLException e){
            System.out.println("Error al actualizar puntos" + e.getMessage());
        }
    }
}
