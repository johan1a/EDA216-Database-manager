package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import cookieProduction.Order;
import cookieProduction.Recipe;

@SuppressWarnings({ "unused", "static-method" })
public class Database {
	private Connection conn;

	public Database() {
		conn = null;
	}

	/**
	 * Open a connection to the database, using the specified user name and
	 * password.
	 * 
	 * @param userName
	 *            The user name.
	 * @param password
	 *            The user's password.
	 * @return true if the connection succeeded, false if the supplied user name
	 *         and password were not recognized. Returns false also if the JDBC
	 *         driver isn't found.
	 */
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

	/**
	 * Close the connection to the database.
	 */
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

	/* Places a new order. */
	public void createNewOrder(Order order) {
	}

	/* Marks the order with the given order number as complete. */
	public void markOrderAsComplete(int orderNbr) {
	}

	/*
	 * Returns info about all orders that are to be delivered in the given time
	 * interval.
	 */
	public String getDueOrders(int timeIntervalStart, int timeIntervalEnd) {
		return "";
	}

	/*
	 * Register a number of produced pallets and updates the ingredient database
	 * accordingly.
	 */
	public String registerProducedPallets(String product,
			String productionDate, int nbrPallets) {
		return "";
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

	/* Returns all blocked products. */
	public String getBlockedProducts() {
		return "";
	}

	/* Returns all blocked pallets. */
	public String getBlockedPallets() {
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

	/*
	 * Returns the time of the last delivery, and the delivered amount of the
	 * ingredient.
	 */
	public String getIngredientLastDeliveryInfo(String ingredient) {
		return "";
	}

	/* Returns the recipe for the given product. */
	public String getRecipe(String product) {
		return "";
	}

	/* Updates the recipe for the given product. */
	public void updateRecipe(String product, Recipe recipe) {
	}

}
