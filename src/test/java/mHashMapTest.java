import electionmngmntsys.mhashmap.mHashMap;
import electionmngmntsys.mhashmap.mNodeH;
import electionmngmntsys.models.Candidate;
import electionmngmntsys.models.Election;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class mHashMapTest {
    private mHashMap<String, Integer> hashMapS;
    private mHashMap<Election, String> hashMapE;
    private mHashMap <Election, String> statusOfElections;


    @BeforeEach
    void setUp() {
        hashMapS= new mHashMap<>();
        hashMapE = new mHashMap<>();
        statusOfElections = new mHashMap<>();
    }


    @Test
    void putNode() {
        hashMapS.put("Catherine Connolly", 1);
        assertEquals(Integer.valueOf(1), hashMapS.getValue("Catherine Connolly"));

        hashMapS.put("Catherine Connolly", 2);
        assertEquals(Integer.valueOf(2), hashMapS.getValue("Catherine Connolly"));

        // nothing happens essentially
        hashMapS.put(null, 999);
        assertEquals(Integer.valueOf(2), hashMapS.getValue("Catherine Connolly"));

        // resizes the map
        for (int i = 0; i < 20; i++) {
            hashMapS.put("Catherine Connolly" + i, i * 10);
        }
        assertTrue(hashMapS.size() >= 20);
        // lol wrote that twice from brain fog
      //  assertEquals(Integer.valueOf(2), hashMap.getValue("Catherine Connolly"));
        assertEquals(Integer.valueOf(2), hashMapS.getValue("Catherine Connolly"));
    }

    @Test
    void get() {
        // 3 = type (presidential)
        Election original = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        original.setId(0);

        hashMapE.put(original, "Active");

        Election duplicate = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        duplicate.setId(0);

        assertNotSame(original, duplicate);


        mNodeH<Election, String> findtheelusivemf = hashMapE.get(duplicate);

        assertNotNull(findtheelusivemf);
        //
        assertEquals("Active", findtheelusivemf.getValue());
        assertEquals("Active", hashMapE.get(duplicate).getValue());

        assertNull(hashMapE.get(null));
    }


    @Test
    void containsKey() {
        mHashMap<Election, String> hashMap = new mHashMap<>();
        // move to setup
        Election electionn = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        electionn.setId(0);

        Election electionnn = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        electionn.setId(0);

        hashMapE.put(electionn, "Active");

        assertNotSame(electionn, electionnn);

        assertTrue(hashMapE.containsKey(electionn));
        assertFalse(hashMapE.containsKey(electionnn));

        Election thewrongkey = new Election("Local / General Election", 1, "Ireland", LocalDate.of(2025, 1, 1), 174);
        assertFalse(hashMapE.containsKey(thewrongkey));
        assertFalse(hashMapE.containsKey(null));

        for (int i = 0; i < 20; i++) {
            hashMapE.put(new Election("Election " + i, 1, "Ireland", LocalDate.of(2025, 1, 1), 174), "Active");
        }
        assertFalse(hashMap.containsKey(thewrongkey));
    }


    @Test
    void containsValue() {
        Election presidentialIreland2025 = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        presidentialIreland2025.setId(0);

        statusOfElections.put(presidentialIreland2025, "Completed");
        assertEquals(1, statusOfElections.size());
        assertTrue(statusOfElections.containsValue("Completed"));
        assertEquals("Completed", statusOfElections.get(presidentialIreland2025).getValue());

        // different object, same data. tests strength of key distinction
        Election weAreNotTheKeyYourLookingFor = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        weAreNotTheKeyYourLookingFor.setId(0);

       // assertNotSame(presidentialIreland2025, weAreNotTheKeyYourLookingFor);
      //  assertNotEquals(presidentialIreland2025, weAreNotTheKeyYourLookingFor);

        assertFalse(statusOfElections.containsKey(weAreNotTheKeyYourLookingFor));
        //assertNotNull(statusOfElections.get(weAreNotTheKeyYourLookingFor));
        //assertEquals("Completed", statusOfElections.get(weAreNotTheKeyYourLookingFor));

        statusOfElections.put(weAreNotTheKeyYourLookingFor, "Completed");
        //assertEquals("Completed", statusOfElections.get(weAreNotTheKeyYourLookingFor)); //wont work, cant have a duplicate key, the key is presidential Ireland
        assertEquals("Completed", statusOfElections.get(presidentialIreland2025).getValue());

        mHashMap<Candidate, Integer> candidateVotes = new mHashMap<>();

        Candidate catherine = new Candidate("Catherine Connolly", LocalDate.of(1957, 7, 12), "Unaffiliated", "Galway", "smthn", presidentialIreland2025, 1);

        candidateVotes.put(catherine, 1);
        assertEquals(1, candidateVotes.get(catherine).getValue());

        for (int i = 0; i < 30; i++) {
            Election e = new Election("Presidential" + i, 3, "Ireland", LocalDate.now(), 1);


            statusOfElections.put(e, "Completed");
        }

        assertTrue(statusOfElections.containsKey(weAreNotTheKeyYourLookingFor));
    }



    @Test
    void remove() {
        Election ee = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        ee.setId(0);

        Election eee = new Election("General Election", 1, "Kilmaccow", LocalDate.of(2026, 3, 15), 1);
        eee.setId(0);

        hashMapE.put(ee, "Completed");
        hashMapE.put(eee, "Ahead");

        assertEquals(2, hashMapE.size());
        assertTrue(hashMapE.containsKey(ee));
        assertTrue(hashMapE.containsKey(eee));

        Election eeCulprit = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        eeCulprit.setId(0);

        Election eeeCulprit = new Election("General Election", 1, "Ireland", LocalDate.of(2026, 3, 15), 1);
        eeeCulprit.setId(0);

        // assertTrue(hashMapE.containsKey(eeCulprit));
         assertEquals(2, hashMapE.size());

        assertTrue(hashMapE.containsKey(ee));
        assertFalse(hashMapE.containsKey(eeCulprit));
       //  assertNull(hashMapE.get(eeCulprit).getValue());

        hashMapE.remove(ee);
        hashMapE.remove(eee);

         // assertTrue(hashMap.remove(eee));
        assertEquals(0, hashMapE.size());
        assertTrue(hashMapE.size() == 0);

        for(int i = 0; i < 20; i++) {
            Election resizer = new Election("Presidential " + i, 3, "Ireland", LocalDate.now(), 1);
            hashMapE.put(resizer, "Presidential");
        }

        assertEquals(20, hashMapE.size());

        Election resizerCulprit = new Election("Presidential", 3, "Ireland", LocalDate.now(), 1);
        //assertTrue(hashMap.remove(resizerCulprit));
        assertEquals(20, hashMapE.size());
    }

    @Test
    void swapValues() {

        Election irishGeneralElection = new Election("General Election", 1, "Ireland", LocalDate.of(2026,3, 15), 1);
        irishGeneralElection.setId(0);

        Election irishPrezzieElection = new Election("Presidential", 3, "Ireland", LocalDate.of(2025, 1, 1), 1);
        irishPrezzieElection.setId(0);

        hashMapE.put(irishGeneralElection, "Ahead");
        hashMapE.put(irishPrezzieElection, "Completed");

        mNodeH<Election, String> nodeIrishGeneral = hashMapE.get(irishGeneralElection);
        mNodeH<Election, String> nodeIrishPrezzie = hashMapE.get(irishPrezzieElection);

       // assertNotNull(nodeIrishGeneral);
      //  assertNotNull(nodeIrishPrezzie);
        assertEquals("Ahead", nodeIrishGeneral.getValue());
        assertEquals("Completed", nodeIrishPrezzie.getValue());

        hashMapE.swapValues(nodeIrishGeneral, nodeIrishPrezzie);

        assertEquals("Ahead", nodeIrishPrezzie.getValue());
        assertEquals("Completed", nodeIrishGeneral.getValue());

        assertEquals("Completed", hashMapE.getValue(nodeIrishGeneral.getKey()));
        assertEquals("Ahead",     hashMapE.getValue(nodeIrishPrezzie.getKey()));


        hashMapE.swapValues(nodeIrishGeneral, nodeIrishPrezzie);

        assertEquals("Ahead", nodeIrishGeneral.getValue());
        assertEquals("Completed", nodeIrishPrezzie.getValue());
        assertEquals("Ahead",     hashMapE.getValue(irishGeneralElection));
        assertEquals("Completed", hashMapE.getValue(irishPrezzieElection));
    }
}