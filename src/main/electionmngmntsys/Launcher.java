package main.electionmngmntsys;

import main.electionmngmntsys.mhashmap.mHashMap;
import main.electionmngmntsys.mlinkedlist.mLinkedList;
import main.electionmngmntsys.models.Candidate;
import main.electionmngmntsys.models.Election;

import java.time.LocalDate;
import java.util.Date;

public class Launcher {
    public static void main(String[] args) {
        mHashMap<String, String> bee = new mHashMap<>();//testone
        bee.put("lmao", "lmae");
        bee.put("lma1", "lmaasdfksadhfkasjd");
        System.out.println(bee.get("lmao").getValue());
        System.out.println(bee.get("lma1").getValue());

        Candidate c = new Candidate("test", LocalDate.of(2024, 12, 12), "testparty",
                "MOLDOVA", "testImg.com", new Election(1, "Bratislava",LocalDate.of(2024, 12, 12), 300,  new mLinkedList<>()), 40);

        System.out.println(c.toString());
        System.out.println(c.getElection().getYearDate());
    }
}
