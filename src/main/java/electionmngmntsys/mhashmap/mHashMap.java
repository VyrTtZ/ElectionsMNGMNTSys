package electionmngmntsys.mhashmap;

import electionmngmntsys.mlinkedlist.mLinkedList;
import electionmngmntsys.models.Candidate;
import electionmngmntsys.models.Election;
import electionmngmntsys.models.Politician;

import java.io.*;

//----------------------------------------------------------------------------------------------------------------------
public class mHashMap<X, Y> {
    private final int INITIALCAPACITY = 11;
    private int CURRENTCAPACITY = INITIALCAPACITY;
    private mLinkedList<mNodeH>[] map;
    private mLinkedList<mNodeH>[] temp;
    private final long SEED1 = 0x5A3C7F1B8E6D2A1FL;
    private final long SEED2 = 0x1F8E6D2A5A3C7F9BL;
    private double loadFactor = 0;
    private int size = 0;

//---------------------------------------------------------------------------------------------------------CONSTRUCTOR
    public mHashMap() {
        map = new mLinkedList[CURRENTCAPACITY]; //Array of LinkedLists (Buckets)
        for (int i = 0; i < CURRENTCAPACITY; i++) map[i] = new mLinkedList<>(); //Stating linked list for each index
    }
//------------------------------------------------------------------------------------------------------------HASH FUNCTION
    public int hash(Object o) { //some garbage copy of wyhash, we get like approx 24% collisions on 100 String, String entries
        if (o == null) return 0; //Null case

        byte[] data = toByteArray((Serializable) o);//Array of bytes
        long h = 0x6D796D6D796D6D6DL ^ data.length; //Hash seed = random const (long) XOR length of byte array

        for (int i = 0; i < data.length; i += 8) {//Divides the array into 64bit (8byte) chunks
            long chunk = 0;
            for (int j = 0; j < 8; j++) { //Iteration over 8 bytes one by one
                int index = i + j;
                long b = (index < data.length) ? (data[index] & 0xFFL) : 0L; //If index < byte array length -> cast into unsigned int else pad the byte with 0s
            }

            h ^= chunk; //XOR the hash seed with the chunk
            h *= (i + 8 <= data.length) ? SEED1 : SEED2; // Alternating constant multiplication dependent on the byte position last -> SEED2
        }
        h ^= h >>> 33; //UNSIGNED BIT ROTATION BY 33 TO THE RIGHT
        h *= 0xff51afd7ed558cc5L; // MULTIPLICATION WITH RANDOM LONG
        h ^= h >>> 33; //UNSIGNED BIT ROTATION BY 33 TO THE RIGHT
        h *= 0xc4ceb9fe1a85ec53L; //MULTIPLICATION WITH RANDON LONG
        h ^= h >>> 33;//UNSIGNED BIT ROTATION BY 33 TO THE RIGHT

        return (int) (h ^ (h >>> 32)); //ANOTHER XOR BETWEEN THE HASHSEED AND THE HASHSEED BIT ROTATED 22 TO THE RIGHT (UNSIGNED) + INT CAST
    }
//----------------------------------------------------------------------------------------------------------------------
    public static byte[] toByteArray(Serializable obj) { //https://docs.oracle.com/javase/8/docs/api/java/io/ByteArrayOutputStream.html
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {

            if (obj instanceof Election e) {//CHECK IF THE OBJECTS IS INSTANCE OF ELECTION -> TURNS SPECIFIC PARAMETERS TO BYTE ARRAYS WITH DOS WITH NULL CHECKS
                dos.writeUTF(e.getName() != null ? e.getName() : "");
                dos.writeInt(e.getType());
                dos.writeUTF(e.getLocation() != null ? e.getLocation() : "");
                dos.writeUTF(e.getYearDate() != null ? e.getYearDate().toString() : "");
                dos.writeInt(e.getNumOfWinners());
                dos.writeInt(e.getId());
                dos.write(toByteArray((Serializable) e.getCandidates()));
                dos.write(toByteArray((Serializable) e.getPoliticians()));
            }
            else if (obj instanceof Candidate c) {//CHECK IF THE OBJECTS IS INSTANCE OF CANDIDATE -> TURNS SPECIFIC PARAMETERS TO BYTE ARRAYS WITH DOS WITH NULL CHECKS
                dos.writeUTF(c.getName() != null ? c.getName() : "");
                dos.writeUTF(c.getDateOfBirth() != null ? c.getDateOfBirth().toString() : "");
                dos.writeUTF(c.getParty() != null ? c.getParty() : "");
                dos.writeUTF(c.getHomeCounty() != null ? c.getHomeCounty() : "");
                dos.writeUTF(c.getImageURL() != null ? c.getImageURL() : "");
                dos.writeUTF(c.getElection() != null && c.getElection().getName() != null ? c.getElection().getName() : "");
                dos.writeInt(c.getVotes());
            }
            else if (obj instanceof Politician p) {//CHECK IF THE OBJECTS IS INSTANCE OF POLITICIAN -> TURNS SPECIFIC PARAMETERS TO BYTE ARRAYS WITH DOS WITH NULL CHECKS
                dos.writeUTF(p.getName() != null ? p.getName() : "");
                dos.writeUTF(p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : "");
                dos.writeUTF(p.getParty() != null ? p.getParty() : "");
                dos.writeUTF(p.getHomeCounty() != null ? p.getHomeCounty() : "");
                dos.writeUTF(p.getImageURL() != null ? p.getImageURL() : "");
                dos.write(toByteArray((Serializable) p.getAssociations() ));
                dos.write(toByteArray((Serializable) p.getVotesList()));
            }
            else if (obj instanceof mLinkedList<?> list) {//CHECK IF THE OBJECTS IS INSTANCE OF LINKEDLIST -> TURNS SPECIFIC PARAMETERS TO BYTE ARRAYS WITH DOS WITH NULL CHECKS
                for (Object item : list) {//LOOP THROUGH EVERY OBJECT IN THE LIST
                    byte[] itemBytes = toByteArray((Serializable) item);//SERIALIZES THE ITEM WITHIN (RECURSION)
                    dos.writeInt(itemBytes.length); //WRITES THE LENGTH AS THE HEADER
                    dos.write(itemBytes); //WRITES THE ITEM ARRAY
                }
            }
            else if (obj != null) {//IF IT ANYTHING ELSE JUST TURN THE TO STRING INTO BYTES
                dos.writeUTF(obj.toString());
            }

            return bos.toByteArray();//RETURNS THE ARRAY

        } catch (IOException ex) { // PRINT THE ERROR IF THERE IS AN ISSUE AND EXIT
            ex.printStackTrace();
            return null;
        }
    }
//----------------------------------------------------------------------------------------------------------------------
    public void put(X key, Y value) {
        putNode(new mNodeH<>(key, value));
    }
//-----------------------------------------------------------------------------------------------------HELPER FOR COMPARING BYTE ARRAYS
    private static boolean bytesEqual(byte[] a, byte[] b) {
        if (a.length != b.length) return false;//CHECKS LENGTH
        for (int i = 0; i < a.length; i++) { //ITERATES AND COMPARES BYTE ON EACH INDEX TO THE CORRESPONDING ONE
            if (a[i] != b[i]) return false;
        }
        return true;
    }
//----------------------------------------------------------------------------------------------------------------------
    public void putNode(mNodeH<X, Y> node) {

        if (node.key == null) return;//KEY CANNOT BE NULL BC CANT BE HASHED

        byte[] nodeBytes = toByteArray((Serializable) node.key); //TURNS THE KEY INTO BYTE ARRAY

        int index = Math.floorMod(hash(node.key), CURRENTCAPACITY); //FINDS THE INDEX IN THE MAP FOR THE KEY

        for (mNodeH<X, Y> n : map[index]) {
            byte[] existingBytes = toByteArray((Serializable) n.key); //ITERATES OVER THE WHOLE MAP AND TURNS EACH KEY INTO BYTE ARRAY
            if (bytesEqual(nodeBytes, existingBytes)) { //COMPARES THE BYTE ARRAYS
                n.value = node.value; //UPDATES THE VALUES IF THE KEY ALREADY EXISTS
                System.out.println("been juked");
                return;
            }
        }
        map[index].add(node);//ADDS NODE TO THE LINKED LIST
        size++;

        loadFactor = (double) size() / CURRENTCAPACITY;
        if (loadFactor > 0.60) resize(); //IF THE LOAD FACTOR IS ABOVE .6 THE MAP RESIZES
    }
    //------------------------------------------------------------------------------------------------------------------

    public Y getValue(X key) {
        mNodeH<X, Y> node = get(key);
        return node != null ? node.getValue() : null;
    }
//----------------------------------------------------------------------------------------------------------------------
    public mNodeH<X, Y> get(X key) {
        for (mNodeH<X, Y> n : map[Math.floorMod(hash(key), CURRENTCAPACITY)]) {//LOOKS FOR A KEY WITHIN THE LINKEDLIST PLACED IN THE ARRAY
            if (key.equals(n.key)) return n;
        }
        return null;
    }
//----------------------------------------------------------------------------------------------------------------------
    public int size() { //RETURNS THE SIZE OF THE MAP (HOW FULL IT IS)
        int retVal = 0;
        for (int i = 0; i < map.length; i++) {
            if (!map[i].isEmpty())
                retVal+=map[i].size();
        }
        return retVal;
    }


//----------------------------------------------------------------------------------------------------------------------
    private void resize() {//RESIZES THE MAP
        temp = map;//SAVES THE MAP
        CURRENTCAPACITY = CURRENTCAPACITY * 2; //INCREASE IN CAPACITY
        map = new mLinkedList[CURRENTCAPACITY];
        for (int i = 0; i < CURRENTCAPACITY; i++) map[i] = new mLinkedList<>();//ADDS LINKED LISTS IN THE MAP
        for (int i = 0; i < temp.length; i++) {//REHASHES THE OBJECTS IN TEMP TO FIT THE NEW MAP
            if (temp[i] != null) {
                for (mNodeH n : temp[i]) {
                    put((X) n.getKey(), (Y) n.getValue());
                }
            }
        }

    }
//----------------------------------------------------------------------------------------------------------------------
    public boolean containsKey(X key) {//COMPARES TEH KEYS USING THEIR BYTE ARRAYS
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
//----------------------------------------------------------------------------------------------------------------------
    public boolean containsValue(Y value) {//CHECKS FOR A VALUE
        for (int i = 0; i < CURRENTCAPACITY; i++) {
            for (mNodeH n : map[i]) {
                if (n.getValue() == (value)) return true;
            }
        }
        return false;
    }
//----------------------------------------------------------------------------------------------------------------------
    public void remove(X key) {
        int index = Math.floorMod(hash(key), CURRENTCAPACITY); //FINDS THE INDEX BY KEY
        if (map[index] != null) {
            map[index].remove(get(key));//REMOVES THE NODE IN THE LINKEDLIST
        }
    }
//----------------------------------------------------------------------------------------------------------------------
    public void swapValues(mNodeH swapping, mNodeH toSwapWith) { //SWAPS VALUES BETEWEN 2 NODES
        Y temp = (Y) swapping.value;
        swapping.value = toSwapWith.value;
        toSwapWith.value = temp;
    }
}







