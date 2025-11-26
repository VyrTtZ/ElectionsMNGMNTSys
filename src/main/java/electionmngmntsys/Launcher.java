package electionmngmntsys;

import electionmngmntsys.controllers.ElectionEdit;
import electionmngmntsys.controllers.ListPage;
import electionmngmntsys.controllers.Individual;
import electionmngmntsys.controllers.PoliticianEdit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public class Launcher extends Application {
    public Scene electionList, electionEdit, politicianEdit, individual;
    public static Stage stage;
    public ElectionEdit electionEditController;
    public ListPage listPageController;
    public PoliticianEdit politicianEditController;
    public Individual individualController;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader electionListLoader = new FXMLLoader(Launcher.class.getResource("/listPage.fxml"));
        FXMLLoader electionEditLoader = new FXMLLoader(Launcher.class.getResource("/electionEdit.fxml"));
        FXMLLoader politicianEditLoader = new FXMLLoader(Launcher.class.getResource("/politicianEdit.fxml"));
        FXMLLoader individualLoader=new FXMLLoader(Launcher.class.getResource("/individualPage.fxml"));

        Parent electionListParent = electionListLoader.load();
        Parent electionEditParent = electionEditLoader.load();
        Parent politicianEditParent = politicianEditLoader.load();
        Parent individualParent=individualLoader.load();

        electionEditController = electionEditLoader.getController();
        listPageController = electionListLoader.getController();
        politicianEditController = politicianEditLoader.getController();
        individualController=individualLoader.getController();

        electionEditController.setLauncher(this);
        electionEditController.setElectionListPage(listPageController);
        listPageController.setLauncher(this);
        listPageController.setElectionEdit(electionEditController);
        listPageController.setPoliticianEdit(politicianEditController);
        listPageController.setIndividual(individualController);
        politicianEditController.setLauncher(this);
        politicianEditController.setElectionListPage(listPageController);
        individualController.setLauncher(this);
        individualController.setElectionListPage(listPageController);
        individualController.setPoliticianEdit(politicianEditController);
        individualController.setElectionEdit(electionEditController);

        electionList = new Scene(electionListParent, 1920, 1080);
        electionEdit = new Scene(electionEditParent, 1920, 1080);
        politicianEdit = new Scene(politicianEditParent, 1920, 1080);
        individual=new Scene(individualParent, 1920, 1080);
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
                listPageController.draw();
                stage.setFullScreen(true);
                break;
            case "politicianForm":
                stage.setScene(politicianEdit);
                stage.setTitle("Politician Form");
                stage.setFullScreen(true);
                break;
            case "individual":
                stage.setScene(individual);
                stage.setTitle("Individual Viewing");
                stage.setFullScreen(true);
                break;
        }
    }
}
