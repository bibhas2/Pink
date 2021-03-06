package com.mobiarch.nf;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Controller
 */
@MultipartConfig
public class PinkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Inject
	Processor processor;
	Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processor.process(request, response);
		} catch (Exception e) {
			if (!response.isCommitted()) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			logger.log(Level.SEVERE, "Error processing request.", e);
		}
	}
}
