package database;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cookieProduction.Recipe;

@SuppressWarnings({ "unused", "static-method" })
public class Database {
	private Connection conn;

	public Database() {
		conn = null;
	}

	public boolean openConnection(String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://puccini.cs.lth.se/" + userName, userName,
					password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}

	public boolean isConnected() {
		return conn != null;
	}

	/*
	 * Register a number of produced pallets and updates the ingredient database
	 * accordingly.
	 */
	public String registerProducedPallets(String productName, String orderID,
			String productionDate, String productionTime, int nbrPallets) {
		String result = "";
		try {
			conn.setAutoCommit(false);

			int insertResult, updateIngredientsResult;
			// TODO behöver kanske kolla resultatkoderna...
			for (int i = 0; i < nbrPallets; i++) {
				insertResult = insertNewPallet(productName, orderID,
						productionDate, productionTime);

				decrementProductIngredients(productName);
			}

			conn.commit();
			result = "Successfully inserted " + nbrPallets + " of "
					+ " product type " + productName + " into the database.";
		} catch (SQLException e) {
			e.printStackTrace();
			rollback();
			result = "An error occured. Insertion unsuccesful.";
		}

		return result;
	}

	@SuppressWarnings("resource")
	private int insertNewPallet(String productName, String orderID,
			String productionDate, String productionTime) throws SQLException {

		String statement;
		PreparedStatement prepStatement = null;

		/* TODO hämta delivery date osv från databasen med hjälp av orderID */
		String delivDate = "";
		String delivTime = "";
		int result = 0;

		try {
			/* TODO behövs semikolon i SQL-satsen? */
			statement = "insert into pallets "
					+ "(cookieName,orderID,productionDate,"
					+ "productionTime,delivDate,deliveryTime)"
					+ " values(?,?,?,?,?,?)";

			prepStatement = conn.prepareStatement(statement);

			prepStatement.setString(1, productName);

			/*
			 * TODO kanske borde ändra till setInt vid orderID
			 */
			prepStatement.setString(2, orderID);
			prepStatement.setString(3, productionDate);
			prepStatement.setString(4, productionTime);
			prepStatement.setString(5, delivDate);
			prepStatement.setString(6, delivTime);

			result = prepStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			close(prepStatement);
			throw new SQLException();
		}
		close(prepStatement);
		return result;
	}

	/*
	 * Decrements the ingredient storage with the amount it takes to produce a
	 * pallet of the given product.
	 * 
	 * TODO stämmer inte med databasen, ändra table ingredients?
	 */
	@SuppressWarnings("resource")
	private void decrementProductIngredients(String productName)
			throws SQLException {
		HashMap<String, Float> ingredientAmounts = getIngredientAmounts(productName);
		PreparedStatement prepStatement = null;

		/* <Ingredient, amount to decrement> */
		String statement = "update ingredients" + "set amount=amount-?"
				+ "where ingredientName = ?";
		try {
			prepStatement = conn.prepareStatement(statement);
			for (Map.Entry<String, Float> entry : ingredientAmounts.entrySet()) {
				prepStatement.setFloat(1, entry.getValue());
				prepStatement.setString(2, entry.getKey());
			}
			prepStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			close(prepStatement);
			throw new SQLException();
		}
		close(prepStatement);
	}

	/* Returns the recipe for the given product. */
	@SuppressWarnings("resource")
	private HashMap<String, Float> getIngredientAmounts(String productName) throws SQLException {
		HashMap<String, Float> ingredients = new HashMap<String, Float>();
		PreparedStatement prepStatement = null;
		ResultSet rs = null;
		try {
			String statement = "select from CookieIngredients where cookieName = ?";
			prepStatement = conn.prepareStatement(statement);
			prepStatement.setString(1, productName);
			rs = prepStatement.executeQuery();

			String ingredient;
			float amount;
			while (!rs.isLast()) {
				rs.next();
				ingredients.put(rs.getString("ingredientName"),
						rs.getFloat("amount"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			close(prepStatement);
			close(rs);
			throw new SQLException();
		}
		close(prepStatement);
		close(rs);

		return ingredients;
	}

	/* Registers any number of delivered Pallets. */
	public void registeredDeliveredPallets(int orderNbr, String product,
			int nbrPallets) {
	}

	/*
	 * Registers the time of delivery of and info about all pallets delivered to
	 * the given customer.
	 */
	public String getDeliveredPalletInfo(String customer) {
		return "";
	}

	/* Returns info regarding the pallet with the palletNbr. */
	public String getPalletInfo(int palletNbr) {
		return "";
	}

	/*
	 * Returns info about all pallets that contain the given product.
	 */
	public String getPalletInfo(String productType) {
		return "";
	}

	/*
	 * Returns info about all pallets that were produced in the given time
	 * interval.
	 */
	public String getPalletInfo(String timeIntervalStart, String timeIntervalEnd) {
		return "";
	}

	/*
	 * Returns info about all pallets that contain the given product and were
	 * produced in the given time interval.
	 */
	public String getPalletInfo(String productType, String timeIntervalStart,
			String timeIntervalEnd) {
		return "";
	}

	/*
	 * Blocks all pallets containing the given product made in the given time
	 * interval.
	 */
	public String blockPallets(String product) {
		return "";
	}

	/*
	 * Blocks all pallets containing the given product made in the given time
	 * interval.
	 */
	public String blockPallets(String product, String timeIntervalStart,
			String timeIntervalEnd) {
		return "";
	}

	/*
	 * Unlocks all pallets containing the given product made in the given time
	 * interval.
	 */
	public String unblockPallets(String product) {
		return "";
	}

	/*
	 * Unlocks all pallets containing the given product made in the given time
	 * interval.
	 */
	public String unblockPallets(String product, String timeIntervalStart,
			String timeIntervalEnd) {
		return "";
	}

	/* Returns all blocked pallets. */
	public String getBlockedPallets() {
		return "";
	}

	/* Returns all blocked products. */
	public String getBlockedProducts() {
		return "";
	}

	/*
	 * Returns the number of produced pallets of the given product type in the
	 * given time interval.
	 */
	public int getNbrProducedPallets(String product, int timeIntervalStart,
			int timeIntervalEnd) {
		return 0;
	}

	/*
	 * Returns the amount of the given ingredient left in the ingredient
	 * storage.
	 */
	public int getIngredientAmount(String ingredient) {
		return 0;
	}

	/* Closes the given PreparedStatement */
	private void close(PreparedStatement prepStatement) {
		try {
			if (prepStatement != null) {
				prepStatement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void close(ResultSet rs) {
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void rollback() {
		try {
			conn.rollback();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
