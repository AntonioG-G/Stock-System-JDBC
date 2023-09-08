package com.alura.jdbc.com.alura.jdbc.fantory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Clase que proporciona conexiones a la base de datos utilizando un pool de conexiones.
public class ConnectionFactory {

    // Almacena el DataSource que gestiona las conexiones.
    private DataSource dataSource;

    // Constructor de la clase ConnectionFactory.
    public ConnectionFactory() {
        // Crear una instancia de ComboPooledDataSource, que es un pool de conexiones.
        var pooledDataSource = new ComboPooledDataSource();

        // Configurar la URL de la base de datos, el usuario y la contraseña.
        pooledDataSource.setJdbcUrl("jdbc:mysql://localhost/control_de_stock?useTimeZone=true&serverTimeZone=UTC");
        pooledDataSource.setUser("root");
        pooledDataSource.setPassword("reips2109");
        pooledDataSource.setMaxPoolSize(10); //tamaño de la pool

        // Asignar el DataSource configurado a la variable de instancia dataSource.
        this.dataSource = pooledDataSource;
    }

    // Método para recuperar una conexión de la base de datos.
    public Connection recuperaConexion() {
        // Devolver una conexión obtenida del DataSource.
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}