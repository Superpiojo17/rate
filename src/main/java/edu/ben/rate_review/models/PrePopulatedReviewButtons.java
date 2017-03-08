package edu.ben.rate_review.models;

/**
 * Object which will return true only for the radio button which is checked. By
 * returning this model, the reviw professor form will be able to be
 * pre-populated.
 * 
 * @author Mike
 * @version 3-8-2017
 */
public class PrePopulatedReviewButtons {

	private boolean rate_1;
	private boolean rate_2;
	private boolean rate_3;
	private boolean rate_4;
	private boolean rate_5;
	
	

	public boolean getRate_1() {
		return rate_1;
	}

	public boolean getRate_2() {
		return rate_2;
	}

	public boolean getRate_3() {
		return rate_3;
	}

	public boolean getRate_4() {
		return rate_4;
	}

	public boolean getRate_5() {
		return rate_5;
	}

	/**
	 * Setter for ratings
	 * 
	 * @param rating
	 */
	public void setRating(int rating) {
		if (rating == 1) {
			rate_1 = true;
		} else if (rating == 2) {
			rate_2 = true;
		} else if (rating == 3) {
			rate_3 = true;
		} else if (rating == 4) {
			rate_4 = true;
		} else {
			rate_5 = true;
		}
	}
}
