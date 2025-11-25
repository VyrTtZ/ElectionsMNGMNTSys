module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.naming;


    opens electionmngmntsys to javafx.fxml;
    exports electionmngmntsys.mlinkedlist;
    exports electionmngmntsys.mhashmap;
    exports electionmngmntsys.controllers;
    exports electionmngmntsys.models;
    exports electionmngmntsys;
    opens electionmngmntsys.controllers to javafx.fxml;
}