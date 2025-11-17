package main.electionmngmntsys.models;

import java.io.Serializable;
import java.time.LocalDate;


public class Candidate extends Politician implements Serializable{
    private Election election;
    private int votes;

    public Candidate(String name, LocalDate dateOfBirth, String party, String homeCountry, String imageURL , Election election, int votes) {
        super(name, dateOfBirth, party, homeCountry, imageURL);
        this.election = election;
        this.votes = votes;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "election=" + election +
                ", votes=" + votes +
                '}';
    }
}
