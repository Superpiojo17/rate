package edu.ben.rate_review.massRegistration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.StudentInCourseDao;
import edu.ben.rate_review.models.StudentInCourse;

/**
 * Class which allows students to be enrolled in courses in mass
 * 
 * @author Mike
 * @version 4-21-2017
 */
public class MassEnroll {

	private static String SEPERATOR = ",";
	private static String ENROLL_CSV = "src/main/resources/csv/EnrollStudents.csv";

	/**
	 * Reads in CSV file and enrolls students into courses
	 */
	public static void enrollStudents() {
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		String line = "";

		StudentInCourseDao sDao = DaoManager.getInstance().getStudentInCourseDao();

		try {
			fileReader = new FileReader(ENROLL_CSV);
			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String[] split = line.split(SEPERATOR);

				long course_id = 0;
				long[] student_id;

				if (split.length > 1) {
					// gets course_id
					course_id = Long.parseLong(split[0]);

					// gets student ids enrolling in that course
					student_id = new long[split.length - 1];
					for (int i = 1; i < split.length; i++) {
						student_id[i - 1] = Long.parseLong(split[i]);
					}

					// creates student in course and saves in DB
					for (int i = 0; i < student_id.length; i++) {
						StudentInCourse student = new StudentInCourse();
						student.setCourse_id(course_id);
						student.setStudent_id(student_id[i]);
						// checks if student already enrolled in class
						if (!sDao.isStudentInCourse(student)) {
							sDao.enrollStudent(student);
						}
					}
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		sDao.close();
	}
}
