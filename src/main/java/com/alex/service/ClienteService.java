package com.alex.service;

import com.alex.dao.ClienteDAO;
import com.alex.exception.ClienteComPedidosException;
import com.alex.exception.EntidadeNaoEncontradaException;
import com.alex.exception.SemClientesException;
import com.alex.model.Cliente;
import com.alex.util.FabricaDeDaos;

import java.util.List;

public class ClienteService {
    private final ClienteDAO clienteDAO = FabricaDeDaos.getDAO(ClienteDAO.class);

    public void adiciona(Cliente cliente) {  clienteDAO.incluir(cliente); }

    public Cliente BuscaCliente(int id){
        Cliente aux = clienteDAO.recuperarPorId(id);
        if (aux == null)
            throw new EntidadeNaoEncontradaException(" \n Cliente não existe.");
        return aux;
    }

    public Cliente remover(int id) {
        Cliente aux = BuscaCliente(id);
        if (aux == null) {
            throw new EntidadeNaoEncontradaException("Cliente não existe.");
        }
        if (!(aux.getPedidos().isEmpty())) {
            throw new ClienteComPedidosException("Cliente ainda tem pedidos.");
        }
        clienteDAO.remover(aux.getId());
        return aux;
    }

    public Cliente alterarCPF(Cliente aux, String novoCPF){
        aux.setCpf(novoCPF);
        return aux;
    }

    public Cliente alterarNome(Cliente aux, String novoNome){
        aux.setNome(novoNome);
        return aux;
    }

    public Cliente alterarEmail(Cliente aux, String novoEmail){
        aux.setEmail(novoEmail);
        return aux;
    }

    public Cliente alterarTelefone(Cliente aux, String novoTelefone){
        aux.setTelefone(novoTelefone);
        return aux;
    }

    public void listarClientes(List<Cliente> aux){
        if(aux.isEmpty()){
            throw new SemClientesException("\n Sem clientes");
        }
        else{
            for (Cliente vasco:aux) {
                System.out.println(vasco);
            }
        }
    }

    public Cliente cadastrarCliente(String cpf, String nome, String email, String telefone){
        Cliente aux = new Cliente(cpf, nome, email, telefone);
        clienteDAO.incluir(aux);
        return aux;
    }

    public List<Cliente> recuperarClientes(){
        return clienteDAO.recuperarTodos();
    }

}
