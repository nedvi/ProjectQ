module com.nedvedd.projectq {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.nedvedd.projectq to javafx.fxml;
    exports com.nedvedd.projectq;

    opens com.nedvedd.projectq.model to javafx.fxml;
    exports com.nedvedd.projectq.model;

    exports com.nedvedd.projectq.controller;
    opens com.nedvedd.projectq.controller to javafx.fxml;
    exports com.nedvedd.projectq.utillities;
    opens com.nedvedd.projectq.utillities to javafx.fxml;
}