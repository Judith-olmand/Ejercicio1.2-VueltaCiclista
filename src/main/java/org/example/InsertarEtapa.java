package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class InsertarEtapa {
    public static void NuevoEtapa(Scanner sc, Connection conexion){
        boolean existeEtapa;
        int etapa;
        /**
         * Pide el número de etapa y llama al Comprobador pasando por parámetro
         * la conexión y en número introducido
         * si existe vuelve a pedir un número
         */
        do {
            System.out.println("Introduce el número de etapa.");
            etapa = sc.nextInt();
            sc.nextLine();
            existeEtapa = Comprobador.numeroEtapa(conexion, etapa);
        }while (existeEtapa);


        /**
         * Pido el resto de datos
         */
        System.out.println("Introduce el origen.");
        String origen = sc.next();
        System.out.println("Introduce el destino.");
        String destino = sc.next();
        System.out.println("Introduce la distancia en km.");
        double distancia = sc.nextDouble();
        sc.nextLine();
        /**
         * Recojo la fecha como DD/MM/YYYY
         * y la paso a YYYY/MM/DDD que es como lo almacena LocalDate
         * para poder usarlo para sql
         */
        System.out.println("Introduce la fecha (dd/MM/yyyy).");
        String fecha = sc.next();
        LocalDate fechaSql = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        /**
         * Realizo el insert mediante PreparedStatement sustituyendo las ?
         * por las variables con los datos anteriores
         */
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
        /**
         * Si hay algún error al insertar lanza este error
         */
        } catch (SQLException e){
            System.out.println("Error al insertar etapa.");
        }

        /**
         * Si todo sale bien llama a insertar participación
         * pasando por parámetro la conexión y el número de etapa
         */
        InsertarParticipacion.nuevaParticipacion(conexion, etapa);
        /**
         * Si insertar participación no da error, una vez completado
         * llama a mostrar resumen pasando por parámetro la conexión
         * y el número de etapa
         */
        MostrarResumen.mostrarResumen(conexion,etapa);

    }
}
