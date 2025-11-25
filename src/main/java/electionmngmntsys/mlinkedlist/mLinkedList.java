package electionmngmntsys.mlinkedlist;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;

//----------------------------------------------------------------------------------------------------------------------
public class mLinkedList<T> implements Iterable<T>, Serializable {
    private mNodeL<T> head;

    //----------------------------------------------------------------------------------------------------------------------
    public mLinkedList() {
        head = null;
    }

    //----------------------------------------------------------------------------------------------------------------------
    public void addNode(mNodeL<T> n) { //ADDS A NODE TO THE BY SETTING THE NEXT OF CURRENT LAST NODE TO THE NEW ONE
        if (head == null) {
            head = n;
        } else {
            mNodeL<T> current = head;
            while (current.next != null)
                current = current.next;
            current.next = n;
        }
    }

    //----------------------------------------------------------------------------------------------------------------------
    public void add(T value) { //HELPER TO THE ADD NODE SO THERE IS NO NEED TO DECLARE A WHOLE NODE
        addNode(new mNodeL<>(value, null, null));
    }

    //----------------------------------------------------------------------------------------------------------------------
    public boolean remove(T item) { //REMOVES A NODE WITH THE SPECIFIED ITEM
        if (head == null) return false;
        if (head.data.equals(item)) { //CHECKS IF THE HEAD HAS TEH SPECIFIED ITEM
            head = head.next;
            return true;
        }

        mNodeL<T> current = head;
        while (current.next != null) { //ITERATES THROUGH ALL ITEMS TO FIND THE SPECIFIED ITEM AND REMOVE IT
            if (current.next.data.equals(item)) {
                current.next = current.next.next;
                return true;
            }
            current = current.next;
        }

        return false;
    }
    //----------------------------------------------------------------------------------------------------------------------

    public boolean isEmpty() {
        return head == null;
    }

    //----------------------------------------------------------------------------------------------------------------------
    public int size() { // RETURNS TEH SIZE OF THE LINKED LIST
        int count = 0;
        mNodeL<T> current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    //----------------------------------------------------------------------------------------------------------------------
    public T get(int i) { //RETURNS THE DATA WITHIN THE NODE AT THE INDEX I
        mNodeL<T> current = head;
        int j = 0;

        while (current != null && j < i) {
            current = current.next;
            j++;
        }
        return (current != null) ? current.data : null;//RETURNS THE DATA OR NULL IF THE HEAD IS EQ TO NULL
    }

    //----------------------------------------------------------------------------------------------------------------------
    public mNodeL<T> getNode(int i) { //RETUNS THE NODE, NOT JUST THE ITEM WITHIN
        mNodeL<T> current = head;
        int j = 0;

        while (current != null && j < i) {
            current = current.next;
            j++;
        }

        return current;
    }

    //----------------------------------------------------------------------------------------------------------------------
    public int getIndex(T obj) { //RETURNS TEH INDEX OF AN ITEM IN A NODE
        mNodeL<T> current = head;
        int index = 0;
        while (current != null) {
            if (current.getContent().equals(obj)) return index;
            else {
                current = current.next;
                index++;
            }

        }
        return -1; //IF IT DOESNT EXIST RETURNS -1
    }

    //----------------------------------------------------------------------------------------------------------------------
    public void swapNodes(mNodeL a, mNodeL b) { //SWAPS 2 NODES
        if (a == b) return;

        mNodeL prevA = null;
        mNodeL prevB = null;
        mNodeL curr = head; //INITIALIZATION

        while (curr != null && curr.next != null) { //FINDS THE PREDECESSING NODES
            if (curr.next == a) prevA = curr;
            if (curr.next == b) prevB = curr;
            curr = curr.next;
        }
        if (head == a) prevA = null; //EDGE CASE IF THE NODES TO SWAP ARE THE HEAD
        if (head == b) prevB = null;

        if (prevA != null) prevA.next = b; //ASSIGNS THE PREVIOUS A TO HAVE B AS NEXT
        else head = b;

        if (prevB != null) prevB.next = a; //JUST AS ABOVE, VICE VERSA
        else head = a;

        mNodeL temp = a.next;//SWAPS THE NODES NEXT VALUES
        a.next = b.next;
        b.next = temp;
    }

    //----------------------------------------------------------------------------------------------------------------------
    public void swapNodes(int i, int j) {//SAME AS SWAP NODES, JUST FINDS NODES WITH INDEXES TO SWAP
        if (i == j) return;

        mNodeL<T> a = getNode(i);
        mNodeL<T> b = getNode(j);
        if (a == null || b == null) return;
        swapNodes(a, b);
    }

    //----------------------------------------------------------------------------------------------------------------------
    @Override //ITERATOR FOR THE LINKED LIST
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private mNodeL<T> current = head; //SETS THE FIRST ITERATION ON THE LINKEDLIST ON ITS HEAD

            @Override
            public boolean hasNext() {
                return current != null;
            } //CHECK IF THERE IS A NEXT OBJECT

            @Override
            public T next() { //GETS THE DATA AND SETS THE NEXT NODE FOR FURTHER ITERATION
                T value = current.data;
                current = current.next;
                return value;
            }
        };
        //----------------------------------------------------------------------------------------------------------------------

    }

    public mNodeL<Object> searchNode(mNodeL<T> third) {
        mNodeL<T> current = head;

        while (current != null) {
            if (Objects.equals(current.getContent(), third)) {  // safe null-check
                return (mNodeL<Object>) current;
            }
            current = current.next;
        }
        return null;
    }
}
