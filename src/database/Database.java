package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cookieProduction.Pallet;
import cookieProduction.PalletList;

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
	public String registerProducedPallets(String productName,
			String productionDate, int nbrPallets) {
		String result = "";
		try {
			conn.setAutoCommit(false);

			int insertResult;
			// TODO beh�ver kanske kolla resultatkoderna...
			for (int i = 0; i < nbrPallets; i++) {
				insertResult = insertNewPallet(productName, productionDate);

				decrementProductIngredients(productName);
			}

			conn.commit();
			result = "Successfully inserted " + nbrPallets + " of "
					+ " product type " + productName + " into the database.";
		} catch (SQLException e) {
			e.printStackTrace();
			rollback();
			result = "An error occured. Insertion unsuccessful.";
		}
		return result;
	}

	@SuppressWarnings("resource")
	private int insertNewPallet(String productName, String productionDate)
			throws SQLException {

		String statement;
		PreparedStatement prepStatement = null;

		String orderID = "null";
		String delivDate = "null";
		int result = 0;
		try {
			/* TODO beh�vs semikolon i SQL-satsen? */
			statement = "insert into Pallets(cookieName, productionDate)"
					+ " values(?,?)";
			prepStatement = conn.prepareStatement(statement);

			prepStatement.setString(1, productName);
			prepStatement.setString(2, productionDate);

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
	 * TODO st�mmer inte med databasen, �ndra table ingredients?
	 */
	@SuppressWarnings("resource")
	private void decrementProductIngredients(String productName)
			throws SQLException {
		HashMap<String, Float> ingredientAmounts = getIngredientAmounts(productName);
		PreparedStatement prepStatement = null;

		/* <Ingredient, amount to decrement> */

		String statement = "update Ingredients"
				+ " set amountInStorage = amountInStorage - ?"
				+ " where ingredientName = ?";
		try {
			prepStatement = conn.prepareStatement(statement);
			for (Map.Entry<String, Float> entry : ingredientAmounts.entrySet()) {

				Float amount = entry.getValue();
				String ingredient = entry.getKey();

				prepStatement.setFloat(1, amount);
				prepStatement.setString(2, ingredient);

				prepStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			close(prepStatement);
			throw new SQLException();
		}
		close(prepStatement);
	}

	/* Returns the recipe for the given product. */
	@SuppressWarnings("resource")
	private HashMap<String, Float> getIngredientAmounts(String productName)
			throws SQLException {
		HashMap<String, Float> ingredients = new HashMap<String, Float>();
		PreparedStatement prepStatement = null;
		ResultSet rs = null;
		try {
			String statement = "select ingredientName,amount from CookieIngredients where cookieName = ?";
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

	/*
	 * Returns info about all pallets that contain the given product and were
	 * produced in the given time interval.
	 */
	public String getPalletInfo(String productType, String timeIntervalStart,
			String timeIntervalEnd) {
		String statement = "select * from pallets where cookieName = '"
				+ productType + "' and productionDate > '" + timeIntervalStart
				+ "' and productionDate < '" + timeIntervalEnd + "'";
		return getPalletInfoInternal(statement);
	}

	/*
	 * Returns info about all pallets that were produced in the given time
	 * interval.
	 */
	public String getPalletInfo(String timeIntervalStart, String timeIntervalEnd) {
		String statement = "select * from pallets where productionDate > '"
				+ timeIntervalStart + "' and productionDate < '"
				+ timeIntervalEnd + "'";
		return getPalletInfoInternal(statement);
	}

	/*
	 * Returns info about all pallets that contain the given product.
	 */
	public String getPalletInfo(String productType) {
		String statement = "select * from pallets where cookieName = '"
				+ productType + "'";
		return getPalletInfoInternal(statement);
	}

	private String getPalletInfoInternal(String statement) {
		PreparedStatement prepStatement = null;
		ResultSet rs = null;
		PalletList pallets = new PalletList();
		String result;
		try {
			conn.setAutoCommit(false);
			prepStatement = conn.prepareStatement(statement);
			rs = prepStatement.executeQuery();

			while (rs.next()) {

				/* getString kanske inte funkar */
				pallets.add(new Pallet(rs.getString("barCodeID"), rs
						.getString("cookieName"), rs.getString("orderID"), rs
						.getString("productionDate"), rs
						.getString("deliveryDate")));
			}

			conn.commit();
			if (!pallets.isEmpty()) {
				result = pallets.toString();
			} else {
				result = "No matching pallets were found.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			rollback();
			result = "An error occured. Query unsuccessful.";
		} finally {
			close(prepStatement);
			close(rs);
		}

		return result;
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
	 * Blocks all pallets containing made in the given time interval.
	 */
	public String blockPallets(String timeIntervalStart, String timeIntervalEnd) {
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
	 * Unlocks all pallets containing the given product made in the given time
	 * interval.
	 */
	public String unblockPallets(String product, String timeIntervalStart,
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

	/* Returns all blocked pallets. */
	public String getBlockedPallets() {
		return "";
	}

	/* Returns all blocked products. */
	public String getBlockedProducts() {
		return "";
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
