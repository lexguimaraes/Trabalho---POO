package com.alex.service;

import com.alex.dao.ItemDePedidoDAO;
import com.alex.exception.EntidadeNaoEncontradaException;
import com.alex.model.ItemDePedido;
import com.alex.util.FabricaDeDaos;

import java.util.List;

public class ItemDePedidoService {
    private final ItemDePedidoDAO itemDePedidoDAO = FabricaDeDaos.getDAO(ItemDePedidoDAO.class);



    public void adiciona(ItemDePedido itemDePedido) { itemDePedidoDAO.incluir(itemDePedido); }




    public ItemDePedido BuscaItemDePedido(int id){
        ItemDePedido ip = itemDePedidoDAO.recuperarPorId(id);
        if (ip == null) throw new EntidadeNaoEncontradaException(" \n Item de Pedido não está no DAO");
        return ip;
    }




    public ItemDePedido remover(int id) {
        ItemDePedido ip = BuscaItemDePedido(id);
        if (ip == null) throw new EntidadeNaoEncontradaException("Item de Pedido não está no DAO");
        itemDePedidoDAO.remover(ip.getId());
        return ip;
    }




    public List<ItemDePedido> recuperarItemDePedidos(){
        return itemDePedidoDAO.recuperarTodos();
    }



}
