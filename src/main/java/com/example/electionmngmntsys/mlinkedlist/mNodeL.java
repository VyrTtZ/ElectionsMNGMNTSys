package com.example.electionmngmntsys.mlinkedlist;

public class mNodeL <T>{
    T data;
    mNodeL<T> next;

    public mNodeL(T data, mNodeL<T> next){
        this.data = data;
        this.next = next;
    }

}
