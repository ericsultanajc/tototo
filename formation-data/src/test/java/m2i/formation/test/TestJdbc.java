package m2i.formation.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import m2i.formation.model.Civilite;

public class TestJdbc {
	public static void main(String[] args) {
		preparedStatementPersonByCivility(Civilite.M);
	}

	private static void statementPersonByCivility(Civilite civ) {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/formation-data", "root", "admin");

			Statement statement = conn.createStatement();

			ResultSet rs = statement.executeQuery(
					"SELECT ID, TYPE, CIVILITY, LASTNAME, FIRSTNAME, EMAIL, BIRTHDATE, HIREDATE, EXPERIENCE, INTERNE FROM PERSON WHERE CIVILITY = '"
							+ civ + "'");

			while (rs.next()) {
				Long id = rs.getLong("ID");
				String type = rs.getString("TYPE");
				String nom = rs.getString("LASTNAME");

				System.out.println(nom);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void preparedStatementPersonByCivility(Civilite civ) {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/formation-data", "root", "admin");
			
			PreparedStatement ps = conn.prepareStatement("SELECT ID, TYPE, CIVILITY, LASTNAME, FIRSTNAME, EMAIL, BIRTHDATE, HIREDATE, EXPERIENCE, INTERNE FROM PERSON WHERE CIVILITY = ?");
			
			ps.setString(1, civ.name());
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Long id = rs.getLong("ID");
				String type = rs.getString("TYPE");
				String nom = rs.getString("LASTNAME");

				System.out.println(nom);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
