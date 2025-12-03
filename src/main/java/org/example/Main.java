package org.example;

import org.example.conectores.ConexaoBD;
import org.example.interacao.MenuFuncoes;
import org.example.service.EstoqueService;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConexaoBD.inicializarBanco();
        Scanner scanner = new Scanner(System.in);
        EstoqueService estoqueService = new EstoqueService();
        MenuFuncoes menuFuncoes = new MenuFuncoes();


        int opcaoMenu = 0;
        while (opcaoMenu != 5) {
            System.out.println("Escolha uma opção");
            System.out.println("1 - Cadastrar novo produto");
            System.out.println("2 - Bucar por codigo");
            System.out.println("3 - Listar todos os produtos");
            System.out.println("4 - Apagar produto por codigo");
            System.out.println("5 - Atualizar produto(por codigo) ");
            System.out.println("6 - Sair");
            opcaoMenu = scanner.nextInt();
            scanner.nextLine();
            switch (opcaoMenu) {
                case 1:
                    menuFuncoes.validarProduto(estoqueService, scanner);
                    break;
                case 2:
                    menuFuncoes.buscarProdutoCodigo(estoqueService, scanner);
                    break;
                case 3:
                    menuFuncoes.buscarTodosProdutos(estoqueService, scanner);
                    break;
                case 4:
                    menuFuncoes.RemoverProduto(estoqueService, scanner);
                    break;
                case 5:
                    menuFuncoes.atualizarDadosProduto(estoqueService, scanner);
            }

        }
    }
}
