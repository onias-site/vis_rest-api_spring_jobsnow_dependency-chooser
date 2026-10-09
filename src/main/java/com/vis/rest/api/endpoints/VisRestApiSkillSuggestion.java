package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vis.rest.open.api.VisOpenApiSkillSuggestion;
import com.vis.services.VisServiceSkillSuggestion;

/**
 * REST controller for skill suggestions at path {@code /resume/{email}/skills/suggestion}.
 * Lives under {@code /resume/*} so the session filter validates the login and fills the candidate's
 * email into the request body.
 */
@CrossOrigin
@RestController
@RequestMapping("/resume/{email}/skills/suggestion")
public class VisRestApiSkillSuggestion implements VisOpenApiSkillSuggestion {

	/**
	 * Suggest a skill. Delegates to the matching service.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@PostMapping
	public Map<String, Object> saveSkillSuggestion(@RequestBody Map<String, Object> sessionValues){
		Map<String, Object> result = VisServiceSkillSuggestion.SuggestSkill.execute(sessionValues);
		return result;
	}

	/**
	 * Withdraw a pending skill suggestion. Delegates to the matching service.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@DeleteMapping
	public Map<String, Object> deleteSkillSuggestion(@RequestBody Map<String, Object> sessionValues){
		Map<String, Object> result = VisServiceSkillSuggestion.DeleteSkillSuggestion.execute(sessionValues);
		return result;
	}

	/**
	 * Get the candidate's suggestion of a skill. Delegates to the matching service.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@PostMapping("/search")
	public Map<String, Object> getSkillSuggestion(@RequestBody Map<String, Object> sessionValues){
		Map<String, Object> result = VisServiceSkillSuggestion.GetSkillSuggestion.execute(sessionValues);
		return result;
	}

}
