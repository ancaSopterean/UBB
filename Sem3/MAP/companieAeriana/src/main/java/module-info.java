module com.example.companieaeriana {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.companieaeriana to javafx.fxml;
    exports com.example.companieaeriana;
    exports com.example.companieaeriana.Domain;
    exports com.example.companieaeriana.Controller;
    opens com.example.companieaeriana.Controller;
    exports com.example.companieaeriana.Service;
    exports com.example.companieaeriana.utils.events;
    exports com.example.companieaeriana.utils.observer;
}