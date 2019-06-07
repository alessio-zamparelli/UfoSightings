package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.ufo.model.AnnoCount;
import it.polito.tdp.ufo.model.Sighting;

public class SightingsDAO {

	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Sighting> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
							res.getString("city"), res.getString("state"), res.getString("country"),
							res.getString("shape"), res.getInt("duration"), res.getString("duration_hm"),
							res.getString("comments"), res.getDate("date_posted").toLocalDate(),
							res.getDouble("latitude"), res.getDouble("longitude")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<AnnoCount> getAnni() {
		String sql = "SELECT YEAR(datetime) as anno, COUNT(id) as cnt FROM `sighting` "
				+ "WHERE sighting.country=\"us\" GROUP BY YEAR(datetime)";
		List<AnnoCount> anni = new LinkedList<>();

		Connection conn = DBConnect.getConnection();

		try {

			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				anni.add(new AnnoCount(Year.of(rs.getInt("anno")), rs.getInt("cnt")));
			}

			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}

		return anni;

	}

	public List<String> getStati(Year anno) {
		String sql = "SELECT DISTINCT state FROM sighting WHERE country=\"us\" AND YEAR(datetime)=?";
		List<String> stati = new LinkedList<>();

		Connection conn = DBConnect.getConnection();

		try {

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno.getValue());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				stati.add(rs.getString("state"));
			}

			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();

		}

		return stati;
	}

	public boolean esisteArco(String s1, String s2, Year anno) {

		String sql = "SELECT COUNT(*) as cnt FROM sighting s1, sighting s2 "
				+ "WHERE YEAR(s1.datetime) = YEAR(s2.datetime) AND YEAR(s1.datetime) = ? "
				+ "AND s1.state = ? AND s2.state = ? AND s1.country = \"us\" "
				+ "AND s2.country = \"us\" AND s1.datetime < s2.datetime";

		Boolean ret = false;
		Connection conn = DBConnect.getConnection();

		try {

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno.getValue());
			st.setString(2, s1);
			st.setString(3, s2);

			ResultSet rs = st.executeQuery();

			if (rs.next() && rs.getInt("cnt") > 0)
				ret = true;

			conn.close();

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}

		return ret;

	}

}
