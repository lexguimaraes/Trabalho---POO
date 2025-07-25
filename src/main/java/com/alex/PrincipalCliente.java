package com.alex;

import com.alex.exception.EntidadeComListaNaoVaziaException;
import com.alex.exception.EntidadeNaoEncontradaException;
import com.alex.exception.SemClientesException;
import com.alex.model.Cliente;
import com.alex.service.ClienteService;
import corejava.Console;

import java.util.List;

public class PrincipalCliente {

    private final ClienteService clienteService = new ClienteService();

    public void principal(){

        String cpf;
        String nome;
        String email;
        String telefone;
        Cliente umCliente;

        boolean continua = true;
        while(continua){
            System.out.println('\n' + "========================================================");
            System.out.println('\n' + "O que você deseja fazer?");
            System.out.println('\n' + "1. Cadastrar um cliente");
            System.out.println("2. Alterar um cliente");
            System.out.println("3. Remover um cliente");
            System.out.println("4. Listar todos clientes");
            System.out.println("5. Voltar");

            int opcao = Console.readInt('\n' + "Digite um número entre 1 e 5:");

            System.out.println();


            switch(opcao){
                case 1 -> {
                    cpf = Console.readLine("Digite o CPF do cliente: ");
                    nome = Console.readLine("Digite o Nome do cliente: ");
                    email = Console.readLine("Digite o Email do cliente: ");
                    telefone = Console.readLine("Digite o Telefone do cliente: ");
                    umCliente = new Cliente(cpf, nome, email, telefone);
                    clienteService.adiciona(umCliente);
                    System.out.println("\nCliente cadastrado com sucesso!");
                }

                case 2 -> {
                    int id = Console.readInt("Digite o id do cliente que você deseja alterar: ");

                    try {
                        umCliente = clienteService.BuscaCliente(id);
                    } catch (EntidadeNaoEncontradaException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println('\n' + "O que você deseja alterar?");
                    System.out.println('\n' + "1. CPF");
                    System.out.println("2. Nome");
                    System.out.println("3. Email");
                    System.out.println("4. Telefone");

                    int opalt = Console.readInt('\n' + "Digite um número entre 1 e 4:");

                    switch (opalt) {
                        case 1 -> {
                            String novoCPF = Console.readLine("Informe o novo CPF: ");
                            clienteService.alterarCPF(umCliente, novoCPF);
                            System.out.println('\n' + "Alteração do CPF realizada com sucesso!");
                        }
                        case 2 -> {
                            String novoNome = Console.readLine("Informe o novo Nome: ");
                            clienteService.alterarNome(umCliente, novoNome);
                            System.out.println('\n' + "Alteração do Nome realizada com sucesso!");
                        }

                        case 3 -> {
                            String novoEmail = Console.readLine("Informe o novo Email: ");
                            clienteService.alterarEmail(umCliente, novoEmail);
                            System.out.println('\n' + "Alteração do Email realizada com sucesso!");
                        }

                        case 4 -> {
                            String novoTelefone = Console.readLine("Informe o novo Telefone: ");
                            clienteService.alterarTelefone(umCliente, novoTelefone);
                            System.out.println('\n' + "Alteração do Telefone realizada com sucesso!");
                        }
                        default -> System.out.println('\n' + "Opção inválida!");
                    }
                }

                case 3 -> {
                    int id = Console.readInt("Digite o id do cliente que deseja remover: ");

                    try {
                        clienteService.remover(id);
                        System.out.println('\n' + "Cliente removido com sucesso!");
                    } catch (EntidadeNaoEncontradaException | EntidadeComListaNaoVaziaException e) {
                        System.out.println('\n' + e.getMessage());
                    }
                }

                case 4 -> {
                    List<Cliente> clientes = clienteService.recuperarClientes();

                    try {
                        clienteService.listarClientes(clientes);
                    } catch (SemClientesException e) {
                        System.out.println('\n' + e.getMessage());
                        break;
                    }

                    System.out.println("\nClientes listados com sucesso!");

                }

                case 5 -> {
                    continua = false;
                }
                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }
}
