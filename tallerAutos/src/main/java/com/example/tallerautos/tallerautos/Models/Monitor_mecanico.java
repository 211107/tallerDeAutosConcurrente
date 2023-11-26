package com.example.tallerautos.tallerautos.Models;



public class Monitor_mecanico {
    private boolean min_mecanico_esperando;
    private boolean max_mecanico_esperando;
    public Monitor_mecanico() {
        min_mecanico_esperando = true;
        max_mecanico_esperando = false;
    }
    public synchronized void wait_buffet () {
        while (!min_mecanico_esperando) { //Guarda booleana
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("El mecanico tiene un auto que reparar");
        min_mecanico_esperando = false;
        max_mecanico_esperando = true;
        this.notifyAll();
    }

    public synchronized void release_buffet () {
        while (!max_mecanico_esperando) //Guarda booleana
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        System.out.println("El mecanico ha arreglado el problema");
        max_mecanico_esperando = false;
        min_mecanico_esperando = true;
        this.notifyAll();
    }
}

