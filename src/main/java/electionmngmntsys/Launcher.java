package electionmngmntsys;

import electionmngmntsys.models.Election;
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
    public ElectionEdit electionEditController;
    public ElectionListPage electionListPageController;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader electionListLoader = new FXMLLoader(Launcher.class.getResource("/electionListPage.fxml"));
        FXMLLoader electionEditLoader = new FXMLLoader(Launcher.class.getResource("/electionEdit.fxml"));

        Parent electionListParent = electionListLoader.load();
        Parent electionEditParent = electionEditLoader.load();

        electionEditController = electionEditLoader.getController();
        electionListPageController = electionListLoader.getController();

        electionEditController.setLauncher(this);
        electionListPageController.setLauncher(this);

        electionList = new Scene(electionListParent);
        Launcher.stage = stage;
        stage.setTitle("Election List");
        stage.setScene(electionList);
        stage.setFullScreen(true);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
