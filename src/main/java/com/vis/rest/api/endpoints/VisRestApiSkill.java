package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vis.rest.open.api.VisOpenApiSkill;
import com.vis.services.VisServiceSkills;

/**
 * REST controller for skill operations at path {@code /skills}.
 * Allows extracting skills from free text, suggesting hierarchy corrections,
 * and requesting creation of new skills.
 */
@CrossOrigin
@RestController
@RequestMapping("skills")
public class VisRestApiSkill implements VisOpenApiSkill {
	
	@PostMapping("/fromText")
	public Map<String, Object> getSkillsFromText(@RequestBody Map<String, Object> sessionValues){
		Map<String, Object> result = VisServiceSkills.GetSkillsFromText.execute(sessionValues);
		return result;
	}

	@PostMapping("/hierarchy/readjustment")
	public Map<String, Object> saveHierarchyFixSuggestion(@RequestBody Map<String, Object> sessionValues){
		Map<String, Object> result = VisServiceSkills.FixSkillHierarchy.execute(sessionValues);
		return result;
	}

	@PostMapping("/{skill}")
	public Map<String, Object> requestToCreateNewSkill(@PathVariable("skill") String skill, @RequestBody Map<String, Object> sessionValues){
		sessionValues.put("skill", skill);
		Map<String, Object> result = VisServiceSkills.RequestToCreateNewSkill.execute(sessionValues);
		return result;
	}

}
