module com.nedvedd.projectq {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.nedvedd.projectq to javafx.fxml;
    exports com.nedvedd.projectq;
    exports com.nedvedd.projectq.data;
    opens com.nedvedd.projectq.data to javafx.fxml;
}