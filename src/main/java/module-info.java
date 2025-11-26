module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.naming;
    requires xstream;


    opens electionmngmntsys to javafx.fxml;
    exports electionmngmntsys.mlinkedlist;
    exports electionmngmntsys.mhashmap;
    exports electionmngmntsys.controllers;
    exports electionmngmntsys.models;
    exports electionmngmntsys;
    opens electionmngmntsys.controllers to javafx.fxml, xstream;
    opens electionmngmntsys.models to xstream;
    opens electionmngmntsys.mhashmap to xstream;
    opens electionmngmntsys.mlinkedlist to xstream;
}