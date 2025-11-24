package electionmngmntsys.mlinkedlist;

public class mNodeL <T>{
    public T data;
    public mNodeL<T> next;
    public mNodeL<T> prev;

    public mNodeL(T data, mNodeL<T> next, mNodeL<T> prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public T getContent() {
        return data;
    }

    public mNodeL<T> getNext() {
        return next;
    }

    public mNodeL<T> getPrev() {
        return prev;
    }

}
