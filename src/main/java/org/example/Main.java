package org.example;

import oracle.net.jdbc.TNSAddress.SOException;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(
                DBConfig.getUrl(),
                DBConfig.getUser(),
                DBConfig.getPassword())) {
            /**
             * Pone a false el autoCommit
             */
            conn.setAutoCommit(false);
            /**
             * Llama a insertar etapa y pasa por parámetro
             * el Scanner y la conexión
             * si todo sale bien realiza un commit
             */
            try {
                InsertarEtapa.NuevoEtapa(sc,conn);
                System.out.println("Todo correcto. Haciendo commit.");
                conn.commit();
            /**
             * Si ocurre algún error en la inserción lanza esta excepción
             * y realiza un rollback
             */
            } catch (Exception e){
                System.out.println("Etapa cancelada por error. No se guardaron los datos.");
                conn.rollback();
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }
}