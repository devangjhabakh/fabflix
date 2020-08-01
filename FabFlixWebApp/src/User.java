import java.util.*;

public class User {
	private final String username;
	private final String id;
	private HashMap<String, Integer> cartItems;
	
	public User(String Username, String Id) {
		this.username = Username;
		this.id = Id;
		this.cartItems = new HashMap<String, Integer>();
	}
	
	public String getUserName() {
		return this.username;
	}

	public String getId() {
		return this.id;
	}
	
	public HashMap<String, Integer> getCart(){
		return this.cartItems;
	}
	
	public void EnterIntoCart(String movie_id) {
		if (!cartItems.containsKey(movie_id))
			cartItems.put(movie_id,1);
	}
	
	public void subtract(String movie_id) {
		int currentCount = cartItems.get(movie_id);
		if(currentCount > 1) {
			cartItems.put(movie_id, currentCount-1);
		}
		else
			cartItems.remove(movie_id);
	}
	
	public void add(String movie_id) {
		int currentCount = cartItems.get(movie_id);
		cartItems.put(movie_id, currentCount+1);
	}
	
	public void UserCheckOut() {
		cartItems = new HashMap<String,Integer>();
	}
}
