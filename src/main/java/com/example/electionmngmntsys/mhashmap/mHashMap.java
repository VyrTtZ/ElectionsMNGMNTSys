package main.java.com.example.electionmngmntsys.mhashmap;

import main.java.com.example.electionmngmntsys.mlinkedlist.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class mHashMap<X, Y> {
    private final int INITIALCAPACITY =97;
    private int CURRENTCAPACITY = INITIALCAPACITY;
    private mLinkedList<mNodeH>[] map;
    private mLinkedList<mNodeH>[] temp;
    private final int seed1 = 0x5A3C7F1B;
    private final int seed2 = 0x1F8E6D2A;
    private double loadFactor = 0;


    public mHashMap() {
        map = new mLinkedList[CURRENTCAPACITY];
        for (int i = 0; i < CURRENTCAPACITY; i++) map[i] = new mLinkedList<>();
    }

    private int hash(Object o) {
        int temp = 0;
        byte[] bytes = toByteArray((Serializable) o);//FREAKY SERIALIZABLE 🥵


        for(int i = 0; i < bytes.length ; i++) temp += bytes[i];
        int tempChunks = 0;
        mLinkedList<Integer> chunks = new mLinkedList<>();


        for(int i = 0; i < bytes.length; i++){
            tempChunks += bytes[i] & 0xFF; //
            if((i + 1) % 8 == 0 || i == bytes.length - 1){
                chunks.add(tempChunks);
                tempChunks = 0;
            }
        }
        int tempSeedUno = seed1;
        int tempSeedDos = seed2;

        long seed = tempSeedUno;
        for (int chunk : chunks) {
            seed += chunk;
            seed = (seed ^ (seed >>> 7));
            seed = (seed ^ (seed >>> 11));
            seed = seed ^ (seed >>> 13);
        }
        tempSeedUno = (int) seed;
        tempSeedDos = (int) (seed >>> 17);


        System.out.println(((tempSeedUno + tempSeedDos)*temp));
        return (tempSeedUno + tempSeedDos);

    }

    public byte[] toByteArray(Serializable obj) {//https://www.baeldung.com/object-to-byte-array
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        }
        catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    public void put(X key, Y value){
        putNode(new mNodeH<>(key, value));
    }
    public void putNode(mNodeH<X, Y> node){

        for (mNodeH<X, Y> n : map[Math.floorMod(hash(node.key), CURRENTCAPACITY)]) {
            if (node.key.equals(n.key)) {
                n.value = node.value;
                return;
            }
        }
        if(!map[Math.floorMod(hash(node.key), CURRENTCAPACITY)].isEmpty()) System.out.println("COllision");

        map[Math.floorMod(hash(node.key), CURRENTCAPACITY)].add(node);

        loadFactor = size()/CURRENTCAPACITY;
        if(loadFactor > .55) resize();

    }

    public mNodeH<X, Y> get(X key){
        for (mNodeH<X,Y> n : map[Math.floorMod(hash(key), CURRENTCAPACITY)]) {
            if (key.equals(n.key)) return n;
        }
        return null;
    }

    public int size(){
        int retVal = 0;
        for(int i = 0; i < map.length; i++){
            if(!map[i].isEmpty()) retVal++;
        }
        return retVal;
    }

    private void resize(){
        System.out.println("resizing");
        temp = map;
        CURRENTCAPACITY = CURRENTCAPACITY*2;
        map = new mLinkedList[CURRENTCAPACITY];
        for (int i = 0; i < CURRENTCAPACITY; i++) map[i] = new mLinkedList<>();
        for(int i = 0; i < temp.length; i++){
            if(temp[i] != null){
                for(mNodeH n : temp[i]){
                    put((X)n.getKey(), (Y)n.getValue());
                }
            }
        }

    }
    public boolean containsKey(X key){
        if(get(key).equals(null)) return false;
        else return true;
    }
    public boolean containsValue(Y value){
        for(int i = 0; i < CURRENTCAPACITY; i++){
            for(mNodeH n : map[i]){
                if(n.getValue().equals(value)) return true;
            }
        }
        return false;
    }
    public void remove(X key){
        int index = Math.floorMod(hash(key), CURRENTCAPACITY);
        if (map[index] != null) {
            map[index].remove(get(key));
        }
    }

    public int getCURRENTCAPACITY() {
        return CURRENTCAPACITY;
    }
}







