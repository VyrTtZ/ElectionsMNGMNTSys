module main.java.com.example.electionmngmntsys {
    requires javafx.controls;
    requires javafx.fxml;


    opens main.java.com.example.electionmngmntsys to javafx.fxml;
    exports main.java.com.example.electionmngmntsys;
}