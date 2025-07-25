package com.alex.service;

import com.alex.dao.FaturaDAO;
import com.alex.dao.ItemFaturadoDAO;
import com.alex.exception.*;
import com.alex.model.*;
import com.alex.util.FabricaDeDaos;

import java.util.ArrayList;
import java.util.List;

public class FaturaService {
    private final ItemFaturadoDAO iFDAO = FabricaDeDaos.getDAO(ItemFaturadoDAO.class);

    private final FaturaDAO fatDAO = FabricaDeDaos.getDAO(FaturaDAO.class);

    public void adicionar(Fatura fat){
        fatDAO.incluir(fat);
        fat.getCliente().getFaturas().add(fat);
        if(fat.getCliente().getQtdFaturasNaoCanceladas(fat.getCliente().getFaturas()) >= 4)
                                    fat.setValorTotalDoDesconto(fat.getValortotal() * 0.05);

    }

    public Fatura buscarFatura(int id) {
        Fatura fat = fatDAO.recuperarPorId(id);
        if (fat == null) throw new EntidadeNaoEncontradaException(" \n Fatura não existe.");
        return fat;
    }


    public Fatura removerFatura(int id){
        Fatura fat = buscarFatura(id);
        if (fat == null) {
            throw new EntidadeNaoEncontradaException("Fatura não existe.");
        }

        if (fat.getDataCancelamento() != null){
            throw new FaturaCanceladaException("\nA fatura " + id + " está cancelada. Impossível remover.\n");
        }

        if(fat.getCliente().getFaturas().isEmpty()){
            throw new ClienteSemFaturaException("\nO cliente " + fat.getCliente().getId() + " não tem faturas.\n");
        }

        List<ItemFaturado> iF = fat.getItensFaturados();

        if(!iF.isEmpty()) {
            for (ItemFaturado iF2 : iF) {
                Livro livroAtual = iF2.getItemDePedido().getLivro();
                livroAtual.setQtdEstoque(livroAtual.getQtdEstoque() + iF2.getQtdFaturada());
                iFDAO.remover(iF2.getId());
            }
        }

        for(Fatura fatAtual : fat.getCliente().getFaturas()){
            if(fatAtual.getId() == fat.getId()){
                fat.getCliente().getFaturas().remove(fatAtual);
                break;
            }
        }

        fatDAO.remover(fat.getId());
        return fat;
    }

    public Fatura cancelaFatura(Cliente clienteAtual, Fatura fat, String dataCancelamento){
        if(clienteAtual.getFaturas().isEmpty()){
            throw new ClienteSemFaturaException("\nO cliente " + clienteAtual.getId() + " não tem faturas.\n");
        }

        if(clienteAtual.getFaturas().size() < 3){
            throw new ClienteComNumeroInsuficienteDeFaturasException("\nPara cancelar uma fatura é necessário o faturamento de no mínimo 3 pedidos.");
        }

        if (fat == null) {
            throw new EntidadeNaoEncontradaException("Fatura não encontrada.");
        }

        if (fat.getDataCancelamento() != null){
            throw new FaturaCanceladaException("\nA fatura " + fat.getId() + " já cancelado.\n");
        }

        List<ItemFaturado> iFs = fat.getItensFaturados();

        for (ItemFaturado itemFaturado : iFs){
            Livro livroAtual = itemFaturado.getItemDePedido().getLivro();
            livroAtual.setQtdEstoque(livroAtual.getQtdEstoque() + itemFaturado.getQtdFaturada());
        }

        fat.setDataCancelamento(dataCancelamento);

        return fat;
    }

    //essa faturarPedido me levou tempo hein... mas finalmente está feita
    public Fatura faturaPedido(Cliente clienteAtual, Pedido pedidoAtual, String dataEmissao){
        ItemFaturadoService itemFaturadoService = new ItemFaturadoService();

        if(pedidoAtual.getStatus().equals("Cancelado"))
            throw new PedidoIntegralmenteFaturadoException("Pedido " + pedidoAtual.getId() + " cancelado. impossível remover.\n");

        if(pedidoAtual.getStatus().equals("Integralmente faturado"))
            throw new PedidoIntegralmenteFaturadoException("Pedido " + pedidoAtual.getId() + " integralmente Faturado. impossível faturar.\n");

        double vT = 0;
        int empty = 0;
        int fat_nov = 0;
        ItemFaturado iFatual;

        List<ItemDePedido> ip = pedidoAtual.getItemDePedidos();
        List<ItemFaturado> iF_aux = new ArrayList<>();
        for (ItemDePedido itemDePedido : ip) {
            Livro livroAtual = itemDePedido.getLivro();

            if(itemDePedido.getQtdRestante() > 0) {
                if (livroAtual.getQtdEstoque() > 0) {

                    if (itemDePedido.getQtdRestante() > livroAtual.getQtdEstoque()) {
                        iFatual = new ItemFaturado(livroAtual.getQtdEstoque(), itemDePedido);
                        itemFaturadoService.adiciona(iFatual);
                        iF_aux.add(iFatual);
                        vT += iFatual.getQtdFaturada() * livroAtual.getPreco();
                        itemDePedido.getItensFaturados().add((iFatual));
                        itemDePedido.setQtdRestante(itemDePedido.getQtdRestante() - livroAtual.getQtdEstoque());
                        livroAtual.setQtdEstoque(0);
                    }


                    else {
                        iFatual = new ItemFaturado(itemDePedido.getQtdRestante(), itemDePedido);
                        itemFaturadoService.adiciona(iFatual);
                        iF_aux.add(iFatual);
                        vT += iFatual.getQtdFaturada() * livroAtual.getPreco();
                        itemDePedido.getItensFaturados().add((iFatual));
                        livroAtual.setQtdEstoque(livroAtual.getQtdEstoque() - itemDePedido.getQtdRestante());
                        itemDePedido.setQtdRestante(0);
                    }
                } else {
                    empty += 1;
                }
            }
        }

        if(empty == pedidoAtual.getItemDePedidos().size())
            throw new EstoquesVaziosException("Impossível faturar o pedido " + pedidoAtual.getId() + ". Estoques vazios\n");


        for (ItemDePedido itemDePedido : ip){
            if(itemDePedido.getQtdRestante() > 0){
                fat_nov = 1;
                break;
            }
        }

        if(fat_nov == 1) pedidoAtual.setStatus("Não integralmente faturado");

        else pedidoAtual.setStatus("Integralmente faturado");

        Fatura fatAtual = new Fatura(dataEmissao, clienteAtual);

        fatAtual.setValortotal(vT);
        fatAtual.setItensFaturados(iF_aux);

        adicionar(fatAtual);

        return fatAtual;

    }

    public void listarFat(List<Fatura> fats){
        if(fats.isEmpty()) throw new ClienteSemFaturaException("\nO cliente não possui faturas!");
        for (Fatura fatAtual : fats){
            System.out.println("Fatura " + fatAtual.getId() + "," + " valor total da fatura = " + fatAtual.getValortotal() + " valor total do desconto = " + fatAtual.getValorTotalDoDesconto());
            for (ItemFaturado itemFaturado : fatAtual.getItensFaturados()){
                Livro livroAtual = itemFaturado.getItemDePedido().getLivro();
                System.out.println("Livro " + livroAtual.getId() + " - " + "quantidade faturada = " + itemFaturado.getQtdFaturada());
            }
            System.out.println("\n");
        }
    }

    public void imprime_iF(ItemFaturado itemFaturado, Fatura fatura){
        System.out.println("Quantidade faturada : " + itemFaturado.getQtdFaturada());
        System.out.println("Nome do Livro : " + itemFaturado.getItemDePedido().getLivro().getTitulo());
        int ano = fatura.getDataEmissao().getYear();
        int mes = fatura.getDataEmissao().getMonthValue();
        int dia = fatura.getDataEmissao().getDayOfMonth();
        String data;
        if (dia >= 10 && mes < 10){
            data = " " + dia + " /" + " 0" + mes + " /" + " " + ano;
            System.out.println("Data da fatura : " + data);
        }
        if (dia < 10 && mes < 10){
            data = "0" + dia + " /" + " 0" + mes + " /" + " " + ano;
            System.out.println("Data da fatura : " + data);
        }
        if (dia >= 10 && mes >= 10){
            data = " " + dia + " /" + " " + mes + " /" + " " + ano;
            System.out.println("Data da fatura : " + data);
        }
        if (dia < 10 && mes >= 10){
            data = "0" + dia + " /" + " " + mes + " /" + " " + ano;
            System.out.println("Data da fatura : " + data);
        }

    }

    public List<Fatura> achaFaturasPorData(int month, int year, List<Fatura> faturas){
        List<Fatura> fat_Data = new ArrayList<>();
        for(Fatura fatura : faturas){
            int mes = fatura.getDataEmissao().getMonthValue();
            int ano = fatura.getDataEmissao().getYear();
            if(mes == month && ano == year) fat_Data.add(fatura);
        }
        return fat_Data;
    }

    public void relatorio_1(List<Fatura> fats, Livro livro, int mes, int ano){
        List<Fatura> fat_jan = achaFaturasPorData(mes, ano, fats);
        for (Fatura fatura : fat_jan){
            for (ItemFaturado iF : fatura.getItensFaturados()){
                ItemDePedido itemDePedido = iF.getItemDePedido();
                if(itemDePedido.getLivro() == livro){
                    imprime_iF(iF, fatura);
                    System.out.println("\n");
                }
            }
        }

    }

    public void relatorio_2(List<Livro> livros){
        int flag = 1;
        for (Livro livro : livros){
            if(livro.getItemDePedidos().isEmpty()){
                flag = 0;
                System.out.println("O livro " + livro.getId() + " nunca foi faturado.");
                System.out.println("\n");
            }

            else{
                for(ItemDePedido itemDePedido : livro.getItemDePedidos()){
                    if(itemDePedido.getItensFaturados().isEmpty()){
                        flag = 0;
                        System.out.println("O livro " + livro.getId() + " nunca foi faturado.");
                        System.out.println("\n");
                    }
                }
            }
        }

        if(flag == 1) System.out.println("Todos os livros foram faturados uma vez ao menos.\n");
    }

    public void relatorio_3(List<Fatura> faturas, List<Livro> livros, int mes, int ano){
        int cont = 0;
        List<Fatura> fat_fev = achaFaturasPorData(mes,ano, faturas);

        for (Livro livro : livros){
            for (Fatura fat : fat_fev){
                for (ItemFaturado iF : fat.getItensFaturados()){
                    ItemDePedido itemDePedido = iF.getItemDePedido();
                    if(itemDePedido.getLivro() == livro) cont += iF.getQtdFaturada(); //tá dando diferente do gabarito, mas livro 4 foi faturado apenas em janeiro.
                }
            }
            if(cont != 0)
                System.out.println("Livro " + livro.getId() + " - quantidade faturada = " + cont);
            cont = 0;
        }

        System.out.println("\n");
    }

    public List<Fatura> recuperarFaturas(){
        return fatDAO.recuperarTodos();
    }
}
