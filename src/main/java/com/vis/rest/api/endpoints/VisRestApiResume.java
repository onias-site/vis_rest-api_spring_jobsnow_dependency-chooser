package com.vis.rest.api.endpoints;

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
import com.ccp.decorators.CcpJsonRepresentation.CcpJsonFieldName;
import com.ccp.especifications.db.utils.CcpEntityCrudOperationType;
import com.ccp.especifications.mensageria.receiver.CcpBulkHandlers;
import com.jn.mensageria.JnFunctionMensageriaSender;
import com.jn.utils.JnDeleteKeysFromCache;
import com.vis.entities.VisEntityResume;
import com.vis.utils.VisUtils;


@CrossOrigin
@RestController
@RequestMapping(value = "/resume/{email}")
public class VisRestApiResume{
	
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PATCH}, path = "/language/{language}")
	public Map<String, Object> save(
			@PathVariable("language") String language,
			@PathVariable("email") String email,
			@RequestBody Map<String, Object> sessionValues) {

		Map<String, Object> result = new JnFunctionMensageriaSender(VisEntityResume.ENTITY, CcpEntityCrudOperationType.save).apply(sessionValues);

		return  result;
	}
	
	@DeleteMapping("/language/{language}")
	public Map<String, Object> delete(@RequestBody Map<String, Object> sessionValues){
		
		Map<String, Object> result = new JnFunctionMensageriaSender(VisEntityResume.ENTITY, CcpEntityCrudOperationType.delete).apply(sessionValues);

		return  result;
	}

	@DeleteMapping("/status")
	public Map<String, Object> changeStatus(@RequestBody Map<String, Object> sessionValues){
		
		Map<String, Object> result = new JnFunctionMensageriaSender(VisEntityResume.ENTITY, CcpBulkHandlers.transferToReverseEntity).apply(sessionValues);

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
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues).put(JsonFieldNames.viewMode, viewMode);
		
		CcpJsonRepresentation resume = VisUtils.getResumeFromBucket(json);

		return resume.content;
		
	}

	@GetMapping("/oi")
	public String oi() {
		return "oi";
	}
	enum JsonFieldNames implements CcpJsonFieldName{
		viewMode
	}
}
