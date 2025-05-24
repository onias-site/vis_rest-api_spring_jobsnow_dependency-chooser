package com.ccp.vis.controller;

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
import com.vis.commons.entities.VisEntityGroupResumesByPosition;
import com.vis.commons.services.VisServicePostion;
@CrossOrigin
@RestController
@RequestMapping(value = "recruiters/{email}/positions/{title}")
public class ControllerVisPosition {

	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PATCH})
	public Map<String, Object> save(@RequestBody String sessionValues){
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues);
		Map<String, Object> result = VisServicePostion.INSTANCE.save(json).content;
		return result;
	}
	
	@DeleteMapping("/status")
	public Map<String, Object> changeStatus(@RequestBody String sessionValues){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues);
		
		Map<String, Object> result = VisServicePostion.INSTANCE.changeStatus(json).content;
	
		return result;
	}

	@GetMapping
	public Map<String, Object> getData(@RequestBody String sessionValues){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues);
		
		Map<String, Object> result = VisServicePostion.INSTANCE.getData(json).content;
	
		return result;
	}
	@GetMapping("/resumes/fromIndex/{fromIndex}/listSize/{listSize}")
	public Map<String, Object> getResumeList(
			@PathVariable("fromIndex") String fromIndex,
			@PathVariable("listSize") String listSize,
			@PathVariable("title") String title,
			@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put(VisEntityGroupResumesByPosition.Fields.from.name(), fromIndex)
				.put(VisEntityGroupResumesByPosition.Fields.listSize.name(), listSize)
				.put(VisEntityGroupResumesByPosition.Fields.title.name(), title)
				;
		
		Map<String, Object> result = VisServicePostion.INSTANCE.getResumeList(json).content;
	
		return result;
	}

	@PostMapping("/resumes/{resumeId}/viewMode/{viewMode}")
	public Map<String, Object> getResumeContent(
			@PathVariable("resumeId") String resumeId,
			@PathVariable("viewMode") String viewMode,
			@PathVariable("title") String title,
			@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put("resumeId", resumeId)
				.put("viewMode", viewMode)
				.put("title", title)
				;
		
		Map<String, Object> result = VisServicePostion.INSTANCE.getResumeContent(json).content;
	
		return result;
	}
	
	@PostMapping("/words")
	public Map<String, Object> getImportantSkillsFromText(
			@PathVariable("title") String title,
			@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put("title", title)
				;
		
		Map<String, Object> result = VisServicePostion.INSTANCE.getImportantSkillsFromText(json).content;
	
		return result;
	}
	// LATER suggestNewSkills
	@PatchMapping("/words")
	public Map<String, Object> suggestNewSkills(
			@PathVariable("title") String title,
			@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put("title", title)
				;
		
		Map<String, Object> result = VisServicePostion.INSTANCE.suggestNewSkills(json).content;
	
		return result;
	}

}
