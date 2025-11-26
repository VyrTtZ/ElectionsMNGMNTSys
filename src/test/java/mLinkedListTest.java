

import electionmngmntsys.mlinkedlist.mLinkedList;
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
        first = new mNodeL<>("First", second);
        second = new mNodeL<>("Second", third);
        third = new mNodeL<>("Third", null);
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
        assertEquals(linkedList.getNode(1).getNext().getContent(), third.getContent());

        assertNull(linkedList.getNode(3)); //Non-existent elements
        assertNull(linkedList.getNode(-1));

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
        assertTrue(linkedList.isEmpty());

        linkedList.addNode(first);
        linkedList.addNode(second);
        linkedList.addNode(third);

        linkedList.swapNodes(first, second);
        assertEquals("Second", getContentAt(0));
        assertEquals("First", getContentAt(1));
        assertEquals("Third", getContentAt(2));

        linkedList.swapNodes(second, third);
        assertEquals("Third", getContentAt(0));
        assertEquals("First", getContentAt(1));
        assertEquals("Second", getContentAt(2));

        linkedList.swapNodes(third, second);
        assertEquals("Second", getContentAt(0));
        assertEquals("First",  getContentAt(1));
        assertEquals("Third",  getContentAt(2));

        linkedList.swapNodes(first, third);
        assertEquals("Second", getContentAt(0));
        assertEquals("Third",  getContentAt(1));
        assertEquals("First",  getContentAt(2));

        linkedList.swapNodes(third, third);
        assertEquals("Second", getContentAt(0));
        assertEquals("Third",  getContentAt(1));
        assertEquals("First",  getContentAt(2));

        linkedList.swapNodes(null, first);
        assertEquals("Second", getContentAt(0));


        linkedList = new mLinkedList<>();
        linkedList.addNode(first);
        linkedList.swapNodes(first, first);
        assertEquals("First", linkedList.getNode(0).getContent());


        linkedList.addNode(second);
        linkedList.swapNodes(first, second);
        assertEquals("Second", getContentAt(0));
        assertEquals("First",  getContentAt(1));
    }

    private String getContentAt(int index) {
        mNodeL<String> node = linkedList.getNode(index);
        return node != null ? node.getContent() : null;
    }
    //----------------------------------------------------------------------------------------------
    @Test
    void testSwapNodes() {
        linkedList.addNode(first);
        linkedList.addNode(second);
        linkedList.addNode(third);

        linkedList.swapNodes(0, 2);

        assertEquals("Third", linkedList.getNode(0).getContent());
        assertEquals("Second", linkedList.getNode(1).getContent());
        assertEquals("First", linkedList.getNode(2).getContent());

        // previous and next node checks if still correct after index swapping
        assertNull(linkedList.getNode(0).getNext());

    }
    //-----------------------------------------------------------------------------------------------
}