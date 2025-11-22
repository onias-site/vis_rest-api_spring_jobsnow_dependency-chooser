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
import com.vis.services.VisServiceResume;


@CrossOrigin
@RestController
@RequestMapping("/resume/{email}")
public class VisRestApiResume{
	
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PATCH}, path = "/language/{language}")
	public Map<String, Object> save(
				@RequestBody Map<String, Object> sessionValues
			) {

		Map<String, Object> result = VisServiceResume.Save.execute(sessionValues);

		return result;
	}
	
	@DeleteMapping("/language/{language}")
	public Map<String, Object> delete(@RequestBody Map<String, Object> sessionValues){
		
		Map<String, Object> result = VisServiceResume.Delete.execute(sessionValues);

		return result;
	}

	@DeleteMapping("/status")
	public Map<String, Object> changeStatus(@RequestBody Map<String, Object> sessionValues){
		
		Map<String, Object> result = VisServiceResume.ChangeStatus.execute(sessionValues);

		return result;
	}

	@GetMapping
	public Map<String, Object> getData(@RequestBody Map<String, Object> sessionValues){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues);
		
		Map<String, Object> result = VisServiceResume.GetData.execute(json.content);

		return result;
	}

	@GetMapping("/viewMode/{viewMode}")
	public Map<String, Object> getFile(
			@PathVariable("viewMode") String viewMode, 
			@RequestBody Map<String, Object> sessionValues){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues).put(JsonFieldNames.viewMode, viewMode);
		
		Map<String, Object> result = VisServiceResume.GetFile.execute(json.content);

		return result;
	}

	@GetMapping("/oi")
	public String oi() {
		return "oi";
	}
	enum JsonFieldNames implements CcpJsonFieldName{
		viewMode
	}
}
