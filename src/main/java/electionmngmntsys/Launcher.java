package electionmngmntsys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import static javafx.application.Application.launch;

public class Launcher extends Application {
    public Scene electionList;
    public static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader electionListLoader = new FXMLLoader(Launcher.class.getResource("/electionListPage.fxml"));
        Parent electionListParent = electionListLoader.load();
        Launcher.stage = stage;
        stage.setTitle("Election List");
        electionList = new Scene(electionListParent);
        stage.setScene(electionList);
        stage.setFullScreen(true);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
