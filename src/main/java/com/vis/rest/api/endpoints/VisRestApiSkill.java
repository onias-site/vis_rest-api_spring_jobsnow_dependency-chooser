package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vis.services.VisServiceSkills;

@CrossOrigin
@RestController
@RequestMapping("skills")
public class VisRestApiSkill {
	
	@PostMapping("/fromText")
	public Map<String, Object> getSkillsFromText(@RequestBody Map<String, Object> sessionValues){
		Map<String, Object> result = VisServiceSkills.GetSkillsFromText.execute(sessionValues);

		return result;
		
	}

}
