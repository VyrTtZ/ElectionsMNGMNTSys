package com.example.electionmngmntsys.mhashmap;

public class mNodeH <X, Y>{
    X key;
    Y value;
    mNodeH<X, Y> next;

    public mNodeH(X k, Y v){
        key = k;
        value = v;
        next = null;

    }

    public X getKey() {
        return key;
    }

    public void setKey(X key) {
        this.key = key;
    }

    public Y getValue() {
        return value;
    }

    public void setValue(Y value) {
        this.value = value;
    }

    public mNodeH<X, Y> getNext() {
        return next;
    }

    public void setNext(mNodeH<X, Y> next) {
        this.next = next;
    }
}
