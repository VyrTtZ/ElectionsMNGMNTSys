package electionmngmntsys.mhashmap;

import electionmngmntsys.mlinkedlist.mLinkedList;
import electionmngmntsys.mlinkedlist.mNodeL;
import electionmngmntsys.models.Candidate;
import electionmngmntsys.models.Election;
import electionmngmntsys.models.Politician;

import java.io.*;
import java.util.Iterator;

public class mHashMap<X, Y> {
    private final int INITIALCAPACITY = 11;
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

    public int hash(Object o) { //some garbage copy of wyhash, we get like approx 24% collisions on 100 String, String entries
        if (o == null) return 0;

        byte[] data = toByteArray((Serializable) o);
        for(byte b : data) System.out.print(b + " ");
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

        return (int) (h ^ (h >>> 32));
    }


    public static byte[] toByteArray(Serializable obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {

            if (obj instanceof Election e) {
                dos.writeUTF(e.getName() != null ? e.getName() : "");
                dos.writeInt(e.getType());
                dos.writeUTF(e.getLocation() != null ? e.getLocation() : "");
                dos.writeUTF(e.getYearDate() != null ? e.getYearDate().toString() : "");
                dos.writeInt(e.getNumOfWinners());
            }
            else if (obj instanceof Candidate c) {
                dos.writeUTF(c.getName() != null ? c.getName() : "");
                dos.writeUTF(c.getDateOfBirth() != null ? c.getDateOfBirth().toString() : "");
                dos.writeUTF(c.getParty() != null ? c.getParty() : "");
                dos.writeUTF(c.getHomeCountry() != null ? c.getHomeCountry() : "");
                dos.writeUTF(c.getImageURL() != null ? c.getImageURL() : "");
                dos.writeUTF(c.getElection() != null && c.getElection().getName() != null
                        ? c.getElection().getName() : "");
                dos.writeInt(c.getVotes());
            }
            else if (obj instanceof Politician p) {
                dos.writeUTF(p.getName() != null ? p.getName() : "");
                dos.writeUTF(p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : "");
                dos.writeUTF(p.getParty() != null ? p.getParty() : "");
                dos.writeUTF(p.getHomeCountry() != null ? p.getHomeCountry() : "");
                dos.writeUTF(p.getImageURL() != null ? p.getImageURL() : "");
            }
            else if (obj instanceof mLinkedList<?> list) {
                for (Object item : list) {
                    byte[] itemBytes = toByteArray((Serializable) item);
                    dos.writeInt(itemBytes.length);
                    dos.write(itemBytes);
                }
            }
            else if (obj != null) {
                dos.writeUTF(obj.toString());
            }

            dos.flush();
            return bos.toByteArray();

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }



    public void put(X key, Y value) {
        putNode(new mNodeH<>(key, value));
    }

    private static boolean bytesEqual(byte[] a, byte[] b) {
        if (a == null || b == null) return a == b;
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }
    public void putNode(mNodeH<X, Y> node) {

        if (node.key == null) return;

        byte[] nodeBytes = toByteArray((Serializable) node.key);

        int index = Math.floorMod(hash(node.key), CURRENTCAPACITY);

        for (mNodeH<X, Y> n : map[index]) {
            byte[] existingBytes = toByteArray((Serializable) n.key);
            if (bytesEqual(nodeBytes, existingBytes)) {
                n.value = node.value;
                System.out.println("been juked");
                return;
            }
        }

        if (!map[index].isEmpty()) System.out.println("Collision");
        map[index].add(node);

        loadFactor = (double) size() / CURRENTCAPACITY;
        if (loadFactor > 0.60) resize();
    }


    public mNodeH<X, Y> get(X key) {
        for (mNodeH<X, Y> n : map[Math.floorMod(hash(key), CURRENTCAPACITY)]) {
            if (key.equals(n.key)) return n;
        }
        return null;
    }

    public int size() {
        int retVal = 0;
        for (int i = 0; i < map.length; i++) {
            if (!map[i].isEmpty()) retVal++;
        }
        return retVal;
    }

    private void resize() {
        temp = map;
        CURRENTCAPACITY = CURRENTCAPACITY * 2;
        map = new mLinkedList[CURRENTCAPACITY];
        for (int i = 0; i < CURRENTCAPACITY; i++) map[i] = new mLinkedList<>();
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != null) {
                for (mNodeH n : temp[i]) {
                    put((X) n.getKey(), (Y) n.getValue());
                }
            }
        }

    }

    public boolean containsKey(X key) {
        byte[] keyBytes = mHashMap.toByteArray((Serializable) key);

        for (mLinkedList<mNodeH> temp : map) {
            for (mNodeH<X, Y> n : temp) {
                if (bytesEqual(keyBytes, mHashMap.toByteArray((Serializable) n.key))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void printAllKeys() {
        System.out.println("Keys in the hashmap:");
        for (int i = 0; i < CURRENTCAPACITY; i++) {
            mLinkedList<mNodeH> bucket = map[i];
            if (!bucket.isEmpty()) {
                for (mNodeH<X, Y> node : bucket) {
                    System.out.println(node.getKey().toString());
                }
            }
        }
    }

    public boolean containsValue(Y value) {
        for (int i = 0; i < CURRENTCAPACITY; i++) {
            for (mNodeH n : map[i]) {
                if (n.getValue().equals(value)) return true;
            }
        }
        return false;
    }

    public void remove(X key) {
        int index = Math.floorMod(hash(key), CURRENTCAPACITY);
        if (map[index] != null) {
            map[index].remove(get(key));
        }
    }

    public int getCURRENTCAPACITY() {
        return CURRENTCAPACITY;
    }

    public void swapValues(mNodeH swapping, mNodeH toSwapWith) {
        Y temp = (Y) swapping.value;
        swapping.value = toSwapWith.value;
        toSwapWith.value = temp;
    }
}







