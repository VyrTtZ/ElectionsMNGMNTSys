package electionmngmntsys;

import electionmngmntsys.mhashmap.mHashMap;
import electionmngmntsys.mlinkedlist.mLinkedList;
import electionmngmntsys.models.Candidate;
import electionmngmntsys.models.Election;
import electionmngmntsys.models.Politician;
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
    public Scene electionList, electionEdit, politicianEdit;
    public static Stage stage;
    public ElectionEdit electionEditController;
    public ElectionListPage electionListPageController;
    public PoliticianEdit politicianEditController;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader electionListLoader = new FXMLLoader(Launcher.class.getResource("/electionListPage.fxml"));
        FXMLLoader electionEditLoader = new FXMLLoader(Launcher.class.getResource("/electionEdit.fxml"));
        FXMLLoader politicianEditLoader = new FXMLLoader(Launcher.class.getResource("/politicianEdit.fxml"));

        Parent electionListParent = electionListLoader.load();
        Parent electionEditParent = electionEditLoader.load();
        Parent politicianEditParent = politicianEditLoader.load();

        electionEditController = electionEditLoader.getController();
        electionListPageController = electionListLoader.getController();
        politicianEditController = politicianEditLoader.getController();

        electionEditController.setLauncher(this);
        electionEditController.setElectionListPage(electionListPageController);
        electionListPageController.setLauncher(this);
        electionListPageController.setElectionEdit(electionEditController);
        electionListPageController.setPoliticianEdit(politicianEditController);
        politicianEditController.setLauncher(this);
        politicianEditController.setElectionListPage(electionListPageController);

        electionList = new Scene(electionListParent, 1920, 1080);
        electionEdit = new Scene(electionEditParent, 1920, 1080);
        politicianEdit = new Scene(politicianEditParent, 1920, 1080);
        Launcher.stage = stage;
        stage.setTitle("Election List");
        stage.setFullScreenExitHint("");
        stage.setScene(electionList);
        stage.setFullScreen(true);
        stage.show();
    }
    public static void main(String[] args) {

//        mLinkedList<String> testList = new mLinkedList<>();
//
//        Politician p = new Politician("testPol", null, null, null, null);
//        Election e = new Election("testE", 0, null,null, 0);
//        Candidate c = new Candidate("test", null, null, null, null, e, 0);
//
//        mHashMap hashMp = new mHashMap();
//        hashMp.put(c, null);
//        hashMp.put(testList, null);
//        hashMp.put(p, null);
//        hashMp.put(e, null);

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
            case "politicianForm":
                stage.setScene(politicianEdit);
                stage.setTitle("Politician Form");
                stage.setFullScreen(true);
                break;
        }
    }
}
