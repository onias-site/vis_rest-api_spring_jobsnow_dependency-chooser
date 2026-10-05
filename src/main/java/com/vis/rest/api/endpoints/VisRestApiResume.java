package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpJsonRepresentation;
import com.vis.rest.open.api.VisOpenApiResume;
import com.vis.services.VisServiceResume;



/**
 * REST controller for resume operations in the VIS module at path {@code /resume/{email}}.
 * Allows saving, deleting, changing status, and retrieving the candidate's resume data by language.
 */
@CrossOrigin
@RestController
@RequestMapping("/resume/{email}")
public class VisRestApiResume implements VisOpenApiResume{
	
	/**
	 * Save (create or update) a resume. Delegates to the matching service.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@PostMapping("/language/{language}")
	public Map<String, Object> save(@RequestBody Map<String, Object> sessionValues) {

		Map<String, Object> result = VisServiceResume.Save.execute(sessionValues);

		return result;
	}
	
	/**
	 * Delete a resume. Delegates to the matching service.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@DeleteMapping("/language/{language}")
	public Map<String, Object> delete(@RequestBody Map<String, Object> sessionValues){
		
		Map<String, Object> result = VisServiceResume.Delete.execute(sessionValues);

		return result;
	}

	/**
	 * Change resume status (deactivate). Delegates to the matching service.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@DeleteMapping("/language/{language}/status")
	public Map<String, Object> changeStatus(@RequestBody Map<String, Object> sessionValues){
		
		Map<String, Object> result = VisServiceResume.ChangeStatus.execute(sessionValues);

		return result;
	}
	

	/**
	 * Get resume data. Delegates to the matching service.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@GetMapping
	public Map<String, Object> getData(@RequestBody Map<String, Object> sessionValues){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues);
		
		Map<String, Object> result = VisServiceResume.GetData.execute(json.content);

		return result;
	}

	/**
	 * Smoke test: answers {@code oi}.
	 * @return the response body
	 */
	@GetMapping("/oi")
	public String hi() {
		return "oi";
	}
	
}
