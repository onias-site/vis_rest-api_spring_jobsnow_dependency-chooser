package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.json.fields.validation.CcpJsonCommonsFields;
import com.ccp.service.CcpCachedService;
import com.vis.rest.open.api.VisOpenApiSkill;
import com.vis.services.VisServiceSkills;

/**
 * REST controller for skill operations at path {@code /skills}.
 * Allows extracting skills from free text.
 * Hierarchy corrections live in {@link VisRestApiSkillFixHierarchy} and skill suggestions in
 * {@link VisRestApiSkillSuggestion}.
 */
@CrossOrigin
@RestController
@RequestMapping("skills")
public class VisRestApiSkill implements VisOpenApiSkill {
	
	/**
	 * Extract skills from free text. Delegates to the matching service.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@PostMapping("/fromText")
	public Map<String, Object> getSkillsFromText(@RequestBody Map<String, Object> sessionValues){
		
		CcpCachedService ccd = new CcpCachedService(CcpJsonCommonsFields.text, VisServiceSkills.GetSkillsFromText, 3_600_000);
		
		Map<String, Object> result = ccd.execute(sessionValues);
		return result;
	}

}
