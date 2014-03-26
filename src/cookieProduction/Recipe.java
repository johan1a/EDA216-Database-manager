package cookieProduction;

import java.util.HashMap;

public class Recipe {
	HashMap<String, Integer> ingredients; // Ingredient type as key, amount as
											// value

	public void addIngredientAmount(String productName, int amount) {
		ingredients.put(productName, amount);
	}
}
