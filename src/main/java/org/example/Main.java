package org.example;

import org.example.conectores.ConexaoBD;
import org.example.model.Produto;
import org.example.service.EstoqueService;

import java.sql.SQLException;
import java.util.Scanner;

import static org.example.interacao.menuFuncoes.adicionarProduto;
import static org.example.interacao.menuFuncoes.buscarProdutoCodigo;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConexaoBD.inicializarBanco();
        Scanner scanner = new Scanner(System.in);
        EstoqueService estoqueService = new EstoqueService();

        int opcaoMenu = 0;
        while (opcaoMenu != 5) {
            System.out.println("Escolha uma opção");
            System.out.println("1 - Cadastrar novo produto");
            System.out.println("2 - Bucar por codigo");
            System.out.println("3 - Listar todos os produtos");
            System.out.println("4 - Remover produto");
            System.out.println("5 - Apagar produto por codigo");
            System.out.println("6 - Sair");
            opcaoMenu = scanner.nextInt();
            scanner.nextLine();
            switch (opcaoMenu) {
                case 1:
                    adicionarProduto(estoqueService, scanner);
                    break;
                case 2:
                    buscarProdutoCodigo(estoqueService, scanner);
                case 3:

            }

        }
    }






}





