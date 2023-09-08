package com.alura.jdbc.DAO;

import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private Connection con;
    public CategoriaDAO(Connection con) {
        this.con = con;
    }

    public List<Categoria> listar() {
        List<Categoria> resultado = new ArrayList<>();

        try {
            final PreparedStatement statement = con.prepareStatement(
                    "SELECT id, nombre FROM categoria");
            try(statement) {
                final ResultSet resultSet = statement.executeQuery();
                try (resultSet) {
                    while (resultSet.next()) {
                        var categoria = new Categoria(resultSet.getInt(1),
                                resultSet.getString(2));
                        resultado.add(categoria);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultado;
    }

    public List<Categoria> listarConProductos() {
        // Crear una lista para almacenar las categorías con sus productos
        List<Categoria> resultado = new ArrayList<>();

        try {
            // Preparar una declaración SQL que selecciona categorías y productos relacionados
            final PreparedStatement statement = con.prepareStatement(
                    "SELECT C.id, C.nombre, P.id, P.nombre, P.cantidad " +
                            "FROM categoria C " +
                            "INNER JOIN producto P ON C.id = P.categoria_id");

            try(statement) {
                // Ejecutar la consulta y almacenar el resultado en un ResultSet
                final ResultSet resultSet = statement.executeQuery();

                try (resultSet) {
                    // Recorrer cada fila del resultado
                    while (resultSet.next()) {
                        // Extraer el ID y nombre de la categoría desde el resultado
                        Integer categoriaId = resultSet.getInt("C.id");
                        String categoriaNombre = resultSet.getString("C.nombre");

                        // Utilizar una expresión lambda y el método filter para verificar si la categoría ya existe
                        // Si no existe, crear una nueva instancia de Categoria y agregarla a la lista resultado
                        var categoria = resultado.stream().filter(cat -> cat.getId().equals(categoriaId)).findAny().orElseGet(() ->{
                            Categoria cat = new Categoria(categoriaId, categoriaNombre);
                            resultado.add(cat);
                            return cat;
                        });
                        Producto producto = new Producto(resultSet.getInt("P.id"),
                                resultSet.getString("P.nombre"),
                                resultSet.getInt("P.cantidad"));

                        categoria.add(producto);
                    }
                }
            }
        } catch (SQLException e) {
            // Capturar excepciones SQL y lanzar una excepción de RuntimeException con la causa original
            throw new RuntimeException(e);
        }

        // Devolver la lista resultado que contiene categorías y productos relacionados
        return resultado;
    }

}
