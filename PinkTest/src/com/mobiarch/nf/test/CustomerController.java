package com.mobiarch.nf.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mobiarch.nf.Context;
import com.mobiarch.nf.Path;

@Named("customers")
@RequestScoped
public class CustomerController implements Serializable {
	private static final long serialVersionUID = 8894086705176801502L;
	Logger logger = Logger.getLogger(getClass().getName());
	@Inject
	Context context;
	Customer customer = new Customer();
	private static HashMap<String, Customer> customerList = new HashMap<String, Customer>();
	private static int nextId = 0;
	private ArrayList<Department> departments = new ArrayList<Department>();
	
	@PostConstruct
	public void onCreate() {
		logger.info("onCreate()");
		departments.add(new Department("Finance", 1));
		departments.add(new Department("Engineering", 2));
		departments.add(new Department("Manufactuering", 3));
		departments.add(new Department("Sales", 4));
	}

	public String index() {
		return "@list";
	}

	@Path("customer.id")
	public String show() {
		logger.fine("showForm() called with id: " + customer.getId());

		customer = customerList.get(customer.getId());

		if (customer != null) {
			return "show_customer";
		} else {
			return "invalid_customer";
		}
	}

	public String register() {
		if (context != null && context.isPostBack() && context.isValidationFailed() == false) {
			logger.fine("Registering: " + customer.getFullName() + " Email: " + customer.getEmail());

			String id = String.valueOf(++nextId);
			customer.setId(id);
			customerList.put(id, customer);

			return "show?customer.id=" + id;
		} else {
			return "show_add_form";
		}
	}

	@Path("/update/customer.id")
	public String updateCustomer() {
		if (context.isPostBack() && context.isValidationFailed() == false) {
			logger.fine("Updating: " + customer.getId());
			customerList.put(customer.getId(), customer);
			
			return "list";
		} else {
			logger.fine("showForm() called with id: " + customer.getId());

			if (!context.isPostBack()) {
				customer = customerList.get(customer.getId());
			}
			return "show_edit_form";
		}
	}

	@Path("customer.id")
	public String delete() {
		logger.fine("Deleting: " + customer.getId());

		customerList.remove(customer.getId());

		return "@list";
	}

	public Object[] getCustomerList() {
		logger.fine("Returning list: " + customerList.size());
		return customerList.values().toArray();
	}

	public String list() {
		return "show_customer_list";
	}

	public ArrayList<Department> getDepartments() {
		return departments;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
