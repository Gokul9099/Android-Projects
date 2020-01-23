package IC01;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MainPart2 {
    /*
    * Question 2:
    * - In this question you will use the Data.users array that includes
    * a list of users. Formatted as : firstname,lastname,age,email,gender,city,state
    * - Create a User class that should parse all the parameters for each user.
    * - The goal is to count the number of users living each state.
    * - Print out the list of State, Count order in ascending order by count.
    * */

    public static void main(String[] args) {

        //example on how to access the Data.users array.
        HashMap <String, Integer> statesHashMap = new HashMap<>(); 
		for(String users : Data.users)
		{			
			String [] elements = users.split(",");
			String state = elements[6].trim();
			int count = 1;
			
			if(statesHashMap.containsKey(state))
			{
				count = statesHashMap.get(state);
				count++;
			}			
			statesHashMap.put(state, count);
		}	
		
	    Set<Entry<String, Integer>> entrySet = statesHashMap.entrySet();
	    List<Entry<String, Integer>> stateList = new ArrayList<Entry<String, Integer>>(entrySet);
	    Collections.sort(stateList, new SortByValue());
	    System.out.println("States"+"  Count");
	  
	    for(Map.Entry<String, Integer> entry : stateList)
	        System.out.println(entry.getKey()+"      "+entry.getValue()+"  ");
    }
}

class SortByValue implements Comparator<Map.Entry<String, Integer>>
{
	@Override
    public int compare( Map.Entry<String,Integer> entry1, Map.Entry<String,Integer> entry2){
        return (entry1.getValue()).compareTo( entry2.getValue() );
    }
}