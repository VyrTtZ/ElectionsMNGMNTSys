module electionmngmntsys {
    requires javafx.controls;
    requires javafx.fxml;


    opens electionmngmntsys to javafx.fxml;
    exports electionmngmntsys;
}