package electionmngmntsys;

import electionmngmntsys.mhashmap.mHashMap;

public class Utilities {
    public static mHashMap <String, Integer> electionTypeMap =new mHashMap();
    public static mHashMap <Integer, String> electionTypeReverseMap =new mHashMap();
    public static void initMap()
    {
        electionTypeMap.put("Local", 1);
        electionTypeMap.put("European", 2);
        electionTypeMap.put("Presidential", 3);
        electionTypeReverseMap.put(1, "Local");
        electionTypeReverseMap.put(2, "European");
        electionTypeReverseMap.put(3, "Presidential");
    }

    public static boolean isValidInteger(String str)
    {
        boolean flag=false;
        for (int i = 0; i < str.length(); i++)
        {
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '-' && str.charAt(i) != '+')
                return false;
            if (Character.isDigit(str.charAt(i)))
                flag=true;
        }
        return flag;
    }
}
