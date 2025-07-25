package com.alex.service;

import com.alex.dao.ItemFaturadoDAO;
import com.alex.exception.EntidadeNaoEncontradaException;
import com.alex.model.ItemFaturado;
import com.alex.util.FabricaDeDaos;

import java.util.List;

public class ItemFaturadoService {
    private final ItemFaturadoDAO itemFaturadoDAO = FabricaDeDaos.getDAO(ItemFaturadoDAO.class);

    public void adiciona(ItemFaturado itemFaturado){
        itemFaturadoDAO.incluir(itemFaturado);
    }




    public ItemFaturado BuscaItemFaturado(int id){
        ItemFaturado iF = itemFaturadoDAO.recuperarPorId(id);
        if (iF == null) throw new EntidadeNaoEncontradaException(" \n Item Faturado não existe");
        return iF;
    }




    public ItemFaturado remover(int id) {
        ItemFaturado itemFaturado = BuscaItemFaturado(id);
        if (itemFaturado == null) throw new EntidadeNaoEncontradaException("Item Faturado não existe.");
        itemFaturadoDAO.remover(itemFaturado.getId());
        return itemFaturado;
    }




    public List<ItemFaturado> recuperarItemFaturados(){
        return itemFaturadoDAO.recuperarTodos();
    }

}
