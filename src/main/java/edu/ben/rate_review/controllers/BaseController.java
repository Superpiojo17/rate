package edu.ben.rate_review.controllers;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public interface BaseController {
    public ModelAndView index(Request req, Response res);
    public ModelAndView newEntity(Request req, Response res);
    public String create(Request req, Response res);
    public ModelAndView edit(Request req, Response res);
    public String update(Request req, Response res);
    public String destroy(Request req, Response res);
}
