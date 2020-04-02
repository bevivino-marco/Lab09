package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public Map<Integer , Country> loadAllCountries(Map<Integer , Country> mappa) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		Map <Integer, Country> result = new HashMap<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if (!mappa.containsKey(rs.getInt("ccode"))) {
				System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
			    result.put(rs.getInt("ccode"), new Country (rs.getInt("ccode"), rs.getString("StateAbb"),rs.getString("StateNme")));
				}
		    }
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public Map<Integer,Border> getCountryPairs(int anno, Map <Integer, Border> mappaB, Map<Integer , Country> mappaC) {
		String sql = "SELECT dyad,state1no,state1ab,state2no,state2ab,year FROM contiguity  WHERE  YEAR<=?";
		Map <Integer, Border> result = new HashMap<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if (!mappaB.containsKey(rs.getInt("dyad")) && mappaC.containsKey(rs.getInt("state1no")) && mappaC.containsKey(rs.getInt("state2no"))){
				//System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
			    result.put(rs.getInt("dyad"),new Border (rs.getInt("dyad"), mappaC.get(rs.getInt("state1no")), mappaC.get(rs.getInt("state2no")), rs.getInt("year")));
				}
		    }
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
