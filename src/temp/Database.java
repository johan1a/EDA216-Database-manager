package temp;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Database is a class that specifies the interface to the movie database. Uses
 * JDBC and the MySQL Connector/J driver.
 */
public class Database {
	/**
	 * The database connection.
	 */
	private Connection conn;

	/**
	 * Create the database interface object. Connection to the database is
	 * performed later.
	 */
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

	/**
	 * Check if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	/* --- insert own code here --- */
	// a) When you click the Login button: log in with the specified username.
	public void logIn(String userId) {
	//	CurrentUser.instance().loginAs(userId);
	}

	// b) When you enter the reservation panel: show movie names in the movie
	// name list.
	public LinkedList<String> getMovieNames() {
		LinkedList<String> movieNames = new LinkedList<String>();
		try {
			String s = "SELECT movieName FROM performances;";
			PreparedStatement stmt = conn.prepareStatement(s);
			String movieName;
			ResultSet rs = stmt.executeQuery(s);
			while (rs.next()) {
				movieName = rs.getString("movieName");
				if (!movieNames.contains(movieName)) {
					movieNames.push(movieName);
					;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movieNames;
	}

	// c) When you select a movie name: show performance dates for the movie in
	// the date list.
	public LinkedList<String> getDates(String movieName) {
		LinkedList<String> dates = new LinkedList<String>();
		try {
			String s = "SELECT date FROM performances WHERE movieName = " + "'"
					+ movieName + "';";
			PreparedStatement stmt = conn.prepareStatement(s);
			String date;
			ResultSet rs = stmt.executeQuery(s);
			while (rs.next()) {
				date = rs.getString("date");
				dates.push(date);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dates;
	}

	// d) When you select a performance date: show all data concerning the
	// performance in
	// the text fields.
//	public Performance getPerformanceData(String date, String movieName) {
//		Performance performance = null;
//		try {
//			String theaterName;
//			int nbrReservations;
//			String s = "select * from performances where date = '" + date
//					+ "' and movieName = '" + movieName + "';";
//			PreparedStatement stmt = conn.prepareStatement(s);
//			ResultSet rs = stmt.executeQuery(s);
//
//			if (rs.next()) {
//				theaterName = rs.getString("theaterName");
//				nbrReservations = rs.getInt("nbrReservations");
////				performance = new Performance(movieName, theaterName, date,
////						nbrReservations);
//			} else {
//				System.out.println("miket fel wow");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return performance;
//	}

	// e) When you click Book ticket: make a ticket reservation. Two users that
	// make reservations
	// simultaneously must not interfere with each other (the code must be
	// transaction
	// safe).
	public void makeReservation(String userId, String date, String movieName) {
	//	if (CurrentUser.instance().isLoggedIn()) {
			try {
				conn.setAutoCommit(false);

				String theaterName = "";
				String s = "select theaterName from performances where date = '"
						+ date + "' and movieName = '" + movieName + "';";

				PreparedStatement prepareTheaterName = conn.prepareStatement(s);
				ResultSet theater = prepareTheaterName.executeQuery(s);
				theater.next();
				theaterName = theater.getString("theaterName");

				s = "select nbrSeats from theaters where theaterName = '"
						+ theaterName + "';";

				PreparedStatement prepareSelectSeats = conn.prepareStatement(s);
				ResultSet seats = prepareSelectSeats.executeQuery(s);

				seats.next();
				int theaterNbrSeats = seats.getInt("NbrSeats");

				s = "select nbrReservations from performances where date = '"
						+ date + "' and movieName = '" + movieName
						+ "'for update;";

				PreparedStatement stmt = conn.prepareStatement(s);
				ResultSet rs = stmt.executeQuery(s);

				rs.next();
				int reservations = rs.getInt("nbrReservations");

				if (theaterNbrSeats <= reservations) {
					conn.rollback();
					System.out.println("Inga platser kvar.......");
				} else {
					reservations++;

					s = "insert into Reservations (userName, date, movieName)"
							+ " values('" + userId + "', '" + date + "', '"
							+ movieName + "')";

					PreparedStatement prepareInsertReservation = conn
							.prepareStatement(s);

					int rows = prepareInsertReservation.executeUpdate(s);

					if (rows != 1) {
						conn.rollback();
					} else {
						s = "update Performances set nbrReservations = ?"
								+ " where movieName = '" + movieName
								+ "' and date = '" + date + "';";

						PreparedStatement prepareUpdateReservations = conn
								.prepareStatement(s);

						prepareUpdateReservations.setInt(1, reservations);

						rows = prepareUpdateReservations.executeUpdate();
						if (rows != 1) {
							conn.rollback();
						} else {
							conn.commit();
						}
					}
				}
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		//} else {
			System.out.println("A�h, nuuu s� blev de fel i reservationen!");
		//}
	}

	public boolean userExists(String userId) {
		try {
			String s = "SELECT userName FROM users WHERE userName = '" + userId
					+ "';";
			PreparedStatement stmt = conn.prepareStatement(s);
			ResultSet rs = stmt.executeQuery(s);

			while(rs.next()) {
				String name = rs.getString("userName");
				return name.equals(userId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int getNbrSeats(String theaterName) {
		try {
			String s = "SELECT NbrSeats FROM theaters WHERE theaterName = '"
					+ theaterName + "';";
			PreparedStatement stmt = conn.prepareStatement(s);
			ResultSet rs = stmt.executeQuery(s);
			if (rs.next()) {
				return rs.getInt("NbrSeats");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;

	}

}
