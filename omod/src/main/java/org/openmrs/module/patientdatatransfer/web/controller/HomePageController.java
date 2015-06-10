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

import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * The main controller.
 */
@Controller
public class HomePageController {
	
	@RequestMapping(value="/module/patientdatatransfer/home", method=RequestMethod.GET)
	public void handleCalls(@RequestParam(value="func",required=false) String func) {
		if (func != null && func.equals("addRole")) {
			System.out.println("In addrole");
			
			Role crole = Context.getUserService().getRole("Clinician");
			crole.addPrivilege(Context.getUserService().getPrivilege("Approve Inbound PDT Requests"));
			crole.addPrivilege(Context.getUserService().getPrivilege("Approve Outbound PDT Requests"));
			crole.addPrivilege(Context.getUserService().getPrivilege("Create New PDT Requests"));
			System.out.println(crole.getPrivileges().toString());
			Context.getUserService().saveRole(crole);
		}
	}
}
