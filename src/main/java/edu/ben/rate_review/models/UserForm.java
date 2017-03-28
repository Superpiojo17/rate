package edu.ben.rate_review.models;

public class UserForm {
	private long id;
	private String first_name;
	private String last_name;
	private String email;
	private String role_string;
	private String year_string;
	private String major;
	private int school_year;
	private int role;

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public UserForm() {
		super();
	}

	public UserForm(User user) {
		// set your stuff
		this.first_name = user.getFirst_name();
		this.last_name = user.getLast_name();
		this.email = user.getEmail();
		this.major = user.getMajor();
		this.role = user.getRole();
		this.school_year = user.getSchool_year();
		this.id = user.getId();
		this.role_string = user.getRole_string();
		this.year_string = user.getYear_string();
	}

	public UserForm(String first_name, String last_name, String email, String role_string, String year_string,
			String major, int school_year, int role) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.role_string = role_string;
		this.year_string = year_string;
		this.major = major;
		this.school_year = school_year;
		this.role = role;
	}

	/**
	 * Create User Object from Form Data
	 * 
	 * @return
	 */
	public User build() {
		User user = new User();

		return user;
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
		if (school_year == 1) {
			year_string = "Freshman";
		} else if (role == 2) {
			year_string = "Sophomore";
		} else if (role == 3) {
			year_string = "Junior";
		} else if (role >= 4) {
			year_string = "Senior";
		}

		this.year_string = year_string;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
