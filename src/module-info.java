module main.electionmngmntsys {
    requires javafx.controls;
    requires javafx.fxml;


    opens main.electionmngmntsys to javafx.fxml;
    exports main.electionmngmntsys;
}