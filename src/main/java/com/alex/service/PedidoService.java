package com.alex.service;

import com.alex.dao.PedidoDAO;
import com.alex.exception.*;
import com.alex.model.*;
import com.alex.util.EstendeThread;
import com.alex.util.FabricaDeDaos;

import com.alex.dao.ItemDePedidoDAO;

import java.text.ParsePosition;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

public class PedidoService {
    private final ItemDePedidoService itemDePedidoService = new ItemDePedidoService();

    private final PedidoDAO pedidoDAO = FabricaDeDaos.getDAO(PedidoDAO.class);

    public Pedido adiciona(Pedido pedido){
        EstendeThread thread = new EstendeThread(pedido.getCliente());
        thread.start();
        pedidoDAO.incluir(pedido);
        pedido.getCliente().getPedidos().add(pedido);
        for(ItemDePedido umItemDePedido : pedido.getItemDePedidos()){
            itemDePedidoService.adiciona(umItemDePedido);
        }
        return pedido;
    }

    public Pedido BuscaPedido(int id) {
        Pedido pedido = pedidoDAO.recuperarPorId(id);
        if (pedido == null) throw new EntidadeNaoEncontradaException(" \n Pedido não existe.");
        return pedido;
    }


    public void verificaData(String data){
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy/HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        TemporalAccessor tA = f.parseUnresolved(data, pos);
        int mes = Integer.parseInt(data.substring(3,5));
        int dia = Integer.parseInt(data.substring(0,2));
        if (tA == null || pos.getIndex() < data.length() || mes < 1 || mes > 12 || dia < 0 || dia > 31)
            throw new DataInvalidaException("Data inválida!\n");
    }

    public Pedido remover(int id){
        Pedido pedido = this.BuscaPedido(id);
        if (pedido == null) throw new EntidadeNaoEncontradaException("Pedido não existe.");
        List<ItemDePedido> itemDePedidos = pedido.getItemDePedidos();
        for (ItemDePedido ip : itemDePedidos) itemDePedidoService.remover(ip.getId());
        for(Pedido pedidoAtual : pedido.getCliente().getPedidos()){
            if(pedidoAtual.getId() == pedido.getId()){
                pedido.getCliente().getPedidos().remove(pedidoAtual);
                break;
            }
        }
        pedidoDAO.remover(pedido.getId());
        return pedido;
    }

    public Pedido cancelarPedido(Cliente clienteAtual, Pedido pedidoAtual, String dataCancelamento) {
        if (clienteAtual.getPedidos().isEmpty()) throw new ClienteSemPedidosException("\nO cliente " + clienteAtual.getId() + " não possui pedidos.\n");
        if (pedidoAtual.getStatus().equals("Cancelado")) throw new PedidoCanceladoException("\nO pedido " + pedidoAtual.getId() + " já está cancelado.\n");
        if (pedidoAtual.getStatus().equals("Integralmente faturado") || pedidoAtual.getStatus().equals("Não integralmente faturado")) throw new PedidoFaturadoException("\nO pedido " + pedidoAtual.getId() + " foi faturado, Impossível cancelar.\n");


        boolean tem_fatura = false;

        for(Fatura fatura : clienteAtual.getFaturas()){
            for(ItemFaturado iF : fatura.getItensFaturados()){
                if(iF.getItemDePedido().getPedido().getId() == pedidoAtual.getId()){
                    tem_fatura = true;
                    break;
                }
            }
        }


        boolean faturas_canceladas = true;
        if (tem_fatura == true) {
            for(Fatura fatura : clienteAtual.getFaturas()){
                for(ItemFaturado itemFaturado : fatura.getItensFaturados()){
                    if(itemFaturado.getItemDePedido().getPedido().getId() == pedidoAtual.getId() && fatura.getDataCancelamento() == null){
                        faturas_canceladas = false;
                        break;
                    }
                }
            }
        }

        if (tem_fatura == false || faturas_canceladas) {
            pedidoAtual.setDataCancelamento(dataCancelamento);
            pedidoAtual.setStatus("Cancelado");
        } else {
            throw new ImpossivelCancelarPedidoException("\n Impossível cancelar este pedido " + pedidoAtual.getId() + " !\n");
        }

        return pedidoAtual;
    }


    public void listarPedidos(List<Pedido> pedidos){
        if(pedidos == null)throw new ClienteSemPedidosException("Não há pedidos");
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
            List<ItemDePedido> ip = pedido.getItemDePedidos();
            for (ItemDePedido itemDePedido : ip) System.out.println(itemDePedido);
            System.out.println("\n");
        }
    }

    public List<Pedido> recuperarPedidos(){
        return pedidoDAO.recuperarTodos();
    }

}
