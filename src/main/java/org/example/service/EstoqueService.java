package org.example.service;

import org.example.Exception.NomeJaCadastrado;
import org.example.Exception.ProdutoJaCadastradoException;
import org.example.conectores.ConexaoBD;
import org.example.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstoqueService {

    private static List<Produto> produtos;

    public EstoqueService() {
        this.produtos = new ArrayList<>();
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public boolean verificaListaVazia() {
        if (produtos.isEmpty()) {
            return true;
        }
        return false;
    }

    private Produto mapResultSetToProduto(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getString("codigo"),
                rs.getString("nome"),
                rs.getDouble("precoCusto"),
                rs.getDouble("precoVenda"),
                rs.getInt("quantidadeEstoque")
        );
    }

    //ADICIONA PRODUTO AO BANCO DE DADOS
    public void adicionarProduto(Produto produto) throws ProdutoJaCadastradoException, NomeJaCadastrado, SQLException {
        Produto existente = buscarPorCodigo(produto.getCodigo());
        Produto nomeExiste = buscarPorNome(produto.getNome());
        if (existente != null) {
            throw new ProdutoJaCadastradoException("Ja existe um produto com esse codigo");
        }
        if (nomeExiste != null) {
            throw new NomeJaCadastrado("Nome ja existente");
        }

        produtos.add(produto);
        String sql = "INSERT INTO produto (codigo, nome, precoCusto, precoVenda, quantidadeEstoque) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getNome());
            stmt.setDouble(3, produto.getPrecoCusto());
            stmt.setDouble(4, produto.getPrecoVenda());
            stmt.setInt(5, produto.getQuantidadeEstoque());
            stmt.executeUpdate();
            System.out.println("Produto adicionado com sucesso!");
        } catch (SQLException ex) {
            throw new ProdutoJaCadastradoException(ex.getMessage());
        }

    }

    //VALIDACAO PARA ADICIONAR PRODUTO A LISTA POR NOME
    public Produto buscarPorNome(String nome) throws SQLException {

        String sql = "SELECT * FROM produto WHERE nome = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToProduto(rs);
            }
        }
        return null;
    }

    //VALIDAÇÃO PARA ADICIONAR PRODUTO A LISTA POR CODIGO
    public Produto buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM produto WHERE codigo = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToProduto(rs);
            }
        }
        return null;
    }

    public List<Produto> buscarTodosProdutos() throws SQLException {
        String sql = "SELECT * FROM produto";

        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto p = mapResultSetToProduto(rs);
                produtos.add(p);
            }
        }

        return produtos;
    }

    public boolean apagarProdutoPorCodigo(String codigo) throws SQLException {
        String sql = "DELETE FROM produto WHERE codigo = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement smtm = conn.prepareStatement(sql)) {
            smtm.setString(1, codigo);
            int linhasAfetadas = smtm.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    public boolean atualizarNome(String nome, String codigo) throws SQLException {
        String sql = " UPDATE produto SET nome = ? WHERE codigo = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement smtm = conn.prepareStatement(sql)) {
            smtm.setString(1, nome);
            smtm.setString(2, codigo);
            int linhasAfetadas = smtm.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    public boolean atualizarPrecoCusto(Double precoCusto, String codigo) throws SQLException {
        String sql = " UPDATE produto SET precocusto = ? WHERE codigo = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement smtm = conn.prepareStatement(sql)) {
            smtm.setDouble(1, precoCusto);
            smtm.setString(2, codigo);
            int linhasAfetadas = smtm.executeUpdate();
            return linhasAfetadas > 0;
        }

    }

    public boolean atualizarPrecoVenda(Double precoVenda, String codigo) throws SQLException {
        String sql = " UPDATE produto SET precovenda = ? WHERE codigo = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement smtm = conn.prepareStatement(sql)) {
            smtm.setDouble(1, precoVenda);
            smtm.setString(2, codigo);
            int linhasAfetadas = smtm.executeUpdate();
            return linhasAfetadas > 0;
        }

    }
    // CRIAR DATA DE VALIDADE
}
