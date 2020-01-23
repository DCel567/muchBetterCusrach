package org.openjfx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class PointsDBHandler extends Config {
	Connection dbConnection;

	public Connection getDbConnection() throws SQLException{
		String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&serverTimezone=UTC";
		dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

		return dbConnection;
	}

	public void add_point(Point point){
		String insert = "INSERT INTO " + Const.POINTS_TABLE + "(" +
				Const.POINTS_X + ", " + Const.POINTS_Y + ") " + "VALUES(?, ?)";

		try {
			PreparedStatement prSt = getDbConnection().prepareStatement(insert);
			prSt.setDouble(1, point.getX());
			prSt.setDouble(2, point.getY());

			prSt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet getPoints() {
		ResultSet results = null;

		String select = "SELECT * FROM " + Const.POINTS_TABLE;

		try {
			PreparedStatement prSt = getDbConnection().prepareStatement(select);
			results = prSt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	public void clear_table(){
		String delete = "DELETE FROM " + Const.POINTS_TABLE;
		String zeroAutoIncrement = "ALTER TABLE " + Const.POINTS_TABLE + " AUTO_INCREMENT=0";

		try {
			PreparedStatement prSt1 = getDbConnection().prepareStatement(delete);
			PreparedStatement prSt2 = getDbConnection().prepareStatement(zeroAutoIncrement);
			prSt1.execute();
			prSt2.execute();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
}
