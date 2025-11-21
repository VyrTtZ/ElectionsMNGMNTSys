module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.naming;


    opens electionmngmntsys to javafx.fxml;
    exports electionmngmntsys;
}