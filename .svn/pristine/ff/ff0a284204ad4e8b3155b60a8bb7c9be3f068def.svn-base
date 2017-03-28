package edu.ben.rate_review.massRegistration;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.ben.rate_review.controller.session.SessionsController;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.User;

public class MassRegistration {

	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DEFAULT_QUOTE = '"';

	private static SessionsController sessionsController = new SessionsController();

	public static void main(String[] args) throws Exception {
		massRegisterUsers();
	}

	public static void massRegisterUsers() throws FileNotFoundException, NoSuchMethodException, SecurityException {
		String csvFile = "src/main/resources/csv/dummydata.csv";

		Scanner scanner = new Scanner(new File(csvFile));
		while (scanner.hasNext()) {
			List<String> line = parseLine(scanner.nextLine());

			User tmp = new User();
			tmp.setFirst_name(line.get(0));
			tmp.setLast_name(line.get(1));
			tmp.setEmail(line.get(2));
			tmp.setPassword(line.get(3));
			tmp.setRole(Integer.parseInt(line.get(4)));

			String first_name = tmp.getFirst_name();
			String last_name = tmp.getLast_name();
			String email = tmp.getEmail();
			String password = tmp.getEncryptedPassword();
			int role = tmp.getRole();
			boolean confirmed = false;
			boolean active = true;

			UserDao user = DaoManager.getInstance().getUserDao();
			User newUser = new User();
			newUser.setFirst_name(first_name);
			newUser.setLast_name(last_name);
			newUser.setEmail(email);
			newUser.setEncryptedPassword(password);
			newUser.setRole(role);
			newUser.setConfirmed(confirmed);
			newUser.setActive(active);

			User u = user.save(newUser);
			

			System.out.println(tmp.getFirst_name() + " " + tmp.getLast_name() + " " + tmp.getEmail() + " "
					+ tmp.getEncryptedPassword() + " " + tmp.getRole());
		}

		scanner.close();

	}

	public static List<String> parseLine(String cvsLine) {
		return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
	}

	public static List<String> parseLine(String cvsLine, char separators) {
		return parseLine(cvsLine, separators, DEFAULT_QUOTE);
	}

	public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

		List<String> result = new ArrayList<>();

		// if empty, return!
		if (cvsLine == null && cvsLine.isEmpty()) {
			return result;
		}

		if (customQuote == ' ') {
			customQuote = DEFAULT_QUOTE;
		}

		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuffer curVal = new StringBuffer();
		boolean inQuotes = false;
		boolean startCollectChar = false;
		boolean doubleQuotesInColumn = false;

		char[] chars = cvsLine.toCharArray();

		for (char ch : chars) {

			if (inQuotes) {
				startCollectChar = true;
				if (ch == customQuote) {
					inQuotes = false;
					doubleQuotesInColumn = false;
				} else {

					// Fixed : allow "" in custom quote enclosed
					if (ch == '\"') {
						if (!doubleQuotesInColumn) {
							curVal.append(ch);
							doubleQuotesInColumn = true;
						}
					} else {
						curVal.append(ch);
					}

				}
			} else {
				if (ch == customQuote) {

					inQuotes = true;

					// Fixed : allow "" in empty quote enclosed
					if (chars[0] != '"' && customQuote == '\"') {
						curVal.append('"');
					}

					// double quotes in column will hit this!
					if (startCollectChar) {
						curVal.append('"');
					}

				} else if (ch == separators) {

					result.add(curVal.toString());

					curVal = new StringBuffer();
					startCollectChar = false;

				} else if (ch == '\r') {
					// ignore LF characters
					continue;
				} else if (ch == '\n') {
					// the end, break!
					break;
				} else {
					curVal.append(ch);
				}
			}

		}

		result.add(curVal.toString());

		return result;
	}

}
