package electionmngmntsys.controllers;

import javafx.scene.control.TextField;

public class PoliticianFilterEdit {
    public TextField includeYearField, excludeYearField, includePartyField, excludePartyField;

    public void setIncludeYearField(String includeYear) {
        includeYearField.setText(includeYear);
    }
    public void setExcludeYearField(String excludeYear) {
        excludeYearField.setText(excludeYear);
    }
    public void setIncludePartyField(String includeParty) {
        includePartyField.setText(includeParty);
    }
    public void setExcludePartyField(String excludeParty) {
        excludePartyField.setText(excludeParty);
    }

    public String getIncludeYearField() {
        return includeYearField.getText();
    }
    public String getExcludeYearField() {
        return excludeYearField.getText();
    }
    public String getIncludePartyField() {
        return includePartyField.getText();
    }
    public String getExcludePartyField() {
        return excludePartyField.getText();
    }
}