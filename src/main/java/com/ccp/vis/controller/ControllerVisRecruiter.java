package com.ccp.vis.controller;

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
import com.ccp.especifications.db.utils.CcpEntityCrudOperationType;
import com.ccp.especifications.mensageria.receiver.CcpBulkHandlers;
import com.jn.mensageria.JnMensageriaSender;
import com.jn.utils.JnDeleteKeysFromCache;
import com.vis.commons.business.recruiter.VisAsyncBusinessRecruiterReceivingResumes;
import com.vis.commons.entities.VisEntityGroupPositionsByRecruiter;
import com.vis.commons.entities.VisEntityGroupResumesPerceptionsByRecruiter;
import com.vis.commons.entities.VisEntityResumePerception;
@CrossOrigin
@RestController
@RequestMapping(value = "recruiter/{email}")
public class ControllerVisRecruiter {

	
	//FIXME CURRICULOS POR E-MAIL PARA RECRUTADORES
	@PostMapping("/resumes/sending/email")
	public Map<String, Object> sendResumesToEmail(
			@RequestParam("resumeIds") List<String> resumeIds,
			@RequestParam("emails") List<String> emails,
			@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put("resumeIds", resumeIds)
				.put("emails", emails)
				;
		
		CcpJsonRepresentation result = new JnMensageriaSender(VisAsyncBusinessRecruiterReceivingResumes.INSTANCE).apply(json);
	
		return result.content;
	}

	@GetMapping("/resumes/seen/{opinionType}")
	public Map<String, Object> getAlreadySeenResumes(
			@PathVariable("opinionType") String opinionType
			,@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put("opinionType", opinionType)
				;
		
		CcpJsonRepresentation result = VisEntityGroupResumesPerceptionsByRecruiter.ENTITY.getData(json, JnDeleteKeysFromCache.INSTANCE);
	
		return result.content;
	}

	//FIXME CACHE LOCAL NO COMPUTE ENGINE
	@GetMapping("/positions/{positionStatus}")
	public Map<String, Object> getPositionsFromThisRecruiter(
			@PathVariable("positionStatus") String positionStatus
			,@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put("positionStatus", positionStatus)
				;
		
		CcpJsonRepresentation result = VisEntityGroupPositionsByRecruiter.ENTITY.getData(json, JnDeleteKeysFromCache.INSTANCE);
	
		return result.content;
	}
	
	@PostMapping("/resumes/{resumeId}")
	public Map<String, Object> changeOpinionAboutThisResume(
			@PathVariable("resumeId") String resumeId,
			@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put("resumeId", resumeId)
				;
		
		CcpJsonRepresentation result = new JnMensageriaSender(VisEntityResumePerception.ENTITY, CcpBulkHandlers.transferToReverseEntity).apply(json);
	
		return result.content;
	}
	@PostMapping("/resumes/{resumeId}/opinion")
	public Map<String, Object> saveOpinionAboutThisResume(
			@PathVariable("resumeId") String resumeId,
			@RequestBody String sessionValues
			){
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(sessionValues)
				.put("resumeId", resumeId)
				;
		//DOUBT SAVE DA TWIN
		CcpJsonRepresentation result = new JnMensageriaSender(VisEntityResumePerception.ENTITY, CcpEntityCrudOperationType.save).apply(json);
	
		return result.content;
	}

}
