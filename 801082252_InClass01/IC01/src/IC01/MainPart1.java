package IC01;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainPart1 {
    /*
    * Question 1:
    * - In this question you will use the Data.users array that includes
    * a list of users. Formatted as : firstname,lastname,age,email,gender,city,state
    * - Create a User class that should parse all the parameters for each user.
    * - Insert each of the users in a list.
    * - Print out the TOP 10 oldest users.
    * */

    public static void main(String[] args) {

        //example on how to access the Data.users array.
        ArrayList<User> userList = new ArrayList<>();
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
        
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return u2.age-u1.age;
            }
        });

        for(int i=0;i<10;i++){
            System.out.println("data: " + userList.get(i).toString());
        }
    }
}