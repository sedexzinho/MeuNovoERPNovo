package org.example.conectores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static final String URL = "jdbc:postgresql://localhost:5432/MeuNovoERP";
    private static final String USER = "postgres";
    private static final String PASSWORD = "4546";

   public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void inicializarBanco() throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS produto (
                codigo VARCHAR(50) PRIMARY KEY,
                nome VARCHAR(100) UNIQUE NOT NULL,
                precoCusto NUMERIC(10,2) NOT NULL,
                precoVenda NUMERIC(10,2) NOT NULL,
                quantidadeEstoque INT NOT NULL
            )
        """;
        try (Connection conn = getConnection();
             var stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Tabela 'produto' pronta!");
        }
    }
}
