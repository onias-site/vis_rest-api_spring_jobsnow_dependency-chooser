package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpJsonRepresentation;
import com.vis.business.resume.VisBusinessResumeViewersOfTheMonth;
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
	 * Delete a resume for good, wherever it is. Delegates to the matching service.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@DeleteMapping("/language/{language}")
	public Map<String, Object> delete(@RequestBody Map<String, Object> sessionValues){
		
		Map<String, Object> result = VisServiceResume.Delete.execute(sessionValues);

		return result;
	}

	/**
	 * Change resume status (deactivate or reactivate). Delegates to the matching service.
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
	 * The domains of the recruiters who viewed the resume in a month. Delegates to the matching service; the month comes
	 * from the path, which the filters do not bring into the body.
	 * @param sessionValues the request body with the values of the session
	 * @param month the {@code month} path variable ({@code yyyyMM})
	 * @return the response body
	 */
	@GetMapping("/viewers/{month}")
	public Map<String, Object> getViewersOfTheMonth(@RequestBody String sessionValues, @PathVariable("month") String month) {
		CcpJsonRepresentation session = new CcpJsonRepresentation(sessionValues);
		CcpJsonRepresentation json = session.put(VisBusinessResumeViewersOfTheMonth.Fields.month, month);
		Map<String, Object> result = VisServiceResume.GetViewersOfTheMonth.execute(json.content);
		return result;
	}
}
