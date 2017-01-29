package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class HomeController {
	
	 /**
<<<<<<< HEAD
     * Show page to list all Users
=======
     * Show home page
>>>>>>> 2007a197e794cf3c217c859c2ae15b6b0ac6d37b
     */
    public ModelAndView showHomePage(Request req, Response res) {
        // Just a hash to pass data from the servlet to the page
        HashMap<String, Object> model = new HashMap<>();
        // Tell the server to render the index page with the data in the model
        return new ModelAndView(model, "home/home.hbs");
    }


}
