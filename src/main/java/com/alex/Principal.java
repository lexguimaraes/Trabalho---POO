package com.alex;

import com.alex.dao.*;
import com.alex.model.*;
import com.alex.util.FabricaDeDaos;
import corejava.Console;

import java.io.*;
import java.util.Map;

public class Principal {
    public static void main(String[] args) {

        //Definindo as principais que iremos usar
        PrincipalCliente principalCliente = new PrincipalCliente();
        PrincipalLivro principalLivro = new PrincipalLivro();
        PrincipalPedido principalPedido = new PrincipalPedido();
        PrincipalFatura principalFatura = new PrincipalFatura();
        PrincipalTestarSistema principalTestarSistema = new PrincipalTestarSistema();

        recuperarDados();
        boolean loop = true;
        while (loop) {
            System.out.println('\n' + "========================================================");
            System.out.println('\n' + "O que você deseja fazer?");
            System.out.println('\n' + "1. Testar Sistema");
            System.out.println("2. Tratar de Faturas");
            System.out.println("3. Tratar de Pedidos");
            System.out.println("4. Tratar de Livros");
            System.out.println("5. Tratar de Clientes");
            System.out.println("6. Sair");

            int opcao = Console.readInt('\n' + "Digite um número entre 1 e 6:");

            System.out.println();

            switch (opcao) {
                case 1 -> { //caso 1 para testar o sistema
                    principalTestarSistema.principal();
                    //salvarDados(); <- desabilitada neste caso para não influenciar as consequentes execuções do código
                    loop = false;
                }
                case 2 -> { //caso 2 para faturas
                    principalFatura.principal();
                }
                case 3 -> { //caso 3 para pedidos
                    principalPedido.principal();
                }
                case 4 -> { //caso 4 para livros
                    principalLivro.principal();
                }
                case 5 -> { //caso 5 para para clientes
                    principalCliente.principal();
                }
                case 6 -> { //caso 6 para sair
                    loop = false;
                    salvarDados();
                }
                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
    }

    private static void recuperarDados() {
        try {
            ClienteDAO clienteDAO = FabricaDeDaos.getDAO(ClienteDAO.class);
            LivroDAO livroDAO = FabricaDeDaos.getDAO(LivroDAO.class);
            PedidoDAO pedidoDAO = FabricaDeDaos.getDAO(PedidoDAO.class);
            FaturaDAO faturaDAO = FabricaDeDaos.getDAO(FaturaDAO.class);

            FileInputStream fis = new FileInputStream(new File("meusObjetos.txt"));
            ObjectInputStream ois = new ObjectInputStream(fis);

            //Clientes
            Map<Integer, Cliente> mapDeClientes = (Map<Integer, Cliente>) ois.readObject();
            clienteDAO.setMap(mapDeClientes);
            Integer contadorDeClientes = (Integer) ois.readObject();
            clienteDAO.setContador(contadorDeClientes);

            //Livro
            Map<Integer, Livro> mapDeLivros = (Map<Integer, Livro>) ois.readObject();
            livroDAO.setMap(mapDeLivros);
            Integer contadorDeLivro = (Integer) ois.readObject();
            livroDAO.setContador(contadorDeLivro);

            //Pedido
            Map<Integer,Pedido> mapDePedidos = (Map<Integer, Pedido>) ois.readObject();
            pedidoDAO.setMap(mapDePedidos);
            Integer contadorPedido = (Integer) ois.readObject();
            pedidoDAO.setContador(contadorPedido);

            //Fatura
            Map<Integer, Fatura> mapDeFaturas = (Map<Integer,Fatura>) ois.readObject();
            faturaDAO.setMap(mapDeFaturas);
            Integer contadorDeFatura = (Integer) ois.readObject();
            faturaDAO.setContador(contadorDeFatura);


        } catch (FileNotFoundException e) {
            System.out.println("Será criado um arquivo meusObjetos.txt na finalização do programa.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private static void salvarDados() {
        ClienteDAO clienteDAO = FabricaDeDaos.getDAO(ClienteDAO.class);
        LivroDAO livroDAO = FabricaDeDaos.getDAO(LivroDAO.class);
        PedidoDAO pedidoDAO = FabricaDeDaos.getDAO(PedidoDAO.class);
        FaturaDAO faturaDAO = FabricaDeDaos.getDAO(FaturaDAO.class);

        Map<Integer, Cliente> mapDeClientes = clienteDAO.getMap();
        Map<Integer, Livro> mapDeLivros = livroDAO.getMap();
        Map<Integer, Pedido> mapDePedidos = pedidoDAO.getMap();
        Map<Integer, Fatura> mapDeFaturas = faturaDAO.getMap();

        Integer contadorDeCliente = clienteDAO.getContador();
        Integer contadorDeLivro = livroDAO.getContador();
        Integer contadorDePedido = pedidoDAO.getContador();
        Integer contadorDeFatura = faturaDAO.getContador();


        try {
            FileOutputStream fos = new FileOutputStream(new File("meusObjetos.txt"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(mapDeClientes);
            oos.writeObject(contadorDeCliente);

            oos.writeObject(mapDeLivros);
            oos.writeObject(contadorDeLivro);

            oos.writeObject(mapDePedidos);
            oos.writeObject(contadorDePedido);

            oos.writeObject(mapDeFaturas);
            oos.writeObject(contadorDeFatura);

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}