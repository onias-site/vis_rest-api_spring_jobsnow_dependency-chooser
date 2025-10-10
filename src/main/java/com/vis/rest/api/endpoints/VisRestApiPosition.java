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
import com.ccp.decorators.CcpJsonRepresentation.CcpJsonFieldName;
import com.vis.entities.VisEntityGroupResumesByPosition;
import com.vis.services.VisServicePosition;

@CrossOrigin
@RestController
@RequestMapping("recruiters/{email}/positions/{title}")
public class VisRestApiPosition {
	enum JsonFieldNames implements CcpJsonFieldName{
		title, viewMode, resumeId
	}

	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PATCH})
	public Map<String, Object> save(@RequestBody String sessionValues){
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues);
		Map<String, Object> result = VisServicePosition.Save.execute(json.content);
		return result;
	}
	
	@DeleteMapping("/status")
	public Map<String, Object> changeStatus(@RequestBody String sessionValues){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues);
		
		Map<String, Object> result = VisServicePosition.ChangeStatus.execute(json.content);
	
		return result;
	}

	@GetMapping
	public Map<String, Object> getData(@RequestBody String sessionValues){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues);
		
		Map<String, Object> result = VisServicePosition.GetData.execute(json.content);
	
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
				.put(VisEntityGroupResumesByPosition.Fields.from, fromIndex)
				.put(VisEntityGroupResumesByPosition.Fields.listSize, listSize)
				.put(VisEntityGroupResumesByPosition.Fields.title, title)
				;
		
		Map<String, Object> result = VisServicePosition.GetResumeList.execute(json.content);
	
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
				.put(JsonFieldNames.resumeId, resumeId)
				.put(JsonFieldNames.viewMode, viewMode)
				.put(JsonFieldNames.title, title)
				;
		
		Map<String, Object> result = VisServicePosition.GetResumeContent.execute(json.content);
	
		return result;
	}
	
	@PostMapping("/words")
	public Map<String, Object> getImportantSkillsFromText(
			@PathVariable("title") String title,
			@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put(JsonFieldNames.title, title)
				;
		
		Map<String, Object> result = VisServicePosition.GetImportantSkillsFromText.execute(json.content);
	
		return result;
	}
	// LATER suggestNewSkills
	@PatchMapping("/words")
	public Map<String, Object> suggestNewSkills(
			@PathVariable("title") String title,
			@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put(JsonFieldNames.title, title)
				;
		
		Map<String, Object> result = VisServicePosition.SuggestNewSkills.execute(json.content);
	
		return result;
	}

}
