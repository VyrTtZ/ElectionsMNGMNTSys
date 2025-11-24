package electionmngmntsys.mlinkedlist;
import electionmngmntsys.mlinkedlist.mNodeL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class mLinkedListTest {
    private mLinkedList<String> linkedList;
    private mNodeL<String> first;
    private mNodeL<String> second;
    private mNodeL<String> third;

    @BeforeEach
    void setUp() {
        linkedList = new mLinkedList<>();
    }

   //--------------------------------------------------------------------------------
   @Test
    void addNode() {
        assertNull(linkedList.getNode(0)); //Empty list
        linkedList.addNode(first);
        assertEquals("First", linkedList.getNode(0).getContent()); //First
        assertEquals(1, linkedList.size());

        linkedList.addNode(second);
        assertEquals("Second", linkedList.getNode(1).getContent()); //Already has something
        assertEquals(2, linkedList.size());

        linkedList.addNode( null);
        assertNull(linkedList.getNode(2)); //Doesn't add null
        assertEquals(2, linkedList.size());

        linkedList.addNode(third);
        assertEquals("Third", linkedList.getNode(2).getContent());
        assertEquals(3, linkedList.size());

        assertEquals(linkedList.getNode(0).getNext().getContent(), second.getContent()); //Checks order
        assertEquals(linkedList.getNode(1).getPrev().getContent(), first.getContent());
        assertEquals(linkedList.getNode(1).getNext().getContent(), third.getContent());
        assertEquals(linkedList.getNode(2).getPrev().getContent(), second.getContent());

        assertNull(linkedList.getNode(3)); //Non-existent elements
        assertNull(linkedList.getNode(-1));

    }

    //--------------------------------------------------------------------------------------------
    @Test
    void add() {
    }
   //----------------------------------------------------------------------------------------------
    @Test
    void remove() {
        assertTrue(linkedList.isEmpty());//Empty list
        linkedList.addNode(first);
        linkedList.addNode(second);
        linkedList.addNode(third);

        linkedList.remove(String.valueOf(second)); //Middle node
        assertEquals(2, linkedList.size());
        assertEquals(first.getContent(), linkedList.searchNode(third).getPrev().getContent());
        assertEquals(third.getContent(), linkedList.searchNode(first).getNext().getContent());
        assertEquals(third.getContent(), linkedList.getNode(1).getContent());

        linkedList.remove(String.valueOf(first)); //Head node
        assertEquals(1, linkedList.size());
        assertEquals(third.getContent(), linkedList.getNode(0).getContent());

        linkedList.remove(String.valueOf(second)); //Non-existent node

        linkedList.remove(String.valueOf(third)); //Head and last node
        assertEquals(0, linkedList.size());
        assertNull(linkedList.getNode(0));
    }
    //----------------------------------------------------------------------------------------------
    @Test
    void swapNodes() {
    }
    //----------------------------------------------------------------------------------------------
    @Test
    void testSwapNodes() {
    }
    //-----------------------------------------------------------------------------------------------
}