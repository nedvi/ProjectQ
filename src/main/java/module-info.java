module com.nedvedd.projectq {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.nedvedd.projectq to javafx.fxml;
    exports com.nedvedd.projectq;

    opens com.nedvedd.projectq.data to javafx.fxml;
    exports com.nedvedd.projectq.data;

    exports com.nedvedd.projectq.controller;
    opens com.nedvedd.projectq.controller to javafx.fxml;
}