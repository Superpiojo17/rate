package edu.ben.rate_review.controller;

import edu.ben.rate_review.authorization.AuthException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public interface BaseController {
    public ModelAndView index(Request req, Response res) throws AuthException;
    public ModelAndView newEntity(Request req, Response res) throws AuthException;
    public String create(Request req, Response res) throws AuthException;
    public ModelAndView edit(Request req, Response res) throws AuthException;
    public String update(Request req, Response res) throws AuthException;
    public String destroy(Request req, Response res) throws AuthException;
}
