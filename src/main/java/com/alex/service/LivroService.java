package com.alex.service;

import com.alex.dao.LivroDAO;
import com.alex.exception.EntidadeComListaNaoVaziaException;
import com.alex.exception.EntidadeNaoEncontradaException;
import com.alex.exception.SemLivrosException;
import com.alex.model.Livro;
import com.alex.util.FabricaDeDaos;

import java.util.List;

public class LivroService {
    private final LivroDAO livroDAO = FabricaDeDaos.getDAO(LivroDAO.class);

    public void adicionar(Livro livro){ livroDAO.incluir(livro); }



    public Livro BuscaLivro(int id) {
        Livro livro = livroDAO.recuperarPorId(id);
        if (livro == null) throw new EntidadeNaoEncontradaException(" \n Livro não existe.");
        return livro;
    }

    public Livro remover(int id){
        Livro livro = BuscaLivro(id);
        if (livro == null) throw new EntidadeNaoEncontradaException("Livro não existe.");
        if(!(livro.getItemDePedidos().isEmpty())) throw new EntidadeComListaNaoVaziaException(" \n Este livro possui pedido e não pode ser removido!");
        livroDAO.remover(livro.getId());
        return livro;
    }

    public Livro alterarIsbn(Livro livro, String novoIsbn){
        livro.setIsbn(novoIsbn);
        return livro;
    }

    public Livro alterarTitulo(Livro livro, String novoTitulo){
        livro.setTitulo(novoTitulo);
        return livro;
    }

    public Livro alterarDescricao(Livro livro, String novaDescricao){
        livro.setDescricao(novaDescricao);
        return livro;
    }

    public Livro alterarEstoque(Livro livro, int novaQtdEstoque){
        livro.setQtdEstoque(novaQtdEstoque);
        return livro;
    }

    public Livro alterarPreco(Livro livro, double novoPreco){
        livro.setPreco(novoPreco);
        return livro;
    }

    public void listarLivros(List<Livro> livros){
        if(livros.isEmpty())throw new SemLivrosException("\nNão há livros!");
        else for (Livro livro : livros) System.out.println(livro);
    }

    public void updt_Estoque(List<Livro> livros, int qtd){
        int cont = qtd;
        for(Livro livro : livros){
            livro.setQtdEstoque(livro.getQtdEstoque() + cont);
            cont += qtd;
        }
    }

    public List<Livro> recuperarLivros(){return livroDAO.recuperarTodos(); }


}
