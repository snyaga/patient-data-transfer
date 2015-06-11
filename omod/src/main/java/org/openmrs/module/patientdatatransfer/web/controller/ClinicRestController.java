package org.openmrs.module.patientdatatransfer.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.patientdatatransfer.PDTClinic;
import org.openmrs.module.patientdatatransfer.api.DirectoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/rest/patientdatatransfer/clinic")
public class ClinicRestController {

	/**
	 * The usual GET method that takes no parameters and returns all the clinics.
	 * 
	 * @param request
	 * @return a list of PDTClinic's
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public Object retrieve(HttpServletRequest request) {
		DirectoryService dirService = Context.getService(DirectoryService.class);
		List<PDTClinic> clinic = dirService.getClinics();
		return clinic;
	}
	
}
