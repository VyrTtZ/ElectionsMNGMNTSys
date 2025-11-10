module com.example.electionmngmntsys {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.electionmngmntsys to javafx.fxml;
    exports com.example.electionmngmntsys;
}