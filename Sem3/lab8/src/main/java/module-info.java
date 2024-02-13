module com.example.lab8 {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
                    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.lab8 to javafx.fxml;
    exports com.example.lab8;
    opens com.example.lab8.controller to javafx.fxml;
    exports com.example.lab8.controller;
    exports com.example.lab8.domain;
    exports com.example.lab8.service;
    exports com.example.lab8.utils.events;
    exports com.example.lab8.utils.observer;
    exports com.example.lab8.repo.paging;
    exports com.example.lab8.repo;

}