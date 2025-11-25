package electionmngmntsys.models;

import java.io.Serializable;
import java.time.LocalDate;


public class Candidate extends Politician implements Serializable{
    private Election election;
    private int votes;

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
        return "Candidate "+ getName() +" representing "+getParty()+ ", from "+getHomeCounty()+", born on "+getDateOfBirth()+
                "; GOT " + votes +
                " votes";
    }
}
