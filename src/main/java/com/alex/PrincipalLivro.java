package com.alex;

import com.alex.exception.EntidadeComListaNaoVaziaException;
import com.alex.exception.EntidadeNaoEncontradaException;
import com.alex.exception.SemLivrosException;
import com.alex.model.Livro;
import com.alex.service.LivroService;
import corejava.Console;

import java.util.List;

public class PrincipalLivro {

    private final LivroService livroService = new LivroService();

    public void principal(){

        String isbn;
        String titulo;
        String descricao;
        int qtdEstoque;
        double preco;
        Livro livroAtual;

        boolean continua = true;
        while(continua){

            System.out.println('\n' + "========================================================");
            System.out.println('\n' + "O que você deseja fazer?");
            System.out.println('\n' + "1. Cadastrar um Livro");
            System.out.println("2. Alterar um Livro");
            System.out.println("3. Remover um Livro");
            System.out.println("4. Listar todos os Livros");
            System.out.println("5. Voltar");

            int opcao = Console.readInt('\n' + "Digite um número entre 1 e 5:");

            System.out.println();

            switch (opcao) {
                case 1 -> {
                    isbn = Console.readLine("Insira o ISBN do livro:");
                    titulo = Console.readLine("Insira o titulo do livro:");
                    descricao = Console.readLine("Insira a descricao do livro:");
                    qtdEstoque = Console.readInt("Insira a quantidade:");
                    preco = Console.readInt("Insira o preco do livro:");
                    livroAtual = new Livro(isbn, titulo, descricao, qtdEstoque, preco);
                    livroService.adicionar(livroAtual);
                    System.out.println("\nLivro cadastrado!");

                }
                case 2 -> {
                    int id = Console.readInt("Insira o id do livro que você deseja alterar: ");

                    try {
                        livroAtual = livroService.BuscaLivro(id);
                    } catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println('\n' + "O que você deseja alterar?");
                    System.out.println('\n' + "1. ISBN");
                    System.out.println("2. Titulo");
                    System.out.println("3. Descricao");
                    System.out.println("4. QtdEstoque");
                    System.out.println("5. Preco");

                    int opcaoAlteracao = Console.readInt('\n' + "Digite um número entre 1 e 5:");

                    switch (opcaoAlteracao) {
                        case 1 -> {
                            String novoISBN = Console.readLine("Insira o novo ISBN: ");
                            livroService.alterarIsbn(livroAtual, novoISBN);
                            System.out.println('\n' + "ISBN alterado com sucesso!");
                        }
                        case 2 -> {
                            String novoTitulo = Console.readLine("Insira o novo Titulo: ");
                            livroService.alterarTitulo(livroAtual, novoTitulo);
                            System.out.println('\n' + "Título alterado com sucesso!");
                        }

                        case 3 -> {
                            String novaDescricao = Console.readLine("Insira o novo Descricao: ");
                            livroService.alterarDescricao(livroAtual, novaDescricao);
                            System.out.println('\n' + "Descrição alterada com sucesso!");
                        }

                        case 4 -> {
                            int novaQtdEstoque = Console.readInt("Insira a nova QtdEstoque: ");
                            livroService.alterarEstoque(livroAtual, novaQtdEstoque);
                            System.out.println('\n' + "QtdEstoque alterada com sucesso!");
                        }

                        case 5 -> {
                            int novoPreco = Console.readInt("Insira o novo Preco: ");
                            livroService.alterarPreco(livroAtual, novoPreco);
                            System.out.println('\n' + "Preço alterado com sucesso!");
                        }

                        default -> System.out.println('\n' + "Opção inválida!");
                    }
                }
                case 3 -> {
                    int id = Console.readInt("Insira o id do Livro que deseja remover: ");

                    try {
                        livroService.remover(id);
                        System.out.println('\n' + "Livro removido com sucesso!");
                    } catch (EntidadeNaoEncontradaException | EntidadeComListaNaoVaziaException e) {
                        System.out.println('\n' + e.getMessage());
                    }
                }
                case 4 -> {
                    List<Livro> livros = livroService.recuperarLivros();

                    try {
                        livroService.listarLivros(livros);
                    } catch (SemLivrosException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println("\nLivros listados com sucesso!");
                }
                case 5 ->{
                    continua = false;
                }
                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }
}
