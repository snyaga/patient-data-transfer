package org.openmrs.module.patientdatatransfer.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientdatatransfer.PatientDataRequest;
import org.openmrs.module.patientdatatransfer.api.PatientDataTransferService;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.util.regex.Pattern;

@Controller
public class CommonController {

	private static Log log = LogFactory.getLog(NewRequestPageController.class);
	
	// Helper function to redirect to the error page.
	public static ModelAndView redirectToErrorPage(Exception e, String errorMessage)
	{
		ModelAndView mav = new ModelAndView("/module/patientdatatransfer/errorPage");
		mav.addObject("errorMessage", errorMessage);
		if (e != null) {
			log.error(errorMessage);
			log.error(e);
		}
		return mav;
	}

	/**
	 * Validate text coming from a "Deny Reason" text box.
	 * @param denyReason - the deny reason in String form, straight from the HTML form submission data
	 * @return a ModelAndView which will redirect to an appropriate error page. If there was no error, returns null.
	 */
	public static ModelAndView validateDenyReason(String denyReason) {
		if(denyReason == null) {
			return CommonController.redirectToErrorPage(null, "NULL encountered when checking deny reasons");
		}			
		if (!denyReason.equals("")) {	
			if (denyReason.length()>256){
				return CommonController.redirectToErrorPage(null, "One of your deny reasons is over 256 characters long");
			}
				
			if (!Pattern.matches("[0-9a-zA-Z[-'.!?,] ]+",denyReason)) {
				return CommonController.redirectToErrorPage(null, "One of your deny reasons contains invalid characters");
			}
		}
		return null;
	}


	
}
