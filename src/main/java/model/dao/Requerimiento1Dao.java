package model.dao;

//Estructura de datos
import java.util.ArrayList;

//Librerías para SQL y Base de Datos
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//Clase para conexión
import util.JDBCUtilities;

//Encapsulamiento de los datos
import model.vo.Requerimiento1;

public class Requerimiento1Dao {

    public ArrayList<Requerimiento1> rankingProyectosComprasDescendente10() throws SQLException {

        ArrayList<Requerimiento1> respuesta = new ArrayList<Requerimiento1>();
        Connection conexion = JDBCUtilities.getConnection();

        try {

            String consulta = "SELECT p.ID_Proyecto, p.Clasificacion, SUM(c.Cantidad*m.Precio_Unidad)  as Gasto_Compras, p.Serial\n"
                    + "FROM Proyecto p\n"
                    + "INNER JOIN Compra c on p.ID_Proyecto = c.ID_Proyecto \n"
                    + "INNER JOIN MaterialConstruccion m on c.ID_MaterialConstruccion = m.ID_MaterialConstruccion\n"
                    + "GROUP BY p.ID_Proyecto \n"
                    + "ORDER BY Gasto_Compras\n"
                    + "DESC\n"
                    + "LIMIT 15;";

            PreparedStatement statement = conexion.prepareStatement(consulta);
            ResultSet resultSet = statement.executeQuery();

            //Recorrer los registros en los VO específicos
            while (resultSet.next()) {
                Requerimiento1 proyectoRankeadoCompras = new Requerimiento1();
                proyectoRankeadoCompras.setIdProyecto(resultSet.getInt("ID_Proyecto"));
                proyectoRankeadoCompras.setClasificacion(resultSet.getString("Clasificacion"));
                proyectoRankeadoCompras.setGastoCompras(resultSet.getDouble("Gasto_Compras"));
                proyectoRankeadoCompras.setSerial(resultSet.getString("Serial"));

                //Se agrega cada registro como un objeto del ArrayList que contiene la consulta
                respuesta.add(proyectoRankeadoCompras);
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            System.err.println("Error consultando ranking de los 10 proyectos con mayor gasto por compras: " + e);
        } finally {
            if (conexion != null) {
                conexion.close();
            }
        }

        //Retornar la colección de vo's
        return respuesta;

    }

}
