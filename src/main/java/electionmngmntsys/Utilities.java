package electionmngmntsys;

import electionmngmntsys.mhashmap.mHashMap;
import electionmngmntsys.mlinkedlist.mLinkedList;
import electionmngmntsys.models.Election;

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

    public static mLinkedList copyList(mLinkedList list)
    {
        mLinkedList newList=new mLinkedList();
        for (Object o : list)
        {
            newList.add(o);
        }
        return newList;
    }

    public static int getCommaSeparatedStringSize(String string)
    {
        int size=1;
        for (int i=0; i<string.length(); i++)
        {
            if (string.charAt(i)==',')
                size++;
        }
        return size;
    }

    public static String getValueFromCommaSeparatedString(int index, String string)
    {
        int num=0;
        String ret="";
        for (int i=0; i<string.length(); i++)
        {
            if (string.charAt(i)==',')
            {
                num++;
                if (num>index)
                    return ret.trim();
                ret="";
            }
            else
                ret+=string.charAt(i);
        }
        return ret.trim();
    }

    public static int compareElections(Election first, Election second, String criteria)
    {
        switch (criteria)
        {
            case "Name Ascending":
                return first.getName().compareToIgnoreCase(second.getName());
            case "Type Ascending":
                return Utilities.electionTypeReverseMap.get(first.getType()).getValue().compareToIgnoreCase(Utilities.electionTypeReverseMap.get(second.getType()).getValue());
            case "Year Ascending":
                return first.getYearDate().compareTo(second.getYearDate());
            case "Location Ascending":
                return first.getLocation().compareToIgnoreCase(second.getLocation());
            case "Name Descending":
                return -first.getName().compareToIgnoreCase(second.getName());
            case "Type Descending":
                return -Utilities.electionTypeReverseMap.get(first.getType()).getValue().compareTo(Utilities.electionTypeReverseMap.get(second.getType()).getValue());
            case "Year Descending":
                return -first.getYearDate().compareTo(second.getYearDate());
            case "Location Descending":
                return -first.getLocation().compareTo(second.getLocation());
        }
        return -1;
    }
}
