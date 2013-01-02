package com.mobiarch.file;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import com.mobiarch.nf.Context;
import com.mobiarch.nf.Controller;
import com.mobiarch.nf.Path;
import com.mobiarch.nf.PropertyViolation;

@Named("upload")
@RequestScoped
public class UploadController extends Controller {
	private String notes;
	private Part fileToUpload;
	
	@Inject
	Context context;
	
	@Path("/ajax-upload")
	public String ajaxUpload() throws Exception {
		InputStream is = context.getRequest().getInputStream();
		dumpFile(is);
		
		return null; //Return no content
	}
	
	@Path("/form-upload")
	public String formUpload() throws Exception {
		if (fileToUpload.getSize() == 0) {
			//No file was selected
			context.addViolation(new PropertyViolation("Please choose a file to upload<br/>", "fileToUpload"));
			
			return "upload_form"; //Redisplay form
		}
		InputStream is = fileToUpload.getInputStream();
		
		dumpFile(is);
		
		return "";
	}

	public String index() {
		return "upload_form";
	}
	
	private void dumpFile(InputStream is) throws Exception {
		System.out.println("Notes:");
		System.out.println(notes);
		System.out.println("Uploaded file contents:");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Part getFileToUpload() {
		return fileToUpload;
	}
	public void setFileToUpload(Part fileToUpload) {
		this.fileToUpload = fileToUpload;
	}
}
