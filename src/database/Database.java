package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import cookieProduction.Order;

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
			String productionDate, String productionTime, int amount) {
		String statement;
		PreparedStatement prepStatement;
		try {
			
			
			//minska ingrediensmängd
			
			statement = "insert into pallets "
					+ "(cookieName,orderID,productionDate,"
					+ "productionTime,delivDate,deliveryTime)"
					+ " values(?,?,?,?,?,?)";
			prepStatement = conn
					.prepareStatement(statement);
			
			
			prepStatement.setString(1, "blabla");
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

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

	/*
	 * Returns the amount of the given ingredient left in the ingredient
	 * storage.
	 */
	public int decrementIngredientAmount(String ingredient, int amount) {
		return 0;
	}

	/* Returns the recipe for the given product. */
	public String getRecipe(String product) {
		return "";
	}
}
