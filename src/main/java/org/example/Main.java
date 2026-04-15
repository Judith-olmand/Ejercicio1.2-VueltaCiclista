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
            conn.setAutoCommit(false);
            try {
                InsertarEtapa.NuevoEtapa(sc,conn);
                System.out.println("Todo correcto. Haciendo commit.");
                conn.commit();
            } catch (Exception e){
                System.out.println("“Etapa cancelada por error. No se guardaron los datos.");
                conn.rollback();
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }
}