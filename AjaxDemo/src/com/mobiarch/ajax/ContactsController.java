package com.mobiarch.ajax;

import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mobiarch.nf.Context;
import com.mobiarch.nf.Controller;

@Named("contacts")
@RequestScoped
public class ContactsController extends Controller {
	private static ArrayList<Contact> contactList = new ArrayList<Contact>();
	private Contact contact = new Contact();

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact subscription) {
		this.contact = subscription;
	}
	
	@Inject
	Context context;
	
	public Object index() {
		if (context.isPostBack()) {
			if (context.isValidationFailed()) {
				return new Response("FAILED", context.getViolations());
			} else {
				System.out.println("Successfully added contact!");
				contactList.add(contact);
				return new Response("SUCCESS");
			}
		} else {
			return "form";
		}
	}
	public Object list() {
		return contactList;
	}
}
