package main.electionmngmntsys.mlinkedlist;

public class mNodeL <T>{
    public T data;
    public mNodeL<T> next;

    public mNodeL(T data, mNodeL<T> next){
        this.data = data;
        this.next = next;
    }

}
