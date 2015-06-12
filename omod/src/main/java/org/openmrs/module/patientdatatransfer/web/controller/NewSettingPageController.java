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

import org.openmrs.api.context.Context;
import org.openmrs.module.patientdatatransfer.PDTSetting;
import org.openmrs.module.patientdatatransfer.api.DirectoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * The main controller.
 */
@Controller
public class NewSettingPageController {

	@RequestMapping(value="/module/patientdatatransfer/newsetting", method= RequestMethod.GET)
	public void showGetNewSettingsForm(ModelMap model) {}

	@RequestMapping(value="/module/patientdatatransfer/newsetting", method= RequestMethod.POST)
	public ModelAndView showPostNewSettingsForm(
			@RequestParam(value = "settingName") String name,
			@RequestParam(value = "settingValue") String value
			) throws IOException {
		
		DirectoryService dirService = Context.getService(DirectoryService.class);
		
		// create a new PDTSetting object and populate with sanitized request parameters
		PDTSetting setting = new PDTSetting();
		
		// sanitize and add name
		if(!(Pattern.matches("[a-zA-Z[0123456789]\\.\\-:/ ]+", name)))
		{
			return CommonController.redirectToErrorPage(null, "The name should only contain spaces, letters, numbers, dashes, colons, and periods.");
		}
		setting.setName(name);
		
		// sanitize and add setting value
		if(!(Pattern.matches("[a-zA-Z[0123456789]\\.\\-:/ =\\+]+", value)))
		{
			return CommonController.redirectToErrorPage(null, "The value should only contain spaces, letters, numbers, dashes, colons, and periods.");
		}
		setting.setValue(value);
		dirService.saveSetting(setting);
		return new ModelAndView("redirect:/module/patientdatatransfer/settings.form");
	}

}