package electionmngmntsys.mlinkedlist;

import java.util.Iterator;


public class mLinkedList<T> implements Iterable<T>{
    private mNodeL<T> head;

    public mLinkedList(){
        head = null;
    }
    public void addNode(mNodeL<T> n){
        if (head == null){
            head = n;
        }
        else{
            mNodeL<T> current = head;
            while(current.next != null)
                current = current.next;
            current.next = n;
        }
    }
    public void add(T value){
        addNode(new mNodeL<>(value, null));
    }
    public boolean remove(T item){
        if (head == null) return false;
        if(head.data.equals(item)){
            head = head.next;
            return true;
        }

        mNodeL<T> current = head;
        while (current.next != null){
            if(current.next.data.equals(item)){
                current.next = current.next.next;
                return true;
            }
            current = current.next;
        }

        return false;
    }

    public boolean isEmpty(){
        return head == null;
    }

    public int size(){
        int count = 0;
        mNodeL<T> current = head;
        while(current != null){
            count++;
            current = current.next;
        }
        return count;
    }

    public T get(int i){
        mNodeL<T> current = head;
        int j = 0;

        while(current != null && j < i){
            current = current.next;
            j++;
        }
        return (current != null) ? current.data : null;
    }
    public int getIndex(T obj){
        mNodeL<T> current = head;
        int index = 0;
        while(current != null){
            if(current == obj) return index;
            else{
                current = current.next;
                index++;
            }

        }
        return -1;
    }

    public void swapNodes(mNodeL head, mNodeL a, mNodeL b) {
        mNodeL prevA = null;
        mNodeL prevB = null;
        mNodeL curr = head;

        while (curr != null && curr.next != null) {
            if (curr.next == a) prevA = curr;
            if (curr.next == b) prevB = curr;
            curr = curr.next;
        }

        prevA.next = b;
        prevB.next = a;

        mNodeL temp = a.next;
        a.next = b.next;
        b.next = temp;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private mNodeL<T> current = head;
            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T value = current.data;
                current = current.next;
                return value;
            }
        };
    }

}
