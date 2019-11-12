package com.upv.Controladores.Principales;

import com.jfoenix.controls.JFXComboBox;
import com.upv.expeciones.Mensajes;
import com.upv.utils.Parametized;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import upv.poo.datos.usuarios.Disponibilidad.Espacio;
import javafx.scene.control.ToggleButton;
import upv.poo.basededatos.ManagerConnection;
import upv.poo.datos.carreras.Carreras;
import upv.poo.datos.login.Login;
import upv.poo.datos.usuarios.Usuarios;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Disponibilidad implements Initializable, Parametized<Login> {
    //Labels de horario
    @FXML private Label hora1;
    @FXML private Label hora2;
    @FXML private Label hora3;
    @FXML private Label hora4;
    @FXML private Label hora5;
    @FXML private Label hora6;
    @FXML private Label hora7;

    //Botones de asignacion de horarios
    @FXML private ToggleButton lunes1;
    @FXML private ToggleButton lunes2;
    @FXML private ToggleButton lunes3;
    @FXML private ToggleButton lunes4;
    @FXML private ToggleButton lunes5;
    @FXML private ToggleButton lunes6;
    @FXML private ToggleButton lunes7;
    @FXML private ToggleButton martes1;
    @FXML private ToggleButton martes2;
    @FXML private ToggleButton martes3;
    @FXML private ToggleButton martes4;
    @FXML private ToggleButton martes5;
    @FXML private ToggleButton martes6;
    @FXML private ToggleButton martes7;
    @FXML private ToggleButton miercoles1;
    @FXML private ToggleButton miercoles2;
    @FXML private ToggleButton miercoles3;
    @FXML private ToggleButton miercoles4;
    @FXML private ToggleButton miercoles5;
    @FXML private ToggleButton miercoles6;
    @FXML private ToggleButton miercoles7;
    @FXML private ToggleButton jueves1;
    @FXML private ToggleButton jueves2;
    @FXML private ToggleButton jueves3;
    @FXML private ToggleButton jueves4;
    @FXML private ToggleButton jueves5;
    @FXML private ToggleButton jueves6;
    @FXML private ToggleButton jueves7;
    @FXML private ToggleButton viernes1;
    @FXML private ToggleButton viernes2;
    @FXML private ToggleButton viernes3;
    @FXML private ToggleButton viernes4;
    @FXML private ToggleButton viernes5;
    @FXML private ToggleButton viernes6;
    @FXML private ToggleButton viernes7;
    @FXML private ToggleButton sabado1;
    @FXML private ToggleButton sabado2;
    @FXML private ToggleButton sabado3;
    @FXML private ToggleButton sabado4;
    @FXML private ToggleButton sabado5;
    @FXML private ToggleButton sabado6;
    @FXML private ToggleButton sabado7;

    ArrayList<ToggleButton> controladorBotones = new ArrayList<>();
    private boolean turno = false;
    //Otro
    @FXML private JFXComboBox<Usuarios.Usuario> profesoresCBox;
    @FXML private JFXComboBox<String> turnoCBox;
    private Usuarios usuarios;
    private Login usuario;
    private Usuarios.Usuario usuarioSelected;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.profesoresCBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    usuarioSelected = newValue;
                    onChangeDisponibilidad();
                });
        this.turnoCBox.getSelectionModel().selectedItemProperty().addListener(
                ((observableValue, oldValue, newValue) -> {
                    onChangeDisponibilidad();
                })
        );
        onChangeDisponibilidad();
        turno = false;
        cambiarHora();
        cargarBotones();
    }

    public void setValuesProfesoresCBox(Carreras.Carrera c) throws SQLException, ClassNotFoundException {
        this.usuarios = ManagerConnection.getInstance().getUsuarios(c);
        ObservableList<Usuarios.Usuario> usuarios = FXCollections.observableArrayList(this.usuarios.getUsuarios());
        this.profesoresCBox.setItems(usuarios);
    }

    @Override
    public void setParameter(Login value) {
        this.usuario = value;
        onChangeValueInfo();
    }

    @Override
    public Login getParameter() {
        return this.usuario;
    }

    @Override
    public void onChangeValueInfo() {
        try {
            this.setValuesProfesoresCBox(ManagerConnection.getInstance().getCarreras().getCarrera(this.usuario.getIdCarrera()));
            this.turnoCBox.getItems().addAll("Matutino","Vespertino");
        } catch (SQLException | ClassNotFoundException e) {
            Mensajes.setMensaje(e, e.getMessage());
        }
    }
    private void onChangeDisponibilidad(){
        if (this.usuarioSelected!=null){
            try {
                if (this.turnoCBox.getSelectionModel().getSelectedItem()!=null){
                    if (this.turnoCBox.getSelectionModel().getSelectedItem().equals("Matutino")){
                        System.out.println(ManagerConnection.getInstance().getDisponibilidad(usuarioSelected,true));
                        //Cambiamos las horas a matutino
                        turno = false;
                        cambiarHora();
                        actualizarHorario(ManagerConnection.getInstance().getDisponibilidad(usuarioSelected,true).getEspacios());
                    }else if(this.turnoCBox.getSelectionModel().getSelectedItem().equals("Vespertino")){
                        System.out.println(ManagerConnection.getInstance().getDisponibilidad(usuarioSelected,false));
                        //Cambiamos las horas a vespertino
                        turno = true;
                        cambiarHora();
                        actualizarHorario(ManagerConnection.getInstance().getDisponibilidad(usuarioSelected,false).getEspacios());
                    }
                }
            } catch (SQLException | ClassNotFoundException e) {
                Mensajes.setMensaje(e, e.getMessage());
            }
        }else{
            //vaciar
        }
    }

    public void actualizar(){
        for (int i = 0; i < this.controladorBotones.size(); i++) {
            if(this.controladorBotones.get(i).isSelected()){
                this.controladorBotones.get(i).setBackground(new Background(new BackgroundFill(Color.web("#3498db"), CornerRadii.EMPTY, Insets.EMPTY)));
            }else{
                this.controladorBotones.get(i).setBackground(new Background(new BackgroundFill(Color.web("#ffffff"), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    public void actualizarHorario(ArrayList<Espacio> espacios){
        limpiarBotones();
        for (int i = 0; i < this.controladorBotones.size(); i++) {
            this.controladorBotones.get(i).setDisable(false);
        }
        for (int i = 0; i < espacios.size(); i++) {
            int puntos = 0;
            switch(espacios.get(i).getDia()){
                case 1: //Lunes 0
                    puntos = 0;
                    break;
                case 2: //Martes puntos 7 = dia 1 - hora 1
                    puntos = 7;
                    break;
                case 3: //Miercoles
                    puntos = 14;
                    break;
                case 4: //Jueves
                    puntos = 21;
                    break;
                case 5: //Viernes
                    puntos = 28;
                    break;
                case 6:
                    puntos = 35;
                    break;
            }
            puntos = puntos + espacios.get(i).getHora();
            puntos = puntos - 1;
            this.controladorBotones.get(puntos).setSelected(true);
        }
        actualizar();
    }

    public void cambiarHora(){
        if(turno == false){ //En caso de que sea matutino
            hora1.setText("07:00 - 07:54");
            hora2.setText("07:55 - 08:49");
            hora3.setText("08:50 - 09:44");
            hora4.setText("09:45 - 10:39");
            hora5.setText("11:10 - 12:04");
            hora6.setText("12:06 - 12:59");
            hora7.setText("13:00 - 13:54");
        }else{ //En caso que el horario sea vespertino
            hora1.setText("14:00 - 14:54");
            hora2.setText("14:55 - 15:49");
            hora3.setText("15:50 - 16:44");
            hora4.setText("16:45 - 17:39");
            hora5.setText("18:10 - 19:04");
            hora6.setText("19:06 - 19:59");
            hora7.setText("20:00 - 20:54");
        }
    }

    private void cargarBotones(){
        this.controladorBotones.add(this.lunes1);
        this.controladorBotones.add(this.lunes2);
        this.controladorBotones.add(this.lunes3);
        this.controladorBotones.add(this.lunes4);
        this.controladorBotones.add(this.lunes5);
        this.controladorBotones.add(this.lunes6);
        this.controladorBotones.add(this.lunes7);
        this.controladorBotones.add(this.martes1);
        this.controladorBotones.add(this.martes2);
        this.controladorBotones.add(this.martes3);
        this.controladorBotones.add(this.martes4);
        this.controladorBotones.add(this.martes5);
        this.controladorBotones.add(this.martes6);
        this.controladorBotones.add(this.martes7);
        this.controladorBotones.add(this.miercoles1);
        this.controladorBotones.add(this.miercoles2);
        this.controladorBotones.add(this.miercoles3);
        this.controladorBotones.add(this.miercoles4);
        this.controladorBotones.add(this.miercoles5);
        this.controladorBotones.add(this.miercoles6);
        this.controladorBotones.add(this.miercoles7);
        this.controladorBotones.add(this.jueves1);
        this.controladorBotones.add(this.jueves2);
        this.controladorBotones.add(this.jueves3);
        this.controladorBotones.add(this.jueves4);
        this.controladorBotones.add(this.jueves5);
        this.controladorBotones.add(this.jueves6);
        this.controladorBotones.add(this.jueves7);
        this.controladorBotones.add(this.viernes1);
        this.controladorBotones.add(this.viernes2);
        this.controladorBotones.add(this.viernes3);
        this.controladorBotones.add(this.viernes4);
        this.controladorBotones.add(this.viernes5);
        this.controladorBotones.add(this.viernes6);
        this.controladorBotones.add(this.viernes7);
        this.controladorBotones.add(this.sabado1);
        this.controladorBotones.add(this.sabado2);
        this.controladorBotones.add(this.sabado3);
        this.controladorBotones.add(this.sabado4);
        this.controladorBotones.add(this.sabado5);
        this.controladorBotones.add(this.sabado6);
        this.controladorBotones.add(this.sabado7);

        limpiarBotones();
    }

    private void limpiarBotones(){
        for (int i = 0; i < this.controladorBotones.size(); i++) {
            this.controladorBotones.get(i).setSelected(false);
            this.controladorBotones.get(i).setDisable(true);
            this.controladorBotones.get(i).setText("");
            this.controladorBotones.get(i).setBackground(new Background(new BackgroundFill(Color.web("#ffffff"), CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }
}
