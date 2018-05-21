
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author renan.rosa
 */
public class Conexao {

    public Connection conectar() {
 
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC nao encontrado");
            e.printStackTrace();
            return null;
        }
        System.out.println("PostgreSQL JDBC Driver Registrado!");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://10.102.0.238:5432/sabium", "renan.rosa",
                    "@kindness2018");
        } catch (SQLException e) {
            System.out.println("Falha na conexão, verifique o log");
            e.printStackTrace();
            return null;
        }
        if (connection != null) {
            System.out.println("Conexão estabelecida com a base de dados");
        } else {
            System.out.println("Houve um problema na conexao com a base de dados, verifique");
        }
        return connection;
    }
}
