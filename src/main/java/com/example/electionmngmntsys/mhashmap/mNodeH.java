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

}
