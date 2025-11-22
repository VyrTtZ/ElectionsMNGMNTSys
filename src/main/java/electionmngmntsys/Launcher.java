package electionmngmntsys;

import electionmngmntsys.mhashmap.mHashMap;
import electionmngmntsys.models.Election;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import static javafx.application.Application.launch;

public class Launcher extends Application {
    public Scene electionList, electionEdit;
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
        electionEditController.setElectionListPage(electionListPageController);
        electionListPageController.setLauncher(this);
        electionListPageController.setElectionEdit(electionEditController);

        electionList = new Scene(electionListParent, 1920, 1080);
        electionEdit = new Scene(electionEditParent, 1920, 1080);
        Launcher.stage = stage;
        stage.setTitle("Election List");
        stage.setFullScreenExitHint("");
        stage.setScene(electionList);
        stage.setFullScreen(true);
        stage.show();
    }
    public static void main(String[] args) {
        mHashMap test = new mHashMap();
        test.put("test", "test");
        System.out.println(test.containsKey("tet"));
        launch();


    }
    public void switchScene(String dest)
    {
        switch (dest)
        {
            case "electionForm":
                stage.setScene(electionEdit);
                stage.setTitle("Election Form");
                stage.setFullScreen(true);
                break;
            case "electionList":
                stage.setScene(electionList);
                stage.setTitle("Election List");
                electionListPageController.draw();
                stage.setFullScreen(true);
                break;
        }
    }
}
