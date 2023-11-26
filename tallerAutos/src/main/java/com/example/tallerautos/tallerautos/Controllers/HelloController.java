package com.example.tallerautos.tallerautos.Controllers;

import com.example.tallerautos.tallerautos.Models.Cliente;
import com.example.tallerautos.tallerautos.Models.Monitor_Recepcionista;
import com.example.tallerautos.tallerautos.Models.Monitor_mecanico;
import com.example.tallerautos.tallerautos.Models.Position;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class HelloController implements Observer {
    @FXML
    private AnchorPane stage;
    @FXML
    private Label espacios;
    @FXML
    private ImageView mecanico_1, mecanico_2, mecanico_3, mecanico_4, mecanico_5, mecanico_6, mecanico_7, mecanico_8, mecanico_9, mecanico_10;
    private final int N = 100;
    private int MAX_QUEUE = 10;
    private Random random;

    private final String label_color = "-fx-text-fill: black;";
    private String path = "C:\\Users\\esmer\\Documents\\CUATRI 9\\concurrente\\C3\\proyectoFin\\tallerAutos\\src\\main\\resources\\com\\example\\tallerautos\\tallerautos\\assets\\imgs\\car_";

    ImageView[] imageViews = new ImageView[N];
    Thread[] hilo_cliente = new Thread[N];
    Label[] labels = new Label[N];
    private Monitor_Recepcionista recepcionista;
    private Monitor_mecanico mecanico;
    boolean[] tables = new boolean[MAX_QUEUE];
    boolean[] salida_cliente = new boolean[N];
    private Cliente cliente;
    private static int cont_clientes;
    private boolean guarda_boolean = false;
    int control_espacios;
    int generar = 0;

    //Método que genera un número aleatorio para una imagen
    public int generate_random_image() {
        return (int) (Math.random() * 8) + 1;
    }

    //Método que genera un número aleatorio evaluar espacios disponibles
    public int generate_random_table() {
        return (int) (Math.random() * 10) + 1;
    }

    @FXML
    void empezarOnMouseClicked(MouseEvent event) {
        int space = 0;
        int serial_number = 1; //Id del cliente en etiquetas

        for (int i = 0; i < N; i++) {
            int random = generate_random_image();
            Image image = new Image(path + (random) + ".png"); //Asigna una imagen aleatorio a objeto Image
            imageViews[i] = new ImageView(image);
            imageViews[i].setFitWidth(40); // ajusta el ancho de la imagen
            imageViews[i].setFitHeight(80); // ajusta la altura de la imagen
            imageViews[i].setImage(image);
            imageViews[i].setLayoutX(1000 + space); //Cliente llega desde lado derecho a izquierdo
            imageViews[i].setLayoutY(450);
            space += 150;

            cliente = new Cliente(10); //Se instancia clase cliente
            cliente.addObserver(this);
            cliente.setPosicion(new Position(i, (int) (imageViews[i].getLayoutX()), (int) (imageViews[i].getLayoutY())));
            hilo_cliente[i] = new Thread(cliente); //Se crea arreglo de hilos
            hilo_cliente[i].setName("Cliente " + serial_number);
            labels[i] = new Label(hilo_cliente[i].getName()); //Se asignan etiquetas a los hilos
            labels[i].setStyle(label_color);
            stage.getChildren().addAll(imageViews[i], labels[i]);
            hilo_cliente[i].start(); //Se lanzan los hilos
            serial_number++;
        }
    }

    //Método que llena los arreglos a False
    public void fill_array_to_false() {
        for (int i = 0; i < 10; i++) {
            tables[i] = false;
        }
        for (int i = 0; i < N; i++)
            salida_cliente[i] = false;
    }

    //Se inicializan los siguientes valores al ejecutar el programa
    @FXML
    public void initialize() {
        random = new Random(System.currentTimeMillis()); // para generar números aleatorios
        cont_clientes = 0;
        control_espacios = 1;
        recepcionista = new Monitor_Recepcionista(); //Instancia única de recepcionista
        mecanico = new Monitor_mecanico(); //Instancia única de mecanico
        fill_array_to_false();
    }

    @Override
    public void update(Observable o, Object arg) {
        Position pos = (Position) arg;
        Platform.runLater(() -> {
            tracking(pos.getId(), pos.getX(), pos.getY()); //Método que realiza el comportamiento de los hilos Cliente
            int cantidad = this.cont_clientes; //Cantidad de clientes actuales dentro del taller
        });
    }

    public void tracking(int pos, int posX, int posY) {
        generar = generate_random_table();

        imageViews[pos].setLayoutX(posX);
        imageViews[pos].setLayoutY(posY);
        labels[pos].setLayoutX(imageViews[pos].getLayoutX());
        labels[pos].setLayoutY(imageViews[pos].getLayoutY());

        //condición que evalúa la salida del cliente
        if (imageViews[pos].getLayoutX() <= 0 && salida_cliente[pos] != true) {
            salida_cliente[pos] = true;
            this.cont_clientes--;
        }

        //Condición cuando llega el cliente, aumenta num clientes y modifica la guarda boleana
        if (imageViews[pos].getLayoutX() == 150 && imageViews[pos].getLayoutY() == 450) {
            this.cont_clientes++;
            if (this.cont_clientes >= MAX_QUEUE) {
                System.out.println("Clientes: " + this.cont_clientes);
                activar_clientes_espera(hilo_cliente, pos);
                System.out.println("espacios no disponibles");
                cont_clientes = cont_clientes - 1;
                guarda_boolean = true;
            }
            //Switch que controla el comportamiento de los clientes al llegar a los espacios
            switch (control_espacios) {
                case 1: {
                    imageViews[pos].setLayoutX(220);
                    imageViews[pos].setLayoutY(210);
                    labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                    labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                    tables[generar - 1] = false; //Variable que controla la disponibilidad de los espacios
                    mecanico_1.setVisible(true); //Simula la atención del mecanico
                    break;
                }
                case 2: {
                    imageViews[pos].setLayoutX(345);
                    imageViews[pos].setLayoutY(210);
                    labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                    labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                    tables[generar - 1] = false;
                    mecanico_2.setVisible(true);
                    break;
                }
                case 3: {
                    imageViews[pos].setLayoutX(470);
                    imageViews[pos].setLayoutY(210);
                    labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                    labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                    tables[generar - 1] = false;
                    mecanico_3.setVisible(true);
                    break;
                }
                case 4: {
                    imageViews[pos].setLayoutX(585);
                    imageViews[pos].setLayoutY(210);
                    labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                    labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                    tables[generar - 1] = false;
                    mecanico_4.setVisible(true);
                    break;
                }
                case 5: {
                    imageViews[pos].setLayoutX(700);
                    imageViews[pos].setLayoutY(210);
                    labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                    labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                    tables[generar - 1] = false;
                    mecanico_5.setVisible(true);
                    break;
                }
                case 6: {
                    imageViews[pos].setLayoutX(225);
                    imageViews[pos].setLayoutY(370);
                    labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                    labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                    tables[generar - 1] = false;
                    mecanico_6.setVisible(true);
                    break;
                }
                case 7: {
                    imageViews[pos].setLayoutX(350);
                    imageViews[pos].setLayoutY(370);
                    labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                    labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                    tables[generar - 1] = false;
                    mecanico_7.setVisible(true);
                    break;
                }
                case 8: {
                    imageViews[pos].setLayoutX(475);
                    imageViews[pos].setLayoutY(370);
                    labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                    labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                    tables[generar - 1] = false;
                    mecanico_8.setVisible(true);
                    break;
                }
                case 9: {
                    imageViews[pos].setLayoutX(590);
                    imageViews[pos].setLayoutY(370);
                    labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                    labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                    tables[generar - 1] = false;
                    mecanico_9.setVisible(true);
                    break;
                }
                case 10: {
                    imageViews[pos].setLayoutX(705);
                    imageViews[pos].setLayoutY(370);
                    labels[pos].setLayoutX(imageViews[pos].getLayoutX());
                    labels[pos].setLayoutY(imageViews[pos].getLayoutY());
                    tables[generar - 1] = false;
                    mecanico_10.setVisible(true);
                    break;
                }
            }

            if (control_espacios == 10) {
                mecanico.wait_buffet(); //Se activa monitor al no haber disponibilidad
                control_espacios = 0;
                mecanico.release_buffet(); //Se libera monitor para que el mecanico quede en reposo en espera de nuevos clientes
            }
            control_espacios++;  //Aumenta el número de mesas para tener control de los clientes
        }

        if (guarda_boolean && cont_clientes == 0) { //Se evalúa guarda boleana para activar monitor recepcionista
            recepcionista.esperar_cola(); //Se para cola al llenarse el taller
            desactivar_clientes_espera(hilo_cliente, pos); //Detiene los hilos de clientes en espera
            cont_clientes = cont_clientes + 1;
            recepcionista.liberar_cola(); //Libera señal para permitir entrada de los clientes
            guarda_boolean = false;  //Guarda boleana cambia a false para control de hilos entrantes
        }

    }

    //Método que activa la espera de clientes, pausa proceso de hilos
    public void activar_clientes_espera(Thread[] hilo_cliente, int pos) {
        for (int i = pos; i < hilo_cliente.length; i++)
            hilo_cliente[i].suspend();
    }

    //Método que reanuda proceso de hilos al vacirse el taller, espacios disponibles
    public void desactivar_clientes_espera(Thread[] hilo_cliente, int pos) {
        //Oculta los mecanicos, simulación de que el mecanico ha terminado
        mecanico_1.setVisible(false);
        mecanico_2.setVisible(false);
        mecanico_3.setVisible(false);
        mecanico_4.setVisible(false);
        mecanico_5.setVisible(false);
        mecanico_6.setVisible(false);
        mecanico_7.setVisible(false);
        mecanico_8.setVisible(false);
        mecanico_9.setVisible(false);
        mecanico_10.setVisible(false);
        for (int i = pos; i < hilo_cliente.length; i++) {
            hilo_cliente[i].resume(); // Reanuda los hilos en su proceso para posteriormente salir del lugar

            // Agrega la lógica para mostrar los mecánicos nuevamente
            mecanico_1.setVisible(true);
            mecanico_2.setVisible(true);
            mecanico_3.setVisible(true);
            mecanico_4.setVisible(true);
            mecanico_5.setVisible(true);
            mecanico_6.setVisible(true);
            mecanico_7.setVisible(true);
            mecanico_8.setVisible(true);
            mecanico_9.setVisible(true);
            mecanico_10.setVisible(true);
        }
    }
}