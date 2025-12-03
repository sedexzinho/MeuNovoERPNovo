package org.example.interacao;

import org.example.model.Produto;
import org.example.service.EstoqueService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MenuFuncoes {

    public void validarProduto(EstoqueService estoqueService, Scanner scanner) throws SQLException {

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

    //BUSCA PRODUTO POR CODIGO
    public void buscarProdutoCodigo(EstoqueService estoqueService, Scanner scanner) throws SQLException {
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

    public void buscarTodosProdutos(EstoqueService estoqueService, Scanner scanner) throws SQLException {
        try {
            List<Produto> produtos = estoqueService.buscarTodosProdutos();
            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto encontrado\n");
            }
            for (Produto p : estoqueService.buscarTodosProdutos()) {
                p.exibirInfo();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar: " + e.getMessage());

        }
    }

    public void RemoverProduto(EstoqueService estoqueService, Scanner scanner) throws SQLException {

        try {
            System.out.println("Digite o codigo do produto: ");
            String codigo = scanner.next();

            boolean removido = estoqueService.apagarProdutoPorCodigo(codigo);
            if (removido) {
                System.out.println("Produto removido com sucesso");
            } else {
                System.out.println("Produto nao exite");
            }

        } catch (Exception e) {
            System.out.println("Erro ao buscar: " + e.getMessage());

        }
    }

    public void atualizarDadosProduto(EstoqueService estoqueService, Scanner scanner) {
        try {
            System.out.println("Digite o codigo do produto: ");
            String codigoUsuario = scanner.next();
            Produto codigo = estoqueService.buscarPorCodigo(codigoUsuario);
            if (codigo != null) {
                codigo.exibirInfo();
                System.out.println("Qual dado do produto deseja atualizar?");
                int opcaoAtualizar = 0;
                while (opcaoAtualizar != 4) {
                    System.out.println("1 - Atualizar nome do produto");
                    System.out.println("2 - Atualizar valor do custo do produto");
                    System.out.println("3 - Atualizar valor de venda do produto");
                    System.out.println("4 - sair");
                    opcaoAtualizar = scanner.nextInt();
                    switch (opcaoAtualizar) {
                        case 1:
                            System.out.println("Digite o novo nome do produto ");
                            String novoNome = scanner.next();
                            Produto nomeExiste = estoqueService.buscarPorNome(novoNome);
                            if (nomeExiste != null) {
                                System.out.println("Ja existe um produto com esse nome");
                            } else {
                                boolean atualizado = estoqueService.atualizarNome(novoNome, codigoUsuario);
                                if (atualizado) {
                                    System.out.println("Produto atualizado com sucesso");
                                }
                            }
                            break;

                        case 2:
                            System.out.println("Digite o novo valor de custo ");
                            Double novoValorCusto = scanner.nextDouble();
                            boolean valorCompraAtualizado = estoqueService.atualizarPrecoCusto(novoValorCusto, codigoUsuario);
                            if (valorCompraAtualizado ) {
                                System.out.println("Valor de Compra atualizado com sucesso");
                            }
                            break;
                        case 3:
                            System.out.println("Digite o novo valor do produto ");
                            Double novoValorCompra = scanner.nextDouble();
                            boolean valorAtualizadoCompra = estoqueService.atualizarPrecoVenda(novoValorCompra, codigoUsuario);
                            if (valorAtualizadoCompra) {
                                System.out.println("Valor de Venda atualizada com sucesso");
                            }
                            break;
                    }
                }
            } else {
                System.out.println("Produto nao exite");
            }
        } catch (Exception e) {
            System.out.println("Erro ao atualizar: " + e.getMessage());
        }
    }
}

