package com.alura.jdbc.DAO;

import com.alura.jdbc.com.alura.jdbc.fantory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class productoDAO {

    final private Connection con;

    public productoDAO(Connection con) {
        this.con = con;
    }

    public int eliminar(Integer id) {
        try {
            final PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");

            try (statement) {
                statement.setInt(1, id);
                statement.execute();

                int updateCount = statement.getUpdateCount();

                return updateCount;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) {
        try {
            final PreparedStatement statement = con.prepareStatement(
                    "UPDATE PRODUCTO SET "
                            + " NOMBRE = ?, "
                            + " DESCRIPCION = ?,"
                            + " CANTIDAD = ?"
                            + " WHERE ID = ?");

            try (statement) {
                statement.setString(1, nombre);
                statement.setString(2, descripcion);
                statement.setInt(3, cantidad);
                statement.setInt(4, id);
                statement.execute();

                int updateCount = statement.getUpdateCount();

                return updateCount;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Producto> listar() {
        List<Producto> resultado = new ArrayList<>();
        try{
            final PreparedStatement statement = con.prepareStatement("SELECT id, nombre, descripcion, cantidad FROM PRODUCTO");

            try (statement) {
                statement.execute();

                final ResultSet resultSet = statement.getResultSet();
                try (resultSet) {
                    while (resultSet.next()) {
                        resultado.add(new Producto(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getInt(4)));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultado;
    }

    public void guardar(Producto producto) {
        Integer cantidad = producto.getCantidad();
        try (con) {
            // Preparar la declaración SQL para insertar registros en la tabla "producto"
            final PreparedStatement statement = con.prepareStatement(
                    "INSERT INTO producto(nombre, descripcion, cantidad, categoria_id) values (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            try (statement) {
                    // Ejecutar el registro
                    ejecutaRegistro(statement, producto);
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void ejecutaRegistro(PreparedStatement statement, Producto producto) throws SQLException {
        // Establecer los valores de nombre, descripción y cantidad en la declaración SQL
        statement.setString(1, producto.getNombre());
        statement.setString(2, producto.getDescripcion());
        statement.setInt(3, producto.getCantidad());
        statement.setInt(4, producto.getCategoriaId());

        // Ejecutar la inserción en la base de datos
        statement.execute();

        // Obtener las claves generadas (en este caso, el ID del producto insertado)
        final ResultSet result = statement.getGeneratedKeys();
        try (result) {
            while (result.next()) {
                //Asignando el ID generado al producto_
                producto.setId(result.getInt(1));
                result.getInt(1); // No es necesario, pero se incluye por referencia.
            }
        }
    }

    public List<Producto> listar(Integer categoriaId) {
        List<Producto> resultado = new ArrayList<>();
        try{
            final PreparedStatement statement = con.prepareStatement(
                    "SELECT id, nombre, descripcion, cantidad FROM PRODUCTO " +
                            "WHERE categoria_id = ?");

            try (statement) {
                statement.setInt(1, categoriaId);
                statement.execute();

                final ResultSet resultSet = statement.getResultSet();
                try (resultSet) {
                    while (resultSet.next()) {
                        resultado.add(new Producto(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getInt(4)));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultado;
    }
}
