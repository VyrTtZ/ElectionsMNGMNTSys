package electionmngmntsys.models;

import java.time.LocalDate;

public class Politician {
    private String name;
    private LocalDate dateOfBirth; //Format dd:mm:yyyy
    private String party;
    private String homeCountry;
    private String imageURL;

    public Politician(String name, LocalDate dateOfBirth, String party, String homeCountry, String imageURL) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.party = party;
        this.homeCountry = homeCountry;
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

    public String getHomeCountry() {
        return homeCountry;
    }

    public void setHomeCountry(String homeCountry) {
        this.homeCountry = homeCountry;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
