package com.alex.util;

import com.alex.model.Cliente;

public class EstendeThread extends Thread {
    Cliente cliente;

    public EstendeThread(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        System.out.println("\nEnviando email para \n" + cliente.getEmail());
    }
}


