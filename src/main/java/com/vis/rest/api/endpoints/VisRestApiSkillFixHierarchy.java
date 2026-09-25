package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vis.rest.open.api.VisOpenApiSkillFixHierarchy;
import com.vis.services.VisServiceSkillFixHierarchy;

/**
 * REST controller for skill hierarchy fix suggestions at path {@code /resume/{email}/skills/hierarchy}.
 * Lives under {@code /resume/*} so the session filter validates the login and fills the candidate's
 * email into the request body.
 */
@CrossOrigin
@RestController
@RequestMapping("/resume/{email}/skills/hierarchy")
public class VisRestApiSkillFixHierarchy implements VisOpenApiSkillFixHierarchy {

	@PostMapping
	public Map<String, Object> saveHierarchyFixSuggestion(@RequestBody Map<String, Object> sessionValues){
		Map<String, Object> result = VisServiceSkillFixHierarchy.FixSkillHierarchy.execute(sessionValues);
		return result;
	}

	@DeleteMapping
	public Map<String, Object> deleteHierarchyFixSuggestion(@RequestBody Map<String, Object> sessionValues){
		Map<String, Object> result = VisServiceSkillFixHierarchy.DeleteSkillFixHierarchy.execute(sessionValues);
		return result;
	}

	@PostMapping("/search")
	public Map<String, Object> getHierarchyFixSuggestion(@RequestBody Map<String, Object> sessionValues){
		Map<String, Object> result = VisServiceSkillFixHierarchy.GetSkillFixHierarchy.execute(sessionValues);
		return result;
	}

}
