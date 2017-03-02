package edu.ben.rate_review.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.User;

public class AnnouncementDao{
	
	String ANNOUNCEMENTS_TABLE = "announcements";
	Connection conn = null;
	
	/**
	 * UserDao connection
	 * 
	 * @param conn
	 */
	public AnnouncementDao(Connection conn) {
		this.conn = conn;
	}
	
	private Announcement mapRow(ResultSet rs) throws SQLException {

		// Create user object and pass to array
		Announcement tmp = new Announcement();
		tmp.setDate(rs.getString("announcement_date"));
		tmp.setAnnouncement_content(rs.getString("announcement_content"));

		return tmp;
	}
	
	
	public Announcement save(Announcement announcement) {
		final String sql = "INSERT INTO " + ANNOUNCEMENTS_TABLE
				+ " (announcement_date, announcement_content) VALUES(?,?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, announcement.getDate());
			ps.setString(2, announcement.getAnnouncement_content());
			;
			ps.executeUpdate();
			return announcement;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public Announcement findById(long id) {
		// Declare SQL template query
		String sql = "SELECT * FROM " + ANNOUNCEMENTS_TABLE + " WHERE announcement_id = ? LIMIT 1";
		try {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setLong(1, id);

			// Run your shit
			ResultSet rs = q.executeQuery();
			if (rs.next()) {
				return mapRow(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// If you don't find a model
		return null;

	}
	
	/**
	 * 
	 * @return all users from the database.
	 */

	public List<Announcement> all() {
		final String SELECT = "SELECT * FROM " + ANNOUNCEMENTS_TABLE;
		List<Announcement> announcements = null;
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			announcements = new ArrayList<Announcement>();
			try {
				ResultSet rs = ps.executeQuery(SELECT);
				while (rs.next()) {
					announcements.add(mapRow(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return announcements;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return announcements;
	}
	

}
