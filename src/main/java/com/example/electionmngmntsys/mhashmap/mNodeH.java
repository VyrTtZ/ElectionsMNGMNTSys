package com.example.electionmngmntsys.mhashmap;

public class mNodeH <X, Y>{
    X key;
    Y value;
    int hash;
    mNodeH<X, Y> next;

    public mNodeH(X k, Y v, int h, mNodeH<X, Y> n){
        key = k;
        value = v;
        hash = h;
        next = n;

    }

}
