package electionmngmntsys.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Politician implements Serializable {
    private String name;
    private LocalDate dateOfBirth; //Format dd:mm:yyyy
    private String party;
    private String homeCounty;
    private String imageURL;

    public Politician(String name, LocalDate dateOfBirth, String party, String homeCounty, String imageURL) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.party = party;
        this.homeCounty = homeCounty;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getHomeCounty() {
        return homeCounty;
    }

    public void setHomeCounty(String homeCounty) {
        this.homeCounty = homeCounty;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
