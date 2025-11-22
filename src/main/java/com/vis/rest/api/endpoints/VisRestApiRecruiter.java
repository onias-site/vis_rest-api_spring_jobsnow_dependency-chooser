package com.vis.rest.api.endpoints;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpJsonRepresentation.CcpJsonFieldName;
import com.vis.services.VisServiceRecruiter;


@CrossOrigin
@RestController
@RequestMapping("recruiter/{email}")
public class VisRestApiRecruiter {
	
	enum JsonFieldNames implements CcpJsonFieldName{
		resumeIds, emails, opinionType, positionStatus, resumeId
	}
	
	//FIXME CURRICULOS POR E-MAIL PARA RECRUTADORES
	@PostMapping("/resumes/sending/email")
	public Map<String, Object> sendResumesToEmail(
			@RequestParam("resumeIds") List<String> resumeIds,
			@RequestParam("emails") List<String> emails,
			@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put(JsonFieldNames.resumeIds, resumeIds)
				.put(JsonFieldNames.emails, emails)
				;
		
		Map<String, Object> execute = VisServiceRecruiter.SendResumesToEmail.execute(json.content);
		return execute;
	}

	@GetMapping("/resumes/seen/{opinionType}")
	public Map<String, Object> getAlreadySeenResumes(
			@PathVariable("opinionType") String opinionType
			,@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put(JsonFieldNames.opinionType, opinionType)
				;
		
		Map<String, Object> execute = VisServiceRecruiter.GetAlreadySeenResumes.execute(json.content);
		return execute;
	}

	//FIXME CACHE LOCAL NO COMPUTE ENGINE
	@GetMapping("/positions/{positionStatus}")
	public Map<String, Object> getPositionsFromThisRecruiter(
			@PathVariable("positionStatus") String positionStatus
			,@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put(JsonFieldNames.positionStatus, positionStatus)
				;
		
		Map<String, Object> execute = VisServiceRecruiter.GetPositionsFromThisRecruiter.execute(json.content);
		return execute;
	}
	
	@PostMapping("/resumes/{resumeId}")
	public Map<String, Object> changeOpinionAboutThisResume(
			@PathVariable("resumeId") String resumeId,
			@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put(JsonFieldNames.resumeId, resumeId)
				;
		
		Map<String, Object> execute = VisServiceRecruiter.ChangeOpinionAboutThisResume.execute(json.content);
		return execute;
	}
	@PostMapping("/resumes/{resumeId}/opinion")
	public Map<String, Object> saveOpinionAboutThisResume(
			@PathVariable("resumeId") String resumeId,
			@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put(JsonFieldNames.resumeId, resumeId)
				;
		//DOUBT SAVE DA TWIN
		Map<String, Object> execute = VisServiceRecruiter.SaveOpinionAboutThisResume.execute(json.content);
		return execute;
	}

}
