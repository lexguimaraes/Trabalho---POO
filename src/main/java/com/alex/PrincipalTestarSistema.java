package com.alex;

import com.alex.exception.*;
import com.alex.model.*;
import com.alex.service.PedidoService;
import com.alex.service.ClienteService;
import com.alex.service.LivroService;
import com.alex.service.FaturaService;

import java.util.List;

public class PrincipalTestarSistema {

    String verde = "\u001B[32m";
    String vermelho = "\u001B[31m" ;
    String amarelo = "\u001B[33m";
    String normal = "\u001B[0m";

    private final PedidoService pedidoService = new PedidoService();

    private final ClienteService clienteService = new ClienteService();

    private final LivroService livroService = new LivroService();

    private final FaturaService faturaService = new FaturaService();

    public void principal() {

        Cliente cliente_1, cliente_2;
        String dataEmissao_fevereiro = "28/02/2025/00:00:00", dataCancelamento = "06/01/2025/00:00:00";
        Livro livro_1, livro_2, livro_3, livro_4, livro_5;
        ItemDePedido umItemDePedido;

        System.out.println('\n' + amarelo + "========================================================" + normal);
        System.out.println('\n' + amarelo + "Iniciando Teste."+ normal);
        System.out.println('\n' + amarelo + "========================================================\n" + normal);

        System.out.println(verde + "Questão 1. Cadastrando 5 livros solicitados...\n" + normal);
        livro_1 = new Livro ("10", "Aaa", "Aaaa", 10, 100);
        livro_2 = new Livro("20", "Bbb", "Bbbb", 20, 200);
        livro_3 = new Livro("30", "Ccc", "Cccc", 30, 300);
        livro_4 = new Livro("40", "Ddd", "Dddd", 40, 400);
        livro_5 = new Livro("50", "Eee", "Eeee", 50, 500);

        livroService.adicionar(livro_1);
        livroService.adicionar(livro_2);
        livroService.adicionar(livro_3);
        livroService.adicionar(livro_4);
        livroService.adicionar(livro_5);

        List<Livro> livros = livroService.recuperarLivros();

        System.out.println(verde + "Questão 2. Listando os livros solicitados..." + normal);
        try {
            livroService.listarLivros(livros);
        } catch (SemLivrosException e) {
            System.out.println('\n' + e.getMessage());
        }
        System.out.println("\n");

        System.out.println(verde + "Questão 3. Cadastrando  os 2 clientes como solicitado...\n" + normal);
        cliente_1 = new Cliente("111", "Xxxx", "xxxx@gmail.com", "11111111");
        cliente_2 = new Cliente("222", "Yyyy", "yyyy@gmail.com", "22222222");

        clienteService.adiciona(cliente_1);
        clienteService.adiciona(cliente_2);

        System.out.println(verde + "Questões (4 e 5). Cadastrando e Listando os 5 pedidos..." + normal);
        //Trabalho braçal mesmo rapaz!!!
        Pedido pedido1 = new Pedido("01/01/2025/23:00:00", cliente_1);
        pedido1.setStatus("Não faturado");
        umItemDePedido = new ItemDePedido(5, 5, livro_1.getPreco(), livro_1, pedido1);
        pedido1.getItemDePedidos().add(umItemDePedido);
        livro_1.getItemDePedidos().add(umItemDePedido);

        umItemDePedido = new ItemDePedido(15, 15, livro_2.getPreco(), livro_2, pedido1);
        pedido1.getItemDePedidos().add(umItemDePedido);
        livro_2.getItemDePedidos().add(umItemDePedido);
        pedidoService.adiciona(pedido1);


        Pedido pedido2 = new Pedido("02/01/2025/23:00:00", cliente_1);
        pedido2.setStatus("Não faturado");
        umItemDePedido = new ItemDePedido(10, 10, livro_1.getPreco(), livro_1, pedido2);
        pedido2.getItemDePedidos().add(umItemDePedido);
        livro_1.getItemDePedidos().add(umItemDePedido);

        umItemDePedido = new ItemDePedido(40, 40, livro_3.getPreco(), livro_3, pedido2);
        pedido2.getItemDePedidos().add(umItemDePedido);
        livro_3.getItemDePedidos().add(umItemDePedido);
        pedidoService.adiciona(pedido2);


        Pedido pedido3 = new Pedido("03/01/2025/23:00:00", cliente_1);
        pedido3.setStatus("Não faturado");
        umItemDePedido = new ItemDePedido(5, 5, livro_1.getPreco(), livro_1, pedido3);
        pedido3.getItemDePedidos().add(umItemDePedido);
        livro_1.getItemDePedidos().add(umItemDePedido);

        umItemDePedido = new ItemDePedido(10, 10, livro_3.getPreco(), livro_3, pedido3);
        pedido3.getItemDePedidos().add(umItemDePedido);
        livro_3.getItemDePedidos().add(umItemDePedido);
        pedidoService.adiciona(pedido3);


        Pedido pedido4 = new Pedido("04/01/2025/23:59:00", cliente_1);
        pedido4.setStatus("Não faturado");
        umItemDePedido = new ItemDePedido(10, 10, livro_2.getPreco(), livro_2, pedido4);
        pedido4.getItemDePedidos().add(umItemDePedido);
        livro_2.getItemDePedidos().add(umItemDePedido);

        umItemDePedido = new ItemDePedido(10, 10, livro_3.getPreco(), livro_3, pedido4);
        pedido4.getItemDePedidos().add(umItemDePedido);
        livro_3.getItemDePedidos().add(umItemDePedido);

        umItemDePedido = new ItemDePedido(10, 10, livro_4.getPreco(), livro_4, pedido4);
        pedido4.getItemDePedidos().add(umItemDePedido);
        livro_4.getItemDePedidos().add(umItemDePedido);
        pedidoService.adiciona(pedido4);


        Pedido pedido5 = new Pedido("05/01/2025/23:59:00", cliente_1);
        pedido5.setStatus("Não faturado");
        umItemDePedido = new ItemDePedido(5, 5, livro_2.getPreco(), livro_2, pedido5);
        pedido5.getItemDePedidos().add(umItemDePedido);
        livro_2.getItemDePedidos().add(umItemDePedido);

        umItemDePedido = new ItemDePedido(5, 5, livro_3.getPreco(), livro_3, pedido5);
        pedido5.getItemDePedidos().add(umItemDePedido);
        livro_3.getItemDePedidos().add(umItemDePedido);

        umItemDePedido = new ItemDePedido(5, 5, livro_4.getPreco(), livro_4, pedido5);
        pedido5.getItemDePedidos().add(umItemDePedido);
        livro_4.getItemDePedidos().add(umItemDePedido);
        pedidoService.adiciona(pedido5);

        pedidoService.listarPedidos(pedidoService.recuperarPedidos());
        System.out.println("\n");

        System.out.println(verde + "Questão 6. Listando os livros solicitados...\n" + normal);
        try {
            livroService.listarLivros(livros);
        } catch (SemLivrosException e) {
            System.out.println('\n' + e.getMessage());
        }
        System.out.println("\n");

        System.out.println(amarelo + "Questão 7. Faturando os pedidos 1 e 2, respectivamente, para janeiro de 2025..." + normal);

        try{
            faturaService.faturaPedido(cliente_1, pedido1, "10/01/2025/00:00:00");
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        try{
            faturaService.faturaPedido(cliente_1, pedido2, "10/01/2025/00:00:00");
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }
        System.out.println("\n");

        System.out.println(vermelho + "Questão 8. Cancelando a segunda fatura..." + normal);
        try{
            Fatura umaFatura = faturaService.buscarFatura(2);
            faturaService.cancelaFatura(cliente_1, umaFatura, dataCancelamento);
        } catch(ClienteSemFaturaException | EntidadeNaoEncontradaException | FaturaCanceladaException |
                ClienteComNumeroInsuficienteDeFaturasException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\n");

        System.out.println(amarelo + "Questão 9. Faturando os pedidos 3 e 4, respectivamente, para janeiro de 2025..." + normal);
        try{
            faturaService.faturaPedido(cliente_1, pedido3, "20/01/2025/00:00:00");
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        try{
            faturaService.faturaPedido(cliente_1, pedido4, "20/01/2025/00:00:00");
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }
        System.out.println("\n");


        System.out.println(amarelo + "Questão 10. Faturando o pedido 5 para fevereiro de 2025..." + normal);
        try{
            faturaService.faturaPedido(cliente_1, pedido5, dataEmissao_fevereiro);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        System.out.println("\n");

        System.out.println(verde + "Questão 11. Listando todos os livros..." + normal);
        try {
            livroService.listarLivros(livros);
        } catch (SemLivrosException e) {
            System.out.println('\n' + e.getMessage());
        }
        System.out.println("\n");

        System.out.println(verde + "Questão 12. Listando todas as faturas..." + normal);
        try {
            faturaService.listarFat(faturaService.recuperarFaturas());
        } catch (ClienteSemFaturaException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(vermelho + "Questão 13. Cancelamneto do pedido 5 para o dia 06/01/2025..." + normal);
        try{
            pedidoService.cancelarPedido(cliente_1, pedido5, dataCancelamento);
        } catch(ClienteSemPedidosException | PedidoCanceladoException | PedidoFaturadoException |
                ImpossivelCancelarPedidoException e){
            System.out.println(e.getMessage());
        }
        System.out.println("\n");

        System.out.println(vermelho + "Questão 14. Cancelamento da fatura 3 para o dia 06/01/2025..." + normal);
        try{
            faturaService.cancelaFatura(cliente_1, faturaService.buscarFatura(3), dataCancelamento);
        } catch(ClienteSemFaturaException | EntidadeNaoEncontradaException | FaturaCanceladaException |
                ClienteComNumeroInsuficienteDeFaturasException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n");

        System.out.println(vermelho + "Questão 15. Removendo a fatura 3..." + normal);
        try {
            faturaService.removerFatura(3);
        } catch (EntidadeNaoEncontradaException | FaturaCanceladaException | ClienteSemFaturaException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\n");

        System.out.println(vermelho + "Questão 16. Removendo a fatura 4..." + normal);
        try {
            faturaService.removerFatura(4);
        } catch (EntidadeNaoEncontradaException | FaturaCanceladaException | ClienteSemFaturaException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\n");

        System.out.println(verde + "Questão 17. Listando todos os livros..." + normal);
        try {
            livroService.listarLivros(livros);
        } catch (SemLivrosException e) {
            System.out.println('\n' + e.getMessage());
        }
        System.out.println("\n");

        System.out.println(verde + "Questão 18. Abastecimento do estoque..." + normal);
        livroService.updt_Estoque(livros, 100);
        System.out.println("\n");

        System.out.println(verde + "Questão 19. Listando todos os livros..." + normal);
        try {
            livroService.listarLivros(livros);
        } catch (SemLivrosException e) {
            System.out.println('\n' + e.getMessage());
        }
        System.out.println("\n");

        System.out.println(amarelo + "Questão 20. Faturando os pedidos 1 a 5, respectivamente, para fevereiro de 2025..." + normal);
        System.out.println("\n");

        //Aqui eu tive que colocar em try's catch's individuais, porque quando eu colocava junto, estava dando problema

        try{
            faturaService.faturaPedido(cliente_1, pedido1, dataEmissao_fevereiro);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        try{
            faturaService.faturaPedido(cliente_1, pedido2, dataEmissao_fevereiro);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        try{
            faturaService.faturaPedido(cliente_1, pedido3, dataEmissao_fevereiro);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        try{
            faturaService.faturaPedido(cliente_1, pedido4, dataEmissao_fevereiro);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        try{
            faturaService.faturaPedido(cliente_1, pedido5, dataEmissao_fevereiro);
        } catch(PedidoCanceladoException | PedidoIntegralmenteFaturadoException | EstoquesVaziosException e){
            System.out.println(e.getMessage());
        }

        System.out.println("\u001B[36m" + "Questão 21. Relatório 1..." + normal);
        faturaService.relatorio_1(faturaService.recuperarFaturas(), livro_1, 1, 2025);
        System.out.println("\n");

        System.out.println("\u001B[36m" + "Questão 22. Relátorio 2..." + normal);
        faturaService.relatorio_2(livros);
        System.out.println("\n");

        //A questão do relatório 3 ainda é um enigma, pois as quantidades pedidas do livro 4 foram faturadas somente em janeiro,
        //então em tese o livro 4 não deveria aparecer no relatório 3
        System.out.println("\u001B[36m" + "Questão 23. Relatório 3..." + normal);
        faturaService.relatorio_3(faturaService.recuperarFaturas(), livros, 2, 2025);

        System.out.println('\n' +"\u001B[36m"+ "========================================================");
        System.out.println('\n' + "Fin!");
        System.out.println('\n'+ "========================================================\n");

    }
}

//QUE TRABALHEIRA!!!
//mereço um açai depois disso
