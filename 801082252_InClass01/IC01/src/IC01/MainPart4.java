package IC01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import java.util.HashMap;

public class MainPart4{
    /*
      Question 4
      You are provided with the Data class that contains an items array (Data.items) which is an array of items in a store. Each element in the array represents a single item record. Each record in the array represents a single item record. Each record is a string formatted as : Name, ID, Price. Also, you are provided with an array called shoppingCart (Data.shoppingCart) which is an array of items’ quantities. Each element in the array represents a single item record. Each record is a string formatted as : ID, quantity. You are asked to perform the following tasks:
      You are provided with the Data class that contains an items array (Data.items) which is an array of items in a store. Each element in the array represents a single item record.
      Each record in the array represents a single item record. Each record is a string formatted as : Name, ID, Price. Also, you are provided with an array called
      shoppingCart (Data.shoppingCart) which is an array of items’ quantities. Each element the array represents a single item record. Each record is a string formatted as : ID,quantity. You are asked to perform the following tasks:
     1. Your implementation for this question should be included in MainPart3.java file.
     2. Create a StoreItem class that should parse all the parameters for each item. Hint: extract each value from a item's record using Java's String.split() method and set the
        delimiter to a comma, see provided code below. Each item record should to be assigned to a StoreItem object.
     3. Create the most efficient data structure that best fit the goal. Hint: The selected data structure should facilitate the retrieval of the item details based on the ID.
     4. The goal is to print out the receipt bill in the following format:
        ID  Name    Quantity    Price * Quantity
        123 Tomatoes 10         $30
        .
        .
        Total Bill: $400
    */

    public static void main(String[] args) {
        HashMap<Integer,ArrayList<StoreItem>> itemsHashMap = new HashMap<Integer,ArrayList<StoreItem>>();
		HashMap<Integer,ArrayList<Shoppingcart>> cartHashMap = new HashMap<Integer,ArrayList<Shoppingcart>>();
		
		for(String items : Data.items)
		{			
			String [] elements = items.split(",");
			String name = elements[0].trim();
			int id = Integer.parseInt(elements[1].trim());
			int price = Integer.parseInt(elements[2].trim());
			
			ArrayList<StoreItem> itemList = new ArrayList<StoreItem>();
			itemList.add(new StoreItem ( name, id, price));
			itemsHashMap.put(id, itemList);
			
		}
		for(String items : Data.shoppingCart)
		{			
			String [] elements = items.split(",");
			int id = Integer.parseInt(elements[0].trim());
			int quantity = Integer.parseInt(elements[1].trim());
			
			ArrayList<Shoppingcart> cartList = new ArrayList<Shoppingcart>();
			cartList.add(new Shoppingcart ( id, quantity));
			cartHashMap.put(id, cartList);
		}
		
		System.out.println(" ID     Name        Quantity      Quantity*Price");

		int total = 0;
		for(Map.Entry<Integer,ArrayList<StoreItem>> itemEntry : itemsHashMap.entrySet())
		{
			ArrayList<StoreItem> itemList = new ArrayList<StoreItem>();
			itemList = itemEntry.getValue();
			for(Map.Entry<Integer,ArrayList<Shoppingcart>> cartEntry : cartHashMap.entrySet())
			{
				ArrayList<Shoppingcart> cartList = new ArrayList<Shoppingcart>();
				cartList = cartEntry.getValue();

				if(itemEntry.getKey().equals(cartEntry.getKey()))
				{
					System.out.println(itemEntry.getKey() + "\t" + itemList.get(0).name + "     \t " + cartList.get(0).quantity +
							"      \t $" + ((itemList.get(0).price)*(cartList.get(0).quantity)) );
					total = total + ((itemList.get(0).price)*(cartList.get(0).quantity));
				}
			}
		}
		System.out.println("Total : $" + total);
    }
}


class StoreItem {
	String name;
	int id;
	int price;
	
	public StoreItem (String name,int id,int price)
	{
		this.name = name;
		this.id = id;
		this.price = price;
	}
}

class Shoppingcart {
	int id;
	int quantity;
	
	public Shoppingcart (int id,int quantity)
	{
		this.id = id;
		this.quantity = quantity;
	}
}
