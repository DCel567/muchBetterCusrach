package org.openjfx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class UserDBHandler extends Config {
	Connection dbConnection;

	public Connection getDbConnection() throws SQLException{
		String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&serverTimezone=UTC";
		dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

		return dbConnection;
	}


	public void register_user(User user){
		String insert = "INSERT INTO " + Const.CONTACTS_TABLE + "(" +
				Const.CONTACTS_NICK + ", " + Const.CONTACTS_EMAIL + ", " +
				Const.CONTACTS_PASS + ") " + "VALUES(?, ?, ?)";

		try {
			PreparedStatement prSt = getDbConnection().prepareStatement(insert);
			prSt.setString(1, user.username);
			prSt.setString(2, user.email);
			prSt.setString(3, user.password);

			prSt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet getUser(User user) {
		ResultSet results = null;

		String select = "SELECT * FROM " + Const.CONTACTS_TABLE + " WHERE " +
				Const.CONTACTS_NICK + "=? AND " + Const.CONTACTS_PASS + "=?";

		try {
			PreparedStatement prSt = getDbConnection().prepareStatement(select);
			prSt.setString(1, user.username);
			prSt.setString(2, user.password);

			results = prSt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	public ResultSet get_user_by_name(String nick){
		ResultSet results = null;

		String select = "SELECT * FROM " + Const.CONTACTS_TABLE + " WHERE " +
				Const.CONTACTS_NICK + "=?";

		try {
			PreparedStatement prSt = getDbConnection().prepareStatement(select);
			prSt.setString(1, nick);

			results = prSt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
}
