package com.mobiarch.nf.test;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@WebServlet("/ValidationTest")
public class ValidationTest extends HttpServlet {
	@Inject
	Person p1;
	@Inject
    private ValidatorFactory validatorFactory;
	@Inject
	private Validator validator; 
	Logger logger = Logger.getLogger(getClass().getName());

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
/*
		p1.setFirstName("ABCD");
		logger.info(p1.getClass().getName() + " : " + p1.getFirstName());
		validate(p1);
		
		Person p2 = new Person();
		p2.setFirstName("ABCD");
		validate(p2);
*/
		validate(Person.class, "firstName", "ABC");
		validate(Person.class, "firstName", "AB");
		validate(Person.class, "firstName", null);
	}

	public void validate(Person o) {
		/*
		ValidatorContext validatorContext = validatorFactory.usingContext();
		javax.validation.Validator beanValidator = validatorContext
				.getValidator();
		Set<ConstraintViolation<Person>> violations = validatorFactory.getValidator().validate(o);
		*/
		Set<ConstraintViolation<Person>> violations = validator.validate(o);
		System.out.println("Object is valid: " + violations.isEmpty());
	}
	public void validate(Class<?> cls, String prop, String val) {
		Set<?> violations = validator.validateValue(cls, prop, val);
		System.out.println("Prop is valid: " + violations.isEmpty());
	}
}
