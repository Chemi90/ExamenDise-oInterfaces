package com.example.examendiseniointerfaces;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.temporal.ChronoUnit;

public class CesurParkingController {

    @FXML
    private TextField txtMatricula;
    @FXML
    private ComboBox<String> cbModelo;
    @FXML
    private RadioButton rbStandard;
    @FXML
    private RadioButton rbOferta;
    @FXML
    private RadioButton rbLargaDuracion;
    @FXML
    private DatePicker dtEntrada;
    @FXML
    private DatePicker dtSalida;
    @FXML
    private Label lbCoste;
    @FXML
    private Button btnAñadir;
    @FXML
    private Button btnSalir;
    @FXML
    private ImageView imgLogo;
    @FXML
    private VBox vbGeneral;
    @FXML
    private TableColumn cMatricula;
    @FXML
    private TableColumn cModelo;
    @FXML
    private TableColumn cEntrada;
    @FXML
    private TableColumn cSalida;
    @FXML
    private TableColumn cCliente;
    @FXML
    private TableColumn cTarifa;
    @FXML
    private TableColumn cCoste;
    @FXML
    private Label lbFooter;
    @FXML
    private Label lbParking;
    @FXML
    private ToggleGroup Eleccion;
    @FXML
    private TableView<EntradaParking> tableView;
    @FXML
    private ChoiceBox<Cliente> cbCliente;

    @FXML
    public void initialize() {
        // Rellena el ComboBox de Modelo
        ObservableList<String> modelos = FXCollections.observableArrayList("Modelo A", "Modelo B", "Modelo C");
        cbModelo.setItems(modelos);
        cbModelo.setEditable(true);

        // Rellena el ChoiceBox de Cliente
        ObservableList<Cliente> clientes = FXCollections.observableArrayList(
                new Cliente(1, "Manolo", "correo1@example.com"),
                new Cliente(2, "Ataulfo", "correo2@example.com"),
                new Cliente(3, "Rodolfo", "correo3@example.com")
        );
        cbCliente.setItems(clientes);
        cbCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente cliente) {
                return cliente == null ? null : cliente.getNombre();
            }

            @Override
            public Cliente fromString(String string) {
                // Implementar si se permite agregar nuevos clientes
                return null;
            }
        });

        txtMatricula.textProperty().addListener((obs, oldVal, newVal) -> actualizarCoste());
        cbModelo.valueProperty().addListener((obs, oldVal, newVal) -> actualizarCoste());
        cbCliente.valueProperty().addListener((obs, oldVal, newVal) -> actualizarCoste());
        dtEntrada.valueProperty().addListener((obs, oldVal, newVal) -> actualizarCoste());
        dtSalida.valueProperty().addListener((obs, oldVal, newVal) -> actualizarCoste());
        Eleccion.selectedToggleProperty().addListener((obs, oldVal, newVal) -> actualizarCoste());

        cMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        cModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        cEntrada.setCellValueFactory(new PropertyValueFactory<>("entrada"));
        cSalida.setCellValueFactory(new PropertyValueFactory<>("salida"));
        cCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        cTarifa.setCellValueFactory(new PropertyValueFactory<>("tarifa"));
        cCoste.setCellValueFactory(new PropertyValueFactory<>("coste"));

        lbFooter.setOnMouseClicked(event -> mostrarInfoAlumno());
    }

    private void actualizarCoste() {
        if (dtEntrada.getValue() != null && dtSalida.getValue() != null &&
                (rbStandard.isSelected() || rbOferta.isSelected() || rbLargaDuracion.isSelected())) {
            long dias = ChronoUnit.DAYS.between(dtEntrada.getValue(), dtSalida.getValue());
            if (dias >= 0) {
                double tarifa = rbStandard.isSelected() ? 8 : rbOferta.isSelected() ? 6 : 2;
                double coste = tarifa * dias;
                lbCoste.setText(String.format("%.2f €", coste));
            } else {
                lbCoste.setText("");
            }
        } else {
            lbCoste.setText("");
        }
    }

    @FXML
        public void añadir(ActionEvent actionEvent) {
            if (txtMatricula.getText().isEmpty() ||
                    cbModelo.getValue() == null ||
                    cbCliente.getValue() == null ||
                (!rbStandard.isSelected() && !rbOferta.isSelected() && !rbLargaDuracion.isSelected()) ||
                dtEntrada.getValue() == null ||
                dtSalida.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Datos Incompletos");
            alert.setHeaderText("Faltan Datos");
            alert.setContentText("Por favor, completa todos los campos requeridos.");
            alert.showAndWait();
            return;
        }

        long dias = ChronoUnit.DAYS.between(dtEntrada.getValue(), dtSalida.getValue());
        if (dias < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error en las Fechas");
            alert.setHeaderText("Fechas Incorrectas");
            alert.setContentText("La fecha de salida debe ser posterior a la fecha de entrada.");
            alert.showAndWait();
            return;
        }
        Cliente clienteSeleccionado = cbCliente.getValue();
        String nombreCliente = clienteSeleccionado != null ? clienteSeleccionado.getNombre() : "";

        double tarifa = rbStandard.isSelected() ? 8 : rbOferta.isSelected() ? 6 : 2;
        double coste = tarifa * dias;

        lbCoste.setText(String.format("%.2f €", coste));

        String tarifaSeleccionada = rbStandard.isSelected() ? "Standard" : rbOferta.isSelected() ? "Oferta" : "Larga Duración";
        EntradaParking nuevaEntrada = new EntradaParking(
                txtMatricula.getText(),
                cbModelo.getValue(),
                nombreCliente,
                tarifaSeleccionada,
                dtEntrada.getValue(),
                dtSalida.getValue(),
                coste
        );

        tableView.getItems().add(nuevaEntrada);

        // Vaciar todos los campos después de añadir
        txtMatricula.clear();
        cbModelo.getSelectionModel().clearSelection();
        cbModelo.getEditor().clear(); // Si el ComboBox es editable
        cbCliente.getSelectionModel().clearSelection();
        rbStandard.setSelected(false);
        rbOferta.setSelected(false);
        rbLargaDuracion.setSelected(false);
        dtEntrada.setValue(null);
        dtSalida.setValue(null);
        lbCoste.setText("");
    }

    @FXML
    public void salir(ActionEvent actionEvent) {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
    }

    private void mostrarInfoAlumno() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información del Alumno");
        alert.setHeaderText("Nombre del Alumno y Ciclo");
        alert.setContentText("Nombre: José Miguel Ruiz Guevara\nCiclo: 2ºDAM");
        alert.showAndWait();
    }
}