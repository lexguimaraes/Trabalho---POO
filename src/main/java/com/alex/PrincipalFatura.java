package com.alex;

import com.alex.exception.*;
import com.alex.model.Pedido;
import com.alex.service.PedidoService;
import com.alex.model.Cliente;
import com.alex.service.ClienteService;
import com.alex.model.Fatura;
import com.alex.service.FaturaService;
import corejava.Console;

public class PrincipalFatura {

    private final PedidoService pedidoService = new PedidoService();

    private final ClienteService clienteService = new ClienteService();

    private final FaturaService faturaService = new FaturaService();

    public void principal(){

        String dataEmissao;
        String dataCancelamento;
        Cliente clienteAtual;
        Pedido umPedido;
        Fatura fatAtual;

        boolean continua = true;
        while(continua){

            System.out.println('\n' + "========================================================");
            System.out.println('\n' + "O que você deseja fazer?");
            System.out.println('\n' + "1. Faturar um Pedido");
            System.out.println("2. Cancelar uma Fatura");
            System.out.println("3. Remover uma Fatura");
            System.out.println("4. Listar todas as Faturas de um Cliente");
            System.out.println("5. Voltar");

            int opcao = Console.readInt('\n' + "Digite um número entre 1 e 5:");

            System.out.println();

            switch(opcao){
                case 1 ->{
                    int idCliente = Console.readInt("Informe o id do Cliente:");

                    try{
                        clienteAtual = clienteService.BuscaCliente(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    if(clienteAtual.getPedidos().isEmpty()){
                        System.out.println("\nO Cliente não tem pedidos!");
                        break;
                    }

                    int idPedido = Console.readInt("Informe o id do Pedido:");

                    try{
                        umPedido = pedidoService.BuscaPedido(idPedido);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    dataEmissao = Console.readLine("Informe a data e hora da Fatura no formato dd/MM/aaaa/hh:MM:ss");
                    try{
                        pedidoService.verificaData(dataEmissao);
                    } catch(DataInvalidaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    try{
                        faturaService.faturaPedido(clienteAtual, umPedido, dataEmissao);
                    } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
                        System.out.println(e.getMessage());
                    }

                    System.out.println("Faturamento concluído!");
                }

                case 2 ->{
                    int idCliente = Console.readInt("Informe o id do Cliente:");

                    try{
                        clienteAtual = clienteService.BuscaCliente(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    int idFatura = Console.readInt("Informe o id da Fatura:");

                    try{
                        fatAtual = faturaService.buscarFatura(idFatura);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    dataCancelamento = Console.readLine("Informe a data e hora do Cancelamento do Pedido no formato dd/MM/aaaa/hh:MM:ss");
                    try{
                        pedidoService.verificaData(dataCancelamento);
                    } catch(DataInvalidaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    try{
                        faturaService.cancelaFatura(clienteAtual, fatAtual, dataCancelamento);
                    } catch(ClienteSemFaturaException | EntidadeNaoEncontradaException | FaturaCanceladaException |
                            ClienteComNumeroInsuficienteDeFaturasException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("\nFatura cancelada com sucesso!");
                }

                case 3 ->{
                    int idCliente = Console.readInt("Informe o id do Cliente:");

                    try{
                        clienteAtual = clienteService.BuscaCliente(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    int idFatura = Console.readInt("Informe o id da Fatura:");

                    try {
                        faturaService.removerFatura(idFatura);
                    } catch (EntidadeNaoEncontradaException | FaturaCanceladaException | ClienteSemFaturaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("\nFatura removida com sucesso.");
                }

                case 4 ->{
                    int idCliente = Console.readInt("Informe o id do Cliente:");

                    try{
                        clienteAtual = clienteService.BuscaCliente(idCliente);
                    } catch(EntidadeNaoEncontradaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    try{
                        faturaService.listarFat(clienteAtual.getFaturas());
                    } catch(ClienteSemFaturaException e){
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("Faturas listadas com sucesso.");
                }

                case 5 ->{
                    continua = false;
                }

                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }
}
