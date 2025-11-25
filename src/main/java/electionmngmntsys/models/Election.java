package electionmngmntsys.models;

import electionmngmntsys.Utilities;
import electionmngmntsys.mhashmap.mHashMap;
import electionmngmntsys.mlinkedlist.mLinkedList;

import java.io.Serializable;
import java.time.LocalDate;
public class Election implements Serializable {
    private int type; //Local = 1; Euro = 2; Presidential = 3;
    private String location, name;
    private LocalDate yearDate;
    private int numOfWinners;
    private mLinkedList<Candidate> candidates = new mLinkedList<>();
    private mLinkedList<Politician> politicians = new mLinkedList<>();
    private static int id = 0;
    private mHashMap<Politician, Integer> duplicateCheck = new mHashMap<>();



    public Election(String name, int type, String location, LocalDate yearDate, int numOfWinners) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.yearDate = yearDate;
        this.numOfWinners = numOfWinners;
        this.candidates=new mLinkedList<>();
        this.politicians = new mLinkedList<>();
        setId();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getYearDate() {
        return yearDate;
    }

    public void setYearDate(LocalDate yearDate) {
        this.yearDate = yearDate;
    }

    public int getNumOfWinners() {
        return numOfWinners;
    }

    public void setNumOfWinners(int numOfWinners) {
        this.numOfWinners = numOfWinners;
    }

    public mLinkedList<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(mLinkedList<Candidate> candidates) {
        this.candidates = candidates;
    }

    public mLinkedList<Politician> getPoliticians() {
        return politicians;
    }

    public void setPoliticians(mLinkedList<Politician> politicians) {
        this.politicians = politicians;
    }


    public int getId() {
        return id;
    }

    public void setId() {
        id++;
        this.id =id;

    }

    public mHashMap<Politician, Integer> getDuplicateCheck() {
        return duplicateCheck;
    }

    public void setDuplicateCheck(mHashMap<Politician, Integer> duplicateCheck) {
        this.duplicateCheck = duplicateCheck;
    }

    public String toString()
    {
        return Utilities.electionTypeReverseMap.get(type).getValue()+" election "+name+", held in "+location+", on "+yearDate+", "+numOfWinners+" winners";
    }
}
