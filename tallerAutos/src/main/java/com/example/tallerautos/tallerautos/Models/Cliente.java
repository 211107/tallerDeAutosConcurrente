package com.example.tallerautos.tallerautos.Models;

import java.util.Observable;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import java.util.Observable;
import java.util.Random;

public class Cliente extends Observable implements Runnable {

    private Position pos;
    public int velocidad;
    private boolean status;
    private Random random;
    public Cliente (int velocidad) {
        this.status = true;
        this.velocidad = velocidad;
        this.random = new Random(System.currentTimeMillis());
    }

    public void setPosicion (Position pos) {
        this.pos = pos;
    }

    @Override
    public void run() {

        while (status) {
            setChanged();
            notifyObservers(pos);
            try {
                Thread.sleep(70);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            if (pos.getX() <= 150 && pos.getY() == 450) {
                try {
                    //Tiempo de espera de 8 seg a 12 seg dentro del establecimiento
                    Thread.sleep(random.nextInt(12000) + 8000);
                    pos.setX(140);
                    pos.setY(115);
                    pos.setX(pos.getX() - 10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                pos.setX(pos.getX() - 10);
            }

            if (pos.getX() < 150) {
                pos.setX(pos.getX() - 10);
            }
        }

    }
    public void setStatus (boolean status) { this.status = status; }
}
