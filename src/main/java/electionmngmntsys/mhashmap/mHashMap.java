package electionmngmntsys.mhashmap;

import electionmngmntsys.mlinkedlist.mLinkedList;

import electionmngmntsys.mlinkedlist.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class mHashMap<X, Y> {
    private final int INITIALCAPACITY =11;
    private int CURRENTCAPACITY = INITIALCAPACITY;
    private mLinkedList<mNodeH>[] map;
    private mLinkedList<mNodeH>[] temp;
    private final long seed1 = 0x5A3C7F1B8E6D2A1FL;
    private final long seed2 = 0x1F8E6D2A5A3C7F9BL;
    private double loadFactor = 0;


    public mHashMap() {
        map = new mLinkedList[CURRENTCAPACITY];
        for (int i = 0; i < CURRENTCAPACITY; i++) map[i] = new mLinkedList<>();
    }

    private int hash(Object o) { //some garbage copy of wyhash, we get like approx 24% collisions on 100 String, String entries
        if (o == null) return 0;

        byte[] data = toByteArray((Serializable) o);
        long h = 0x6D796D6D796D6D6DL ^ data.length;

        for (int i = 0; i < data.length; i += 8) {
            long chunk = 0;
            for (int j = 0; j < 8; j++) {
                int index = i + j;
                long b = (index < data.length) ? (data[index] & 0xFFL) : 0L;
                chunk |= b << (j * 8);
            }

            h ^= chunk;
            h *= (i + 8 <= data.length) ? seed1 : seed2; // tail action inbound
        }
        h ^= h >>> 33;
        h *= 0xff51afd7ed558cc5L;
        h ^= h >>> 33;
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= h >>> 33;

        return (int)(h ^ (h >>> 32));
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

        loadFactor = (double) size() /CURRENTCAPACITY;
        if(loadFactor > .60) resize();

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







