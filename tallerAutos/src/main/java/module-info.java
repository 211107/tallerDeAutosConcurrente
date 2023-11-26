module com.example.tallerautos.tallerautos {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tallerautos.tallerautos to javafx.fxml;
    exports com.example.tallerautos.tallerautos;
    exports com.example.tallerautos.tallerautos.Controllers;
    opens com.example.tallerautos.tallerautos.Controllers to javafx.fxml;
}