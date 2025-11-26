package electionmngmntsys.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


public class Candidate extends Politician implements Serializable{
    private Election election;
    private int votes, ranking;

    public Candidate(String name, LocalDate dateOfBirth, String party, String homeCounty, String imageURL , Election election, int votes) {
        super(name, dateOfBirth, party, homeCounty, imageURL);
        this.election = election;
        this.votes = votes;
    }

    public Candidate(Politician politician, Election election, int votes) {
        super(politician.getName(), politician.getDateOfBirth(), politician.getParty(), politician.getHomeCounty(), politician.getImageURL());
        this.setElection(election);
        this.votes = votes;
    }

    public void set(Candidate candidate) {
        this.setName(candidate.getName());
        this.setDateOfBirth(candidate.getDateOfBirth());
        this.setParty(candidate.getParty());
        this.setHomeCounty(candidate.getHomeCounty());
        this.setImageURL(candidate.getImageURL());
        this.setElection(candidate.getElection());
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

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    @Override
    public String toString() {
        return "Candidate "+ getName() +" representing "+getParty()+ ", from "+getHomeCounty()+", born on "+getDateOfBirth()+
                "; GOT " + votes +
                " votes";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Candidate candidate = (Candidate) o;
        return votes == candidate.votes && ranking == candidate.ranking && Objects.equals(election, candidate.election);
    }
}
