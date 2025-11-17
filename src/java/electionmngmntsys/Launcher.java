package electionmngmntsys;

import electionmngmntsys.mhashmap.mHashMap;

public class Launcher {
    public static void main(String[] args) {
        mHashMap<String, String> bee = new mHashMap<>();//testone
        bee.put("lmao", "lmae");
        bee.put("lma1", "lmaasdfksadhfkasjd");
        System.out.println(bee.get("lmao").getValue());
        System.out.println(bee.get("lma1").getValue());
    }
}
