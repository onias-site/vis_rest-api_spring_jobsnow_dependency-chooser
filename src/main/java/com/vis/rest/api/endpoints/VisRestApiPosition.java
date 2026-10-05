package com.vis.rest.api.endpoints;

import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.ccp.decorators.CcpJsonRepresentation;
import com.vis.rest.open.api.VisOpenApiPosition;
import com.vis.services.VisServicePosition;
import com.vis.json.fields.validation.VisJsonCommonsFields;


/**
 * REST controller for job position management in the VIS module at path
 * {@code /recruiters/{email}/positions/{title}}. Allows creating/updating positions, changing status,
 * retrieving data, listing paginated resumes, and extracting/suggesting skills from position text.
 */
@CrossOrigin
@RestController
@RequestMapping("recruiters/{email}/positions/{title}")
public class VisRestApiPosition implements VisOpenApiPosition{

	/**
	 * Save (create or update) a resume. Delegates to the matching service.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PATCH})
	public Map<String, Object> save(@RequestBody String sessionValues){
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues);
		Map<String, Object> result = VisServicePosition.Save.execute(json.content);
		return result;
	}
	
	/**
	 * Change resume status (deactivate). Delegates to the matching service.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@DeleteMapping("/status")
	public Map<String, Object> changeStatus(@RequestBody String sessionValues){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues);
		
		Map<String, Object> result = VisServicePosition.ChangeStatus.execute(json.content);
	
		return result;
	}

	/**
	 * Get resume data. Delegates to the matching service.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@GetMapping
	public Map<String, Object> getData(@RequestBody String sessionValues){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues);
		
		Map<String, Object> result = VisServicePosition.GetData.execute(json.content);
	
		return result;
	}
	/**
	 * List resumes matching this position (paginated). Delegates to the matching service.
	 * @param sessionValues the request body
	 * @param fromIndex the {@code fromIndex} path variable
	 * @param listSize the {@code listSize} path variable
	 * @param title the {@code title} path variable
	 * @return the response body
	 */
	@GetMapping("/resumes/fromIndex/{fromIndex}/listSize/{listSize}")
	public Map<String, Object> getResumeList(
			@RequestBody String sessionValues,
			@PathVariable("fromIndex") String fromIndex,
			@PathVariable("listSize") String listSize,
			@PathVariable("title") String title
			){
				CcpJsonRepresentation ccpJsonRepresentation = new CcpJsonRepresentation(sessionValues);
				CcpJsonRepresentation put = ccpJsonRepresentation
				.put(VisJsonCommonsFields.from, fromIndex);
				CcpJsonRepresentation put2 = put
				.put(VisJsonCommonsFields.listSize, listSize);

				CcpJsonRepresentation json = put2
				.put(VisJsonCommonsFields.title, title)
				;
		
		Map<String, Object> result = VisServicePosition.GetResumeList.execute(json.content);
	
		return result;
	}
	
	/**
	 * Extract important skills from position text. Delegates to the matching service.
	 * @param sessionValues the request body
	 * @param title the {@code title} path variable
	 * @return the response body
	 */
	@PostMapping("/words")
	public Map<String, Object> getImportantSkillsFromText(
			@RequestBody String sessionValues,
			@PathVariable("title") String title
			){
				CcpJsonRepresentation ccpJsonRepresentation2 = new CcpJsonRepresentation(sessionValues);

				CcpJsonRepresentation json = ccpJsonRepresentation2
				.put(VisJsonCommonsFields.title, title)
				;
		
		Map<String, Object> result = VisServicePosition.GetImportantSkillsFromText.execute(json.content);
	
		return result;
	}
	/**
	 * Suggest new skills for this position. Delegates to the matching service.
	 * @param sessionValues the request body
	 * @param title the {@code title} path variable
	 * @return the response body
	 */
	@PatchMapping("/words")
	public Map<String, Object> suggestNewSkills(
			@RequestBody String sessionValues,
			@PathVariable("title") String title
			){
				CcpJsonRepresentation ccpJsonRepresentation3 = new CcpJsonRepresentation(sessionValues);

				CcpJsonRepresentation json = ccpJsonRepresentation3
				.put(VisJsonCommonsFields.title, title)
				;
		
		Map<String, Object> result = VisServicePosition.SuggestNewSkills.execute(json.content);
	
		return result;
	}

}
