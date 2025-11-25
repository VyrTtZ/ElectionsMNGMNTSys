package electionmngmntsys.models;

import electionmngmntsys.mhashmap.mHashMap;
import electionmngmntsys.mlinkedlist.mLinkedList;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

public class Politician implements Serializable {
    private String name;
    private LocalDate dateOfBirth; //Format dd:mm:yyyy
    private String party;
    private String homeCounty;
    private String imageURL;
    private mLinkedList<Integer> votesList = new mLinkedList<>(); //for votes
    private mLinkedList<Election> elections = new mLinkedList<>();
    private mLinkedList<String> associations = new mLinkedList<>(); //for party during an election (id)
    private mHashMap<Election, Integer> duplicateCheck = new mHashMap<>();

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

    public mLinkedList<Integer> getVotesList() {
        return votesList;
    }

    public void setVotesList(mLinkedList<Integer> votes) {
        this.votesList = votes;
    }

    public mLinkedList<String> getAssociations() {
        return associations;
    }

    public void setAssociations(mLinkedList<String> associations) {
        this.associations = associations;
    }

    public mLinkedList<Election> getElections() {
        return elections;
    }
    public void setElections(mLinkedList<Election> elections) {
        this.elections = elections;
    }

    public mHashMap<Election, Integer> getDuplicateCheck() {
        return duplicateCheck;
    }

    public void setDuplicateCheck(mHashMap<Election, Integer> duplicateCheck) {
        this.duplicateCheck = duplicateCheck;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Politician that = (Politician) o;
        return Objects.equals(name, that.name) && Objects.equals(dateOfBirth, that.dateOfBirth) && Objects.equals(party, that.party) && Objects.equals(homeCounty, that.homeCounty) && Objects.equals(imageURL, that.imageURL);
    }
}
