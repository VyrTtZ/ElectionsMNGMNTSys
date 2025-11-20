package electionmngmntsys;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;


public class electionListPage {

    //
    public Button electionTab, politicianTab;
    public ChoiceBox <String> electionFilterType, electionSort;
    public TextField electionSearchBar, electionFilterContent;
    public Canvas electionCanvas;

    @FXML
    public void initialize() {
        electionFilterType.getItems().addAll("Type", "Year", "Location");
        electionFilterType.setValue("Type");

        electionSort.getItems().addAll("Name Ascending", "Type Ascending", "Year Ascending", "Location Ascending", "Name Descending", "Type Descending", "Year Descending", "Location Descending");
        electionSort.setValue("Name Ascending");
    }
}
