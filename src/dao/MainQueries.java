/**
 * 
 */
package dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainQueries {

	private static Connection connect;
	private static Statement stm;

	private MainQueries(){
		// Constructeur vide
	}

	/**
	 * @param query la requette a executer (Select)
	 * @return une ArrayList<HashMap<String, Object>>. Chaque case de la
	 *         ArrayList correspond a une ligne de la requette. Chaque HashMap
	 *         contient les valeurs de chaque ligne avec pour identifiant le nom
	 *         de la colonne.
	 * @throws SQLException
	 */
	public static List<HashMap<String, Object>> select(String query) throws SQLException {
		ResultSet result = null;
		ArrayList<HashMap<String, Object>> res = new ArrayList<>();

		ArrayList<String> names = new ArrayList<>();

		try{
			stm = connect();
			System.out.println("ca marche");
		}catch(SQLException e){
			stm = connect2();
		}
		if (stm.execute(query)) {
			result = stm.getResultSet();
		}

		if(result != null) {
			ResultSetMetaData rsmd = result.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String name = rsmd.getColumnName(i);
				names.add(name);
			}

			while (result.next()) {
				HashMap<String, Object> temp = new HashMap<>();
				for (String name : names) {
					temp.put(name, result.getObject(name));
				}
				res.add(temp);
			}
			result.close();
			disconnect();
		}
		return res;
	}

	private static Statement connect() throws SQLException {
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());

		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/datanova?user=root&password=&useOldAliasMetadataBehavior=true");

		return connect.createStatement();
	}

	private static Statement connect2() throws SQLException {
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());

		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/datanova?user=superuser&password=superpassword&useOldAliasMetadataBehavior=true");

		return connect.createStatement();
	}

	private static void disconnect() throws SQLException {
		connect.close();
		stm.close();
	}
}
