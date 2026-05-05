package org.example;

import java.sql.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class InsertarParticipacion {
    public static void nuevaParticipacion(Connection conexion, int etapa){
        int numeroMaximo;
        /**
         * Bucle que llama al comprobador de numeros de ciclista
         * que se repite mientras sea 0, pues 0 solo va a ser si ocurre alún error en la consulta
         */
        do {
            numeroMaximo = Comprobador.numeroCiclistas(conexion);
        }while (numeroMaximo == 0);

        /**
         * Set de numeros enteros para no repetir las posiciones
         */
        Set<Integer> noRepetir =  new HashSet<>();
        int posicion;

        try (Statement st = conexion.createStatement()){
            /**
             * Consulta que devuelve los id de los ciclista
             */
            String consultaIdCiclista = "SELECT ID_CICLISTA FROM CICLISTA";
            ResultSet rs = st.executeQuery(consultaIdCiclista);
            /**
             * Los recorro mediante while, que se ejecuta mientras haya contenido
             */
            while (rs.next()){
                /**
                 * Almaceno 1 id en la variable
                 */
                int idCiclista = rs.getInt("ID_CICLISTA");
                Random random = new Random();
                /**
                 * Genero un número aleatorio que va de 0 al número máximo
                 * y suma 1 al número que de, pues la posición 0 no existe
                 */
                do {
                    posicion = random.nextInt(numeroMaximo)+1;
                    //System.out.println(posicion);

                /**
                 * Si el número generado ya existe en el Set anterior noRepetir
                 * vuelve a repetir el bucle
                 */
                }while (noRepetir.contains(posicion));
                /**
                 * si no existe guargo el valor de posición en el Set
                 */
                noRepetir.add(posicion);
                /**
                 * Dependiendo de la posición establezco unos puntos
                 */
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

                    /**
                     * Realizo el insert con todos los datos para cada uno de los
                     * ciclistas que estén entre la posición 1 y 5
                     */
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
            /**
             * Cierro ResultSet
             */
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
        /**
         * Si hay error al obtener el id de los ciclistas lanza esta excepción
         */
        }catch (SQLException e){
            System.out.println("Error al obtener el id del ciclista: ");
        }
    }
}
