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

@SuppressWarnings({ "static-method" })
public class Database {
	private Connection conn;

	public Database(String url, String username, String password) {
		conn = null;
		openConnection(url, username, password);
	}

	public boolean openConnection(String url, String username,
			String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + url + "/"
					+ username, username, password);
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

			for (int i = 0; i < nbrPallets; i++) {
				insertNewPallet(productName, productionDate);
				decrementProductIngredients(productName);
			}

			conn.commit();
			result = "Successfully inserted " + nbrPallets + " pallets of "
					+ " product type " + productName + " into the database.";
		} catch (SQLException e) {
			e.printStackTrace();
			rollback();
			result = "An error occured. Insertion unsuccessful.";
		}
		return result;
	}

	@SuppressWarnings("resource")
	private void insertNewPallet(String productName, String productionDate)
			throws SQLException {

		String statement;
		PreparedStatement prepStatement = null;

		try {
			statement = "insert into Pallets(cookieName, productionDate)"
					+ " values(?,?)";
			prepStatement = conn.prepareStatement(statement);

			prepStatement.setString(1, productName);
			prepStatement.setString(2, productionDate);

			prepStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			close(prepStatement);
			throw new SQLException();
		}
		close(prepStatement);
	}

	/*
	 * Decrements the ingredient storage with the amount it takes to produce a
	 * pallet of the given product.
	 */
	@SuppressWarnings("resource")
	private void decrementProductIngredients(String productName)
			throws SQLException {
		HashMap<String, Float> ingredientAmounts = getIngredientAmounts(productName);
		PreparedStatement prepStatement = null;

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

			while (rs.next()) {
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
			String timeIntervalEnd, boolean filterBlocked) {
		String statement = "select * from pallets where cookieName = '"
				+ productType + "' and productionDate >= '" + timeIntervalStart
				+ "' and productionDate <= '" + timeIntervalEnd + "'";
		return getPalletInfoInternal(statement, filterBlocked);
	}

	/*
	 * Returns info about all pallets that were produced in the given time
	 * interval.
	 */
	public String getPalletInfo(String timeIntervalStart,
			String timeIntervalEnd, boolean filterBlocked) {
		String statement = "select * from pallets where productionDate >= '"
				+ timeIntervalStart + "' and productionDate <= '"
				+ timeIntervalEnd + "'";
		return getPalletInfoInternal(statement, filterBlocked);
	}

	/*
	 * Returns info about all pallets that contain the given product.
	 */
	public String getPalletInfo(String productType, boolean filterBlocked) {
		String statement = "select * from pallets where cookieName = '"
				+ productType + "'";
		return getPalletInfoInternal(statement, filterBlocked);
	}

	/*
	 * Returns info about the pallet with the given barcodeID.
	 */
	public String getPalletInfo(int barcodeID) {
		String statement = "select * from pallets where barcodeID = '"
				+ barcodeID + "'";
		return getPalletInfoInternal(statement, false);
	}

	/* Returns all blocked pallets. */
	public String getBlockedPallets() {
		String statement = "select * from pallets";
		return getPalletInfoInternal(statement, true);
	}

	/*
	 * Returns info about pallets delivered to the given customer. Optionally
	 * filters delivered pallets.
	 */
	public String getPalletInfoByCustomer(String customerName) {
		String statement = "select * from pallets p join orders s on p.orderID = s.orderID "
				+ "where customerName = '"
				+ customerName
				+ "' and p.deliveryDate is not null";
		return getPalletInfoInternal(statement, false);
	}

	private String getPalletInfoInternal(String statement, boolean filterBlocked) {
		PreparedStatement prepStatement = null, prepBlockStatement = null, prepCustomerStatement;
		ResultSet rs = null, blockRS = null, customerRS = null;
		PalletList pallets = new PalletList();
		String result;

		try {
			conn.setAutoCommit(false);
			prepStatement = conn.prepareStatement(statement);
			rs = prepStatement.executeQuery();

			String productionDate, productName, blockStatus, customerName, barcodeID;

			String blockQuery = "select * from blockedProducts where cookieName = ? "
					+ "and intervalStart <= ? and intervalEnd >= ?";
			String customerQuery = "select customerName from pallets p join orders o on p.orderID = o.orderID where barcodeID = ?";

			while (rs.next()) {
				productionDate = rs.getString("productionDate");
				productName = rs.getString("cookieName");
				barcodeID = rs.getString("barcodeID");

				prepBlockStatement = conn.prepareStatement(blockQuery);
				prepBlockStatement.setString(1, productName);
				prepBlockStatement.setString(2, productionDate);
				prepBlockStatement.setString(3, productionDate);

				blockRS = prepBlockStatement.executeQuery();
				if (!blockRS.next()) {
					blockStatus = "Not blocked";
				} else {
					blockStatus = "Blocked";
				}

				prepCustomerStatement = conn.prepareStatement(customerQuery);
				prepCustomerStatement.setString(1, barcodeID);
				customerRS = prepCustomerStatement.executeQuery();

				if (customerRS.next()) {
					customerName = customerRS.getString("customerName");
				} else {
					customerName = "No customer";
				}

				if (!(filterBlocked && blockStatus.equals("Not blocked"))) {
					pallets.add(new Pallet(rs.getString("barCodeID"),
							productName, rs.getString("orderID"),
							productionDate, rs.getString("deliveryDate"),
							blockStatus, customerName));
				}
			}

			conn.commit();
			if (!pallets.isEmpty()) {
				result = pallets.toString();
			} else {
				result = "No matching pallets were found.";
			}
		} catch (SQLException e) {
			rollback();
			result = "An error occured. Query unsuccessful.";
		} finally {
			close(prepStatement);
			close(prepBlockStatement);
			close(blockRS);
			close(rs);
			close(customerRS);
		}

		return result;
	}

	/* Returns all blocked products. */
	public String getBlockedProducts() {
		PreparedStatement prepStatement = null;
		ResultSet rs = null;
		String result = "";
		String statement = "select * from blockedProducts";
		try {
			conn.setAutoCommit(false);
			prepStatement = conn.prepareStatement(statement);
			rs = prepStatement.executeQuery();

			ArrayList<String> temp = new ArrayList<String>();

			String name, start, end;
			while (rs.next()) {
				name = rs.getString("cookieName");
				try {
					start = rs.getString("intervalStart");
				} catch (SQLException e) {
					start = "not specified";
				}
				try {
					end = rs.getString("intervalEnd");
				} catch (SQLException e) {
					end = "not specified";
				}
				temp.add(name + ", " + start + ", " + end + "\n");
			}

			temp.add(0, "Blocked products: " + temp.size() + "\n");
			temp.add(1,
					"Cookie name, Block interval start, Block interval end\n");

			for (int i = 0; i < temp.size(); i++) {
				result += temp.get(i);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			result = "An error occurred...";
		} finally {
			close(rs);
			close(prepStatement);
		}

		return result;
	}

	/*
	 * Blocks all pallets containing the given product made in the given time
	 * interval.
	 */
	public String blockPallets(String product, String timeIntervalStart,
			String timeIntervalEnd) {
		String statement = "insert into BlockedProducts(cookieName, intervalStart, intervalEnd) values(?,?,?)";
		PreparedStatement pstmt = null;
		String result;
		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(statement);

			pstmt.setString(1, product);
			pstmt.setString(2, timeIntervalStart);
			pstmt.setString(3, timeIntervalEnd);

			pstmt.executeUpdate();
			conn.commit();
			result = "Block successful.";
		} catch (SQLException e) {
			e.printStackTrace();
			rollback();
			result = "An error occured.";
		} finally {
			close(pstmt);
		}
		return result;
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
			if (rs != null) {
				rs.close();
			}
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

	/* Returns an array containing all the product names. */
	public String[] getProductNames() {
		String statement = "select cookieName from cookies";
		PreparedStatement prepstmt = null;
		ResultSet rs = null;
		String[] result;
		try {
			conn.setAutoCommit(false);
			prepstmt = conn.prepareStatement(statement);
			rs = prepstmt.executeQuery();

			// funkar ej:
			// conn.commit();
			// Array array = rs.getArray("cookieName");
			// result = (String[]) array.getArray();

			ArrayList<String> temp = new ArrayList<String>();
			while (rs.next()) {
				temp.add(rs.getString("cookieName"));
			}
			result = new String[temp.size()];
			for (int i = 0; i < temp.size(); i++) {
				result[i] = temp.get(i);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			rollback();
			close(prepstmt);
			close(rs);
			result = new String[] { "Fatal error!" };
		}
		return result;
	}

	public String blockPallet(int palletNbr) {
		// TODO Ska vi till�ta detta?
		return null;
	}

}
