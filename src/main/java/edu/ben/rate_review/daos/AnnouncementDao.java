package edu.ben.rate_review.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.zaxxer.hikari.HikariDataSource;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.AnnouncementForm;

/**
 * This dao manages the announcements table in the data base where the
 * announcements on the left hand sides of many pages are found
 *
 * @author Joel
 */
public class AnnouncementDao extends BaseDao implements Dao<Announcement> {

	// table variable
	private final String ANNOUNCEMENTS_TABLE = "announcements";

	public AnnouncementDao(HikariDataSource db) {
		super(db);
	}

	/**
	 * Creates announcements
	 *
	 * @param rs
	 * @return announcement object
	 * @throws SQLException
	 */
	private Announcement mapRow(ResultSet rs) throws SQLException {

		// Create user object and pass to array
		Announcement tmp = new Announcement();
		tmp.setDate(rs.getString("announcement_date"));
		tmp.setAnnouncement_content(rs.getString("announcement_content"));
		tmp.setId(rs.getLong("announcement_id"));

		return tmp;
	}

	/**
	 * SQL method to update an announcement
	 *
	 * @param announcement
	 * @return AnnouncementForm
	 */
	public AnnouncementForm updateAnnouncement(AnnouncementForm announcement) {
		String sql = "UPDATE " + ANNOUNCEMENTS_TABLE
				+ " SET announcement_date = ?, announcement_content = ? WHERE announcement_id = ? LIMIT 1";

		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setString(1, announcement.getDate());
			ps.setString(2, announcement.getAnnouncement_content());
			ps.setLong(3, announcement.getId());

			// Runs query
			ps.execute();
			ps.close();
			return announcement;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	/**
	 * Saves an announcement
	 *
	 * @param announcement
	 * @return an annoucnemnt object
	 */
	public Announcement save(Announcement announcement) {
		final String sql = "INSERT INTO " + ANNOUNCEMENTS_TABLE
				+ " (announcement_date, announcement_content) VALUES(?,?)";
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, announcement.getDate());
			ps.setString(2, announcement.getAnnouncement_content());
			ps.executeUpdate();
			ps.close();
			return announcement;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * A method to find an announcement in the db
	 *
	 * @param id
	 * @return
	 */
	public Announcement findById(long id) {
		return find(id);
	}

	/**
	 * deletes announcement from database by using the announcement ID
	 *
	 * @param id
	 * @return
	 */
	public Boolean deleteAnnouncement(long id) {

		String sql = "DELETE FROM " + ANNOUNCEMENTS_TABLE + " WHERE announcement_id = ? LIMIT 1";

		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
			// Runs query
			ps.execute();
			ps.close();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @return all anouncements from the database.
	 * @throws ParseException
	 */
	public List<Announcement> all() {
		final String SELECT = "SELECT * FROM " + ANNOUNCEMENTS_TABLE + " order by announcement_date asc";
		List<Announcement> announcements = new ArrayList<>();

		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			ResultSet rs = ps.executeQuery(SELECT);
			while (rs.next()) {
				announcements.add(mapRow(rs));
			}
			ps.close();
			return announcements;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return announcements;
	}

	@Override
	public Announcement find(Long id) {
		String sql = "SELECT * FROM " + ANNOUNCEMENTS_TABLE + " WHERE announcement_id = ? LIMIT 1";
		Announcement announcement = null;
		PreparedStatement q;
		try (Connection conn = this.db.getConnection()) {
			q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setLong(1, id);

			// Run your shit
			ResultSet rs = q.executeQuery();
			if (rs.next()) {
				announcement = mapRow(rs);
				q.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return announcement;
	}

}
