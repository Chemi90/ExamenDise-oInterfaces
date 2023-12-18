module com.example.examendiseniointerfaces {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;


    opens com.example.examendiseniointerfaces to javafx.fxml;
    exports com.example.examendiseniointerfaces;
}