package com.vegalabz.register;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class RegisterRobotServlet extends HttpServlet {
	Logger LOG = Logger.getLogger(RegisterRobotServlet.class.getName());
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LOG.info("RegisterRobotServlet.doGet");
		String verificationToken = "";
		String securityToken = "";
		String securityTokenFromServer = req.getParameter("st");
		if(securityToken.equals(securityTokenFromServer)){
			PrintWriter pw = resp.getWriter();
			pw.print(verificationToken);
			pw.flush();
		}
	}

}



