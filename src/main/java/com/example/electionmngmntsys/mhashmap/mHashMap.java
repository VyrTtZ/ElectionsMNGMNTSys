package com.example.electionmngmntsys.mhashmap;

import com.example.electionmngmntsys.mlinkedlist.mNodeL;
//FIX THE HASH THINGIE
public class mHashMap<X, Y> {
    private mNodeH<X, Y> head;
    private int hash;

    public mHashMap(){
        head = null;
    }

    public void putNode(mNodeH<X, Y> n){
        if(head == null){
            head = n;
        }
        else{
            mNodeH<X, Y> current = head;
            while(current.next != null)
                current = current.next;
            current.next = n;
        }
    }
    public void add(X x, Y y){
        putNode(new mNodeH<>(x, y, 0, null));
    }

    public boolean isEmpty(){
        return head == null;
    }



}
