package org.example.service;

import org.example.Exception.NomeJaCadastrado;
import org.example.Exception.ProdutoJaCadastradoException;
import org.example.conectores.ConexaoBD;
import org.example.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public void adicionarProduto(Produto produto) throws ProdutoJaCadastradoException, NomeJaCadastrado, SQLException {
        Produto existente = buscarPorCodigo(produto.getCodigo());
        Produto nomeExistente = buscarPorNome(produto.getNome());
        if (existente != null) {
            throw new ProdutoJaCadastradoException("Ja existe um produto com esse codigo");
        }
        if (nomeExistente != null) {
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

    public Produto buscarPorNome(String nome) {

        for (Produto p : produtos) {
            if (p.getNome().equals(nome)) {
                return p;
            }
        }
        return null;
    }

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




}
