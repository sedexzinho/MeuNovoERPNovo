package org.example.interacao;

import org.example.conectores.ConexaoBD;
import org.example.model.Produto;
import org.example.service.EstoqueService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class menuFuncoes {
    public static void adicionarProduto(EstoqueService estoqueService, Scanner scanner) throws SQLException {

        while (true) {
            String codigo;
            String nome;
            double custoCompra;
            double custoVenda;
            int quantidadeEstoque;
            System.out.println("Digite o codigo (ou 'pare') para encerrar");
            codigo = scanner.next();

            if (codigo.equals("pare")) {
                if (estoqueService.verificaListaVazia()) {
                    System.err.println("Cadastre pelo menos um produto");
                    continue;
                }
                break;
            }

            if (estoqueService.buscarPorCodigo(codigo) != null) {
                System.err.println("O codigo ja existe, tente outro");
                continue;
            }
            System.out.println("Nome do produto: ");
            nome = scanner.next();
            if (estoqueService.buscarPorNome(nome) != null) {
                System.err.println("O nome ja existe, tente outro");
                continue;
            }
            System.out.println("Valor de compra:");
            custoCompra = scanner.nextDouble();
            do {
                System.out.println("Valor de venda:");
                custoVenda = scanner.nextDouble();
                if (custoVenda < custoCompra) {
                    System.err.println("O valor de venda NÃO pode ser menor que o de compra. Tente novamente.");
                }
            } while (custoVenda < custoCompra);

            System.out.println("Quantidade de Estoque do produto: ");
            quantidadeEstoque = scanner.nextInt();

            if (quantidadeEstoque < 0 || quantidadeEstoque > 100001) {
                System.out.println("Quantidade invalida, digite um numero entre 1 e 100.000");
                continue;
            }
            scanner.nextLine();
            Produto produto = new Produto(codigo, nome, custoCompra, custoVenda, quantidadeEstoque);
            try {
                estoqueService.adicionarProduto(produto);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public static void buscarProdutoCodigo(EstoqueService estoqueService, Scanner scanner) throws SQLException {
        try {
            System.out.println("Digite o codigo do produto: ");
            String codigo = scanner.next();
            Produto produto = estoqueService.buscarPorCodigo(codigo);
            if (produto != null) {
                produto.exibirInfo();
            } else {
                System.out.println("Produto nao exite");
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar: " + e.getMessage());
        }


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
}
