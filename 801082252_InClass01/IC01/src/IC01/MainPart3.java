package IC01;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.HashSet;

public class MainPart3 {
    /*
    * Question 3:
    * - In this question you will use the Data.users and Data.otherUsers array that includes
    * a list of users. Formatted as : firstname,lastname,age,email,gender,city,state
    * - Create a User class that should parse all the parameters for each user.
    * - The goal is to print out the users that are exist in both the Data.users and Data.otherUsers.
    * Two users are equal if all their attributes are equal.
    * - Print out the list of users which exist in both Data.users and Data.otherUsers.
    * */

    public static void main(String[] args) {
        ArrayList<User> userList = new ArrayList<>();
		ArrayList<User> otherUserList = new ArrayList<>();
		ArrayList<User> matchList = new ArrayList<>();
		for(String users : Data.users)
		{			
			String [] elements = users.split(",");
			String firstname = elements[0].trim();
			String lastname = elements[1].trim();
			int age = Integer.parseInt(elements[2]);
			String email = elements[3].trim();
			String gender = elements[4].trim();
			String city = elements[5].trim();
			String state = elements[6].trim();
			User user = new User(firstname,lastname,age,email,gender,city,state);
			userList.add(user);
		}
		
		for(String users : Data.otherUsers)
		{			
			String [] elements = users.split(",");
			String firstname = elements[0].trim();
			String lastname = elements[1].trim();
			int age = Integer.parseInt(elements[2]);
			String email = elements[3].trim();
			String gender = elements[4].trim();
			String city = elements[5].trim();
			String state = elements[6].trim();
			User user = new User(firstname,lastname,age,email,gender,city,state);
			otherUserList.add(user);
		}
		
		for(User user : userList)
		{
			for(User otherUser : otherUserList)
			{
				if(user.firstname.equals(otherUser.firstname) && (user.lastname.equals(otherUser.lastname)) && (user.age == otherUser.age) &&
						(user.email.equals(otherUser.email)) && (user.gender.equals(otherUser.gender)) && (user.city.equals(otherUser.city)) 
							&& (user.state.equals(otherUser.state)))
				{
					matchList.add(user);
				}
			}
		}

		Collections.sort(matchList, sortedList);
		for(User user : matchList)
			System.out.println(user);
    }
    public static Comparator<User> sortedList = new Comparator<User>()
	{
		public int compare(User x1, User x2)
		{
			return x1.state.compareTo(x2.state);
		}
	};
}