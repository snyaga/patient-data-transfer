/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.patientdatatransfer.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientdatatransfer.PDTClinic;
import org.openmrs.module.patientdatatransfer.PDTSetting;
import org.openmrs.module.patientdatatransfer.api.DirectoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * The main controller.
 */
@Controller
public class SettingsPageController {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@RequestMapping(value="/module/patientdatatransfer/settings", method=RequestMethod.GET)
	public void showGetSettingsForm(
			@RequestParam(value="action", required=false) String action,
			ModelMap model) {
		
		DirectoryService dirService = Context.getService(DirectoryService.class);
		
		// handle 'Force Manual Directory Update' action
		if (action != null && action.equals("updateDirectory")) {
			log.info("Updating directory manually due to user click");
			dirService.updateDirectory();
		}
		
		// get clinics from DB
		List<PDTClinic> clinics = dirService.getClinics();

		

		
		// add settings and clinics to model so it can be displayed by JSP
		model.addAttribute("clinicsList", clinics);

	}
	
	@RequestMapping(value="/module/patientdatatransfer/settings", method=RequestMethod.POST)
	public String showPostSettingsForm(
			@RequestParam(value = "clinicsToRemoveId", required=false) String[] xclinics)
 {
		DirectoryService dirService = Context.getService(DirectoryService.class);
		if (xclinics != null) {
			for (String str_id : xclinics) {
				Integer int_id = Integer.parseInt(str_id);
				dirService.deleteClinicById(int_id);
			}
		}

				return "redirect:/module/patientdatatransfer/settings.form";
	}

}
