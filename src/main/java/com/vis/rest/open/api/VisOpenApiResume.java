package com.vis.rest.open.api;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OpenAPI contract for resume operations at path {@code /resume/{email}}.
 * Covers saving, deleting, status changes, and data retrieval for a candidate's resume.
 * All endpoints include a path variable <b>language</b> for operations that are language-specific.
 */
@RequestMapping("/resume/{email}")
@Tag(name = "Resume", description = "Operations for managing candidate resumes: save, delete, status change, and data retrieval.")
public interface VisOpenApiResume {

	@Operation(
		summary = "Save (create or update) a resume",
		description = "When does it occur? When the candidate creates or updates their resume in the system. "
			+ "What does it do? Persists the resume data, calculates skill hashes, and triggers sending the resume to matching recruiters."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Candidate's email address. Valid email format, min 7, max 100 characters.</li>"
			+ "<li><b>language</b> – Required. Language of the resume content.</li>"
			+ "</ul>"
			+ "<b>Request body fields (JSON):</b><ul>"
			+ "<li><b>disponibility</b> – Required. Non-negative integer, max 30 (days available per month).</li>"
			+ "<li><b>ddd</b> – Required array. Brazilian area codes of interest. Min 1, max 67 items. "
			+ "Accepted values: 11, 12, 13, 14, 15, 16, 17, 18, 19, 21, 22, 24, 27, 28, 31–35, 37, 38, 41–49, "
			+ "51, 53–55, 61–69, 71, 73–75, 77, 79, 81–89, 91–99.</li>"
			+ "<li><b>desiredJob</b> – Required. String, min 2, max 50 characters.</li>"
			+ "<li><b>experience</b> – Required. Year the candidate started working professionally (max 70 years ago).</li>"
			+ "<li><b>linkedinAddress</b> – Required. Must be a valid LinkedIn profile URL "
			+ "(format: https://www.linkedin.com/in/username).</li>"
			+ "<li><b>resumeType</b> – Required. Non-negative integer. Accepted values: 1, 2, 3, 4.</li>"
			+ "<li><b>temporallyJobTime</b> – Required. Non-negative integer, max 12 (months available for temporary work).</li>"
			+ "<li><b>pj</b> or <b>clt</b> – At least one is required. Contract type salary expectations.</li>"
			+ "<li><b>btc</b> – Optional. BTC contract value, between 1,000 and 100,000.</li>"
			+ "<li><b>skill</b> – Optional array of skill objects. Each skill must contain:<ul>"
			+ "  <li><b>skill</b> – Required. Skill identifier. Min 2, max 50 characters.</li>"
			+ "  <li><b>word</b> – Required. Skill keyword as found in the text. Min 2, max 50 characters.</li>"
			+ "  <li><b>category</b> – Required. Skill category. Min 2, max 20 characters.</li>"
			+ "  <li><b>type</b> – Required. Skill type. Min 2, max 20 characters.</li>"
			+ "  <li><b>parent</b> – Optional array. Parent skill identifiers. Min 2, max 50 characters each.</li>"
			+ "  <li><b>associated</b> – Optional. Associated skill name. Min 2, max 50 characters.</li>"
			+ "</ul></li>"
			+ "<li><b>language</b> – Optional array of language objects. Each must contain:<ul>"
			+ "  <li><b>name</b> – String, min 3, max 20 characters.</li>"
			+ "  <li><b>level</b> – Non-negative integer. Accepted values: 1 (basic), 2 (advanced).</li>"
			+ "</ul></li>"
			+ "<li><b>lastJob</b> – Optional. Last job title. Min 2, max 50 characters.</li>"
			+ "<li><b>notAllowedCompany</b> – Optional array. Company names to exclude. Min 2, max 20 characters each.</li>"
			+ "<li><b>pcd</b> – Optional. Boolean. True if the candidate has a disability.</li>"
			+ "<li><b>negotiableClaim</b> – Optional. Boolean. True if salary is negotiable.</li>"
			+ "<li><b>travel</b> – Optional. Boolean. True if the candidate is available to travel.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{}")) },
			responseCode = "200",
			description = "Resume saved successfully."),
		@ApiResponse(responseCode = "400",
			description = "Validation error — one or more required fields are missing or contain invalid values."),
	})
	@PostMapping("/language/{language}")
	Map<String, Object> save(@RequestBody Map<String, Object> sessionValues);

	@Operation(
		summary = "Delete a resume",
		description = "When does it occur? When the candidate wants to permanently remove their resume. "
			+ "What does it do? Deletes the resume record from the active entity."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Candidate's email address.</li>"
			+ "<li><b>language</b> – Required. Language of the resume to delete.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{}")) },
			responseCode = "200",
			description = "Resume deleted successfully."),
	})
	@DeleteMapping("/language/{language}")
	Map<String, Object> delete(@RequestBody Map<String, Object> sessionValues);

	@Operation(
		summary = "Change resume status (deactivate)",
		description = "When does it occur? When the candidate wants to temporarily deactivate their resume. "
			+ "What does it do? Moves the resume to the inactive (twin) entity."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Candidate's email address.</li>"
			+ "<li><b>language</b> – Required. Language of the resume to deactivate.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{}")) },
			responseCode = "200",
			description = "Resume status changed (deactivated) successfully."),
	})
	@DeleteMapping("/language/{language}/status")
	Map<String, Object> changeStatus(@RequestBody Map<String, Object> sessionValues);

	@Operation(
		summary = "Get resume data",
		description = "When does it occur? When the candidate or the system needs to retrieve the candidate's resume. "
			+ "What does it do? Returns the resume data from either the active or inactive entity, whichever has a record."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Candidate's email address.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{}")) },
			responseCode = "200",
			description = "Resume data returned successfully."),
		@ApiResponse(responseCode = "404",
			description = "No resume found for the given email."),
	})
	@GetMapping
	Map<String, Object> getData(@RequestBody Map<String, Object> sessionValues);
}
