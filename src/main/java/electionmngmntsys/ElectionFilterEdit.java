package electionmngmntsys;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class ElectionFilterEdit {
    public TextField includeYearField, excludeYearField;
    public CheckBox localType, europeanType, presidentialType;

    public void setIncludeYearField(String includeYear) {
        includeYearField.setText(includeYear);
    }
    public void setExcludeYearField(String excludeYear) {
        excludeYearField.setText(excludeYear);
    }
    public void setLocalType(boolean localType) {
        this.localType.setSelected(localType);
    }
    public void setPresidentialType(boolean presidentialType) {
        this.presidentialType.setSelected(presidentialType);
    }
    public void setEuropeanType(boolean europeanType) {
        this.europeanType.setSelected(europeanType);
    }

    public String getIncludeYearField() {
        return includeYearField.getText();
    }
    public String getExcludeYearField() {
        return excludeYearField.getText();
    }
    public boolean getLocalType() {
        return localType.isSelected();
    }
    public boolean getEuropeanType() {
        return europeanType.isSelected();
    }
    public boolean getPresidentialType() {
        return presidentialType.isSelected();
    }
}