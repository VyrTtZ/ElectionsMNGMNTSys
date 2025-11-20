module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens electionmngmntsys to javafx.fxml;
    exports electionmngmntsys;
}