package edu.ben.rate_review.models;

import edu.ben.rate_review.authorization.AuthorizationUser;
import edu.ben.rate_review.controller.home.ProfessorController;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.encryption.SecurePassword;

public class User implements AuthorizationUser {
	private Long id;
	private String first_name;
	private String last_name;
	private String email;
	private String encryptedPassword;
	private String role_string;
	private String confirmed_icon;
	private String active_icon;
	private String year_string;
	private String major;
	private int school_year;
	private int role;
	private boolean confirmed;
	private boolean active;
	private String department;
	private String profilepic;
	private double overall;
	private boolean adminEditFlag;
	private String nickname;
	private String personal_email;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPersonal_email() {
		return personal_email;
	}

	public void setPersonal_email(String personal_email) {
		this.personal_email = personal_email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = SecurePassword.getHashPassword(encryptedPassword);
	}

	public void setPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	@Override
	public int getRole() {
		return role;
	}

	@Override
	public void setRole(int role) {
		this.role = role;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean hasRole(int role) {
		return this.role == role;
	}

	public String getRole_string() {
		return role_string;
	}

	public void setRole_string(String role_string) {
		if (role == 1) {
			role_string = "Admin";
		} else if (role == 2) {
			role_string = "Faculty";
		} else if (role == 3) {
			role_string = "Tutor";
		} else if (role == 4) {
			role_string = "Student";
		}

		this.role_string = role_string;
	}

	public String getActive_icon() {
		return active_icon;
	}

	public void setActive_icon(String active_icon) {
		if (active) {
			active_icon = " glyphicon glyphicon-ok icon-success";
		} else if (!active) {
			active_icon = " glyphicon glyphicon-remove icon-failure";
		}

		this.active_icon = active_icon;
	}

	public String getConfirmed_icon() {
		return confirmed_icon;
	}

	public void setConfirmed_icon(String confirmed_icon) {
		if (confirmed) {
			confirmed_icon = "glyphicon glyphicon-ok green icon-success";
		} else if (!confirmed) {
			confirmed_icon = " glyphicon glyphicon-remove icon-failure";
		}

		this.confirmed_icon = confirmed_icon;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {

		this.major = major;
	}

	public int getSchool_year() {
		return school_year;
	}

	public void setSchool_year(int school_year) {
		this.school_year = school_year;
	}

	public String getYear_string() {
		return year_string;
	}

	public void setYear_string(String year_string) {
		if (role >= 3) {
			if (school_year == 1) {
				year_string = "Freshman";
			} else if (school_year == 2) {
				year_string = "Sophomore";
			} else if (school_year == 3) {
				year_string = "Junior";
			} else if (school_year >= 4) {
				year_string = "Senior";
			}
		} else {
			year_string = "N / A";
		}

		this.year_string = year_string;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getProfilepic() {
		DaoManager dao = DaoManager.getInstance();
		UserDao ud = dao.getUserDao();
		if (ud.getPicString(id) != null) {
			return ud.getPicString(id);
		}
		return "logo";
	}

	public boolean isAdminEditFlag() {
		if (role == 4) {
			adminEditFlag = true;
		} else {
			adminEditFlag = false;
		}
		return adminEditFlag;
	}
	
	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
	}

	public void setOverall(double overall) {
		this.overall = overall;
	}

	public double getOverall() {
		return overall;
	}

}