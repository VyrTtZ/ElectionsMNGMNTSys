package electionmngmntsys.models;

import electionmngmntsys.mhashmap.mHashMap;
import electionmngmntsys.mlinkedlist.mLinkedList;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

public class Politician implements Serializable {
    private String name;
    private LocalDate dateOfBirth; //Format dd:mm:yyyy
    private String party;
    private String homeCounty;
    private String imageURL;
    private mLinkedList<mLinkedList<Integer>> votesList = new mLinkedList<>(); //for votes and elections (id)
    private mLinkedList<Election> electionList = new mLinkedList<>();
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

    public mLinkedList<mLinkedList<Integer>> getVotesList() {
        return votesList;
    }

    public void setVotesList(mLinkedList<mLinkedList<Integer>> votes) {
        this.votesList = votes;
    }

    public mLinkedList<String> getAssociations() {
        return associations;
    }

    public void setAssociations(mLinkedList<String> associations) {
        this.associations = associations;
    }

    public mHashMap<Election, Integer> getDuplicateCheck() {
        return duplicateCheck;
    }

    public void setDuplicateCheck(mHashMap<Election, Integer> duplicateCheck) {
        this.duplicateCheck = duplicateCheck;
    }

    public mLinkedList<Election> getElectionList() {
        return electionList;
    }

    public void setElectionList(mLinkedList<Election> electionList) {
        this.electionList = electionList;
    }
}
