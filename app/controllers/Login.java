package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class Login extends Controller {
    public static Result login(String token) {
        if ("iam/presenter".equals(token)) {
            session("user", "presenter");

            return ok();
        } else
            return unauthorized();
    }
}
