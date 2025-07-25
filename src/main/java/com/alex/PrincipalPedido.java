package com.alex;

import com.alex.exception.*;
import com.alex.model.ItemDePedido;
import com.alex.model.Pedido;
import com.alex.service.PedidoService;
import com.alex.model.Cliente;
import com.alex.service.ClienteService;
import com.alex.model.Livro;
import com.alex.service.LivroService;
import corejava.Console;

public class PrincipalPedido {

    private final PedidoService pedidoService = new PedidoService();

    private final ClienteService clienteService = new ClienteService();

    private final LivroService livroService = new LivroService();

    public void principal(){

        Pedido pedidoAtual;
        Cliente clienteAtual;
        Livro livroAtual;

        String dataEmissao;
        String dataCancelamento;

        int qtdPedida;
        int idLivro;

        boolean continua = true;
        while(continua){

            System.out.println('\n' + "========================================================");
            System.out.println('\n' + "O que você deseja fazer?");
            System.out.println('\n' + "1. Fazer um Pedido");
            System.out.println("2. Cancelar um Pedido");
            System.out.println("3. Listar todos os Pedidos de um Cliente");
            System.out.println("4. Voltar");

            int opcao = Console.readInt('\n' + "Digite um número entre 1 e 5:");

            System.out.println();

            switch (opcao) {
                case 1 -> {
                    int idCliente = Console.readInt("Insira o id do Cliente:");

                    try{
                        clienteAtual = clienteService.BuscaCliente(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    dataEmissao = Console.readLine("Insira a data e hora do Pedido no formato dd/MM/aaaa/hh:MM:ss");

                    try{
                        pedidoService.verificaData(dataEmissao);
                    } catch(DataInvalidaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    qtdPedida = Console.readInt("Insira a quantidade desejada: ");
                    idLivro = Console.readInt("Insira o id do livro: ");

                    try{
                        livroAtual = livroService.BuscaLivro(idLivro);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    pedidoAtual = new Pedido(dataEmissao, clienteAtual);
                    ItemDePedido itemDePedido = new ItemDePedido(qtdPedida, qtdPedida, livroAtual.getPreco(), livroAtual, pedidoAtual);
                    pedidoAtual.getItemDePedidos().add(itemDePedido);
                    livroAtual.getItemDePedidos().add(itemDePedido);
                    pedidoAtual.setStatus("Não faturado");
                    System.out.println("\nPedido realizado com sucesso!");

                    while(true){
                        String op = Console.readLine("\nDeseja adicionar mais itens de pedido? s/n");
                        if(op.equals("n") || op.equals("N")){
                            System.out.println("\nPedido cadastrado!");
                            pedidoService.adiciona(pedidoAtual);
                            break;
                        }

                        if(op.equals("s") || op.equals("S")){
                            qtdPedida = Console.readInt("Insira a quantidade: ");
                            idLivro = Console.readInt("Insira o id do livro: ");

                            try{
                                livroAtual = livroService.BuscaLivro(idLivro);
                            } catch(EntidadeNaoEncontradaException e){
                                System.out.println(e.getMessage());
                                break;
                            }

                            itemDePedido = new ItemDePedido(qtdPedida, qtdPedida, livroAtual.getPreco(), livroAtual, pedidoAtual);
                            pedidoAtual.getItemDePedidos().add(itemDePedido);
                            livroAtual.getItemDePedidos().add(itemDePedido);
                            System.out.println("\nItem de pedido adicionado com sucesso!");
                        }

                        else {
                            System.out.println('\n' + "Opção inválida! Item não contabilizado!");
                        }
                    }
                }

                case 2 -> {
                    int idCliente = Console.readInt("Insira o id do Cliente:");

                    try{
                        clienteAtual = clienteService.BuscaCliente(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    int idPedido = Console.readInt("Insira o id do Pedido:");

                    try{
                        pedidoAtual = pedidoService.BuscaPedido(idPedido);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    dataCancelamento = Console.readLine("Insira a data e hora do Cancelamento do Pedido no formato dd/MM/aaaa/hh:MM:ss");
                    try{
                        pedidoService.verificaData(dataCancelamento);
                    } catch(DataInvalidaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    try{
                        pedidoService.cancelarPedido(clienteAtual, pedidoAtual, dataCancelamento);
                    } catch(ClienteSemPedidosException | PedidoCanceladoException | PedidoFaturadoException |
                            ImpossivelCancelarPedidoException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("\nO Pedido " + pedidoAtual.getId() + " foi cancelado com sucesso!\n");

                }

                case 3 -> {
                    int idCliente = Console.readInt("Insira o id do Cliente:");

                    try{
                        clienteAtual = clienteService.BuscaCliente(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    try{
                        pedidoService.listarPedidos(clienteAtual.getPedidos());
                    } catch(ClienteSemPedidosException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("\nTodos os pedidos listados com sucesso!");
                }
                case 4 ->{
                    continua = false;
                }
                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }
}
