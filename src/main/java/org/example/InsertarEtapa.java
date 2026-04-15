package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class InsertarEtapa {
    public static void NuevoEtapa(Scanner sc, Connection conexion){
        boolean existeEtapa;
        int etapa;
        do {
            System.out.println("Introduce el número de etapa.");
            etapa = sc.nextInt();
            sc.nextLine();
            existeEtapa = Comprobador.numeroEtapa(conexion, etapa);
        }while (existeEtapa);


            System.out.println("Introduce el origen.");
            String origen = sc.next();
            System.out.println("Introduce el destino.");
            String destino = sc.next();
            System.out.println("Introduce la distancia en km.");
            double distancia = sc.nextDouble();
            sc.nextLine();
            System.out.println("Introduce la fecha (YYYY-MM-DD).");
            String fecha = sc.next();
            LocalDate fechaSql = LocalDate.parse(fecha);

            try {
                String inert = "INSERT INTO ETAPA (NUMERO, ORIGEN," +
                        " DESTINO, DISTANCIA_KM, FECHA) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conexion.prepareStatement(inert);
                ps.setInt(1, etapa);
                ps.setString(2, origen);
                ps.setString(3, destino);
                ps.setDouble(4, distancia);
                ps.setObject(5, fechaSql);
                ps.executeUpdate();
                System.out.println("Etapa añadida correctamente.");

            } catch (SQLException e){
                System.out.println("Error al insertar etapa.");
            }

            InsertarParticipacion.nuevaParticipacion(conexion, etapa);
            MostrarResumen.mostrarResumen(conexion,etapa);

    }
}
