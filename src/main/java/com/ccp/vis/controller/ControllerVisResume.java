package com.ccp.vis.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntityCrudOperationType;
import com.ccp.especifications.mensageria.receiver.CcpBulkHandlers;
import com.jn.mensageria.JnMensageriaSender;
import com.jn.utils.JnDeleteKeysFromCache;
import com.vis.commons.entities.VisEntityResume;
import com.vis.commons.utils.VisCommonsUtils;

@CrossOrigin
@RestController
@RequestMapping(value = "/resume/{email}")
public class ControllerVisResume{
	
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PATCH}, path = "/language/{language}")
	public Map<String, Object> save(
			@PathVariable("language") String language,
			@PathVariable("email") String email,
			@RequestBody Map<String, Object> sessionValues) {

		Map<String, Object> result = new JnMensageriaSender(VisEntityResume.ENTITY, CcpEntityCrudOperationType.save).apply(sessionValues);

		return  result;
	}
	
	@DeleteMapping("/language/{language}")
	public Map<String, Object> delete(@RequestBody Map<String, Object> sessionValues){
		
		Map<String, Object> result = new JnMensageriaSender(VisEntityResume.ENTITY, CcpEntityCrudOperationType.delete).apply(sessionValues);

		return  result;
	}

	@DeleteMapping("/status")
	public Map<String, Object> changeStatus(@RequestBody Map<String, Object> sessionValues){
		
		Map<String, Object> result = new JnMensageriaSender(VisEntityResume.ENTITY, CcpBulkHandlers.transferToReverseEntity).apply(sessionValues);

		return  result;
	}

	@GetMapping
	public Map<String, Object> getData(@RequestBody Map<String, Object> sessionValues){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues);
		
		Map<String, Object> changeStatus = VisEntityResume.ENTITY.getData(json, JnDeleteKeysFromCache.INSTANCE).content;
	
		return changeStatus;
	}

	@GetMapping("/viewMode/{viewMode}")
	public Map<String, Object> getFile(
			@PathVariable("viewMode") String viewMode, 
			@RequestBody Map<String, Object> sessionValues){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues).put("viewMode", viewMode);
		
		CcpJsonRepresentation resume = VisCommonsUtils.getResumeFromBucket(json);

		return resume.content;
		
	}

	@GetMapping("/oi")
	public String oi() {
		return "oi";
	}
}
