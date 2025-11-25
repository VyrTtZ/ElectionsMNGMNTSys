import electionmngmntsys.mhashmap.mHashMap;
import electionmngmntsys.mhashmap.mNodeH;
import electionmngmntsys.models.Candidate;
import electionmngmntsys.models.Election;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class mHashMapTest {


    @BeforeEach
    void setUp() {


    }


    @Test
    void putNode() {
        mHashMap<String, Integer> hashMap = new mHashMap<>();

        hashMap.put("Catherine Connolly", 1);
        assertEquals(Integer.valueOf(1), hashMap.getValue("Catherine Connolly"));

        hashMap.put("Catherine Connolly", 2);
        assertEquals(Integer.valueOf(2), hashMap.getValue("Catherine Connolly"));

        // nothing happens essentially
        hashMap.put(null, 999);
        assertEquals(Integer.valueOf(2), hashMap.getValue("Catherine Connolly"));

        // resizes the map
        for (int i = 0; i < 20; i++) {
            hashMap.put("Catherine Connolly" + i, i * 10);
        }
        assertTrue(hashMap.sizeOfHashMap() >= 20);
        // lol wrote that twice from brain fog
      //  assertEquals(Integer.valueOf(2), hashMap.getValue("Catherine Connolly"));
        assertEquals(Integer.valueOf(2), hashMap.getValue("Catherine Connolly"));
    }

    @Test
    void get() {
        mHashMap<Election, String> hashMap = new mHashMap<>();
        // 3 = type (presidential)
        Election original = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        original.setId();

        hashMap.put(original, "Active");

        Election duplicate = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        duplicate.setId();

        assertNotSame(original, duplicate);
        assertNotEquals(original, duplicate);

        mNodeH<Election, String> findtheelusivemf = hashMap.get(duplicate);

        assertNotNull(findtheelusivemf);
        //
        assertEquals("Active", findtheelusivemf.getValue());
        assertEquals("Active", hashMap.get(duplicate));

        assertNull(hashMap.get(null));
    }


    @Test
    void containsKey() {
        mHashMap<Election, String> hashMap = new mHashMap<>();
        Election electionn = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        electionn.setId();

        Election electionnn = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        electionn.setId();

        hashMap.put(electionn, "Active");

        assertNotSame(electionn, electionnn);

        assertTrue(hashMap.containsKey(electionn));
        assertTrue(hashMap.containsKey(electionnn));

        Election thewrongkey = new Election("Local / General Election", 1, "Ireland", LocalDate.of(2025, 1, 1), 174);
        assertFalse(hashMap.containsKey(thewrongkey));

        assertFalse(hashMap.containsKey(null));

        for (int i = 0; i < 20; i++) {
            hashMap.put(new Election("Election " + i, 1, "Ireland", LocalDate.of(2025, 1, 1), 174), "Active");
        }
        assertTrue(hashMap.containsKey(thewrongkey));
    }

    @Test
    void containsValue() {
        //
        mHashMap<Election, String> statusOfElections = new mHashMap<>();

        Election presidentialIreland2025 = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        presidentialIreland2025.setId();

        statusOfElections.put(presidentialIreland2025, "Completed");
        assertEquals(1, statusOfElections.size());
        assertTrue(statusOfElections.containsValue("Completed"));
        assertEquals("Completed", statusOfElections.get(presidentialIreland2025).getValue());

        // different object, same data. tests strength of key distinction
        Election weAreNotTheKeyYourLookingFor = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        weAreNotTheKeyYourLookingFor.setId();

        assertNotSame(presidentialIreland2025, weAreNotTheKeyYourLookingFor);
        assertNotEquals(presidentialIreland2025, weAreNotTheKeyYourLookingFor);

        assertTrue(statusOfElections.containsKey(weAreNotTheKeyYourLookingFor));
        //assertNotNull(statusOfElections.get(weAreNotTheKeyYourLookingFor));
        //assertEquals("Completed", statusOfElections.get(weAreNotTheKeyYourLookingFor)); ?

        statusOfElections.put(weAreNotTheKeyYourLookingFor, "Completed");
        //assertEquals("Completed", statusOfElections.get(weAreNotTheKeyYourLookingFor)); //wont work, cant have a duplicate key, the key is presidential Ireland
        assertEquals("Completed", statusOfElections.get(presidentialIreland2025).getValue());

        mHashMap<Candidate, Integer> candidateVotes = new mHashMap<>();

        Candidate catherine = new Candidate("Catherine Connolly", LocalDate.of(1957, 7, 12), "Unaffiliated", "Galway", "smthn", presidentialIreland2025, 1);
        Candidate catherinee = new Candidate("Catherine Connolly", LocalDate.of(1957, 7, 12), "Unaffiliated", "Galway", "Screenshot 2025-11-25 141737.png", presidentialIreland2025, 20);

        candidateVotes.put(catherine, 1);
        assertEquals(Integer.valueOf(1), candidateVotes.get(catherinee));

        for (int i = 0; i < 30; i++) {
            Election e = new Election("Presidential" + i, 3, "Ireland", LocalDate.now(), 1);

            statusOfElections.put(e, "Completed");
        }

        assertTrue(statusOfElections.containsKey(weAreNotTheKeyYourLookingFor));
    }

    @Test
    void remove() {
        mHashMap<Election, String> hashMap = new mHashMap<>();

        Election ee = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        ee.setId();

        Election eee = new Election("General Election", 1, "Kilmaccow", LocalDate.of(2026, 3, 15), 1);
        eee.setId();

        hashMap.put(ee, "Completed");
        hashMap.put(eee, "Ahead");

        assertEquals(2, hashMap.size());
        assertTrue(hashMap.containsKey(ee));
        assertTrue(hashMap.containsKey(eee));

        Election eeCulprit = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        eeCulprit.setId();

        Election eeeCulprit = new Election("General Election", 1, "Ireland", LocalDate.of(2026, 3, 15), 1);
        eeeCulprit.setId();

        assertTrue(hashMap.containsKey(eeCulprit));
        assertEquals(1, hashMap.size());

        assertFalse(hashMap.containsKey(ee));
        assertFalse(hashMap.containsKey(eeCulprit));
        assertNull(hashMap.get(eeCulprit));

        ////boo////

       // assertTrue(hashMap.remove(eee));
        assertEquals(0, hashMap.size());
        assertTrue(hashMap.size() == 0);

        for(int i = 0; i < 20; i++) {
            Election resizer = new Election("Presidential " + i, 3, "Ireland", LocalDate.now(), 1);
            hashMap.put(resizer, "Presidential");
        }
        assertEquals(20, hashMap.size());

        Election resizerCulprit = new Election("Presidential", 3, "Ireland", LocalDate.now(), 1);
        //assertTrue(hashMap.remove(resizerCulprit));
        assertEquals(19, hashMap.size());
    }

    @Test
    void swapValues() {
        mHashMap<Election, String> hashMap = new mHashMap<>();

        Election irishGeneralElection = new Election("General Election", 1, "Ireland", LocalDate.of(2026,3, 15), 1);
        irishGeneralElection.setId();

        Election irishPrezzieElection = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        irishPrezzieElection.setId();

        hashMap.put(irishGeneralElection, "Ahead");
        hashMap.put(irishPrezzieElection, "Completed");

        mNodeH<Election, String> nodeIrishGeneral = hashMap.get(irishGeneralElection);
        mNodeH<Election, String> nodeIrishPrezzie = hashMap.get(irishPrezzieElection);

        assertNotNull(nodeIrishGeneral);
        assertNotNull(nodeIrishPrezzie);
        assertEquals("Ahead", nodeIrishGeneral.getValue());
        assertEquals("Completed", nodeIrishPrezzie.getValue());

        hashMap.swapValues(nodeIrishGeneral, nodeIrishPrezzie);

        assertEquals("Completed", nodeIrishPrezzie.getValue());
        assertEquals("Ahead", nodeIrishGeneral.getValue());

        Election IrishGeneralElectCulprit = new Election("General Election", 1, "Ireland", LocalDate.of(2026, 3, 15), 1);
        IrishGeneralElectCulprit.setId();

        assertEquals("Ahead", hashMap.get(IrishGeneralElectCulprit));

        hashMap.swapValues(nodeIrishGeneral, nodeIrishPrezzie);
        assertEquals("Ahead", hashMap.get(irishGeneralElection));
        assertEquals("Completed", hashMap.get(irishPrezzieElection));
    }
}