package com.vis.rest.open.api;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OpenAPI contract for recruiter operations at path {@code /recruiter/{email}}.
 * Covers sending resumes by email, viewing seen resumes, listing positions,
 * and saving or changing opinions about resumes.
 */
@RequestMapping("recruiter/{email}")
@Tag(name = "Recruiter", description = "Operations for recruiters: managing resume interactions and job positions.")
public interface VisOpenApiRecruiter {

	/**
	 * Send resumes to email addresses.
	 * @param sessionValues the request body
	 * @param emails the {@code emails} request parameter
	 * @param resumeIds the {@code resumeIds} request parameter
	 * @return the response body
	 */
	@Operation(
		summary = "Send resumes to email addresses",
		description = "When does it occur? When the recruiter wants to share selected resumes via email. "
			+ "What does it do? Sends the specified resumes to the given email addresses."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Recruiter's email address (session owner).</li>"
			+ "</ul>"
			+ "<b>Query parameters:</b><ul>"
			+ "<li><b>emails</b> – Required. List of destination email addresses to send resumes to.</li>"
			+ "<li><b>resumeIds</b> – Required. List of resume IDs to send.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{}")) },
			responseCode = "200",
			description = "Resumes sent successfully."),
	})
	@PostMapping("/resumes/sending/email")
	Map<String, Object> sendResumesToEmail(@RequestBody String sessionValues,
			@RequestParam("emails") List<String> emails,
			@RequestParam("resumeIds") List<String> resumeIds);

	/**
	 * Change opinion about a resume.
	 * @param sessionValues the request body
	 * @param resumeId the {@code resumeId} path variable
	 * @return the response body
	 */
	@Operation(
		summary = "Change opinion about a resume",
		description = "When does it occur? When the recruiter wants to update a previously saved opinion about a resume. "
			+ "What does it do? Removes the existing opinion record, effectively toggling or clearing the opinion."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Recruiter's email address.</li>"
			+ "<li><b>resumeId</b> – Required. The unique identifier of the resume.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{}")) },
			responseCode = "200",
			description = "Opinion changed (removed) successfully."),
	})
	@PostMapping("/resumes/{resumeId}")
	Map<String, Object> changeOpinionAboutThisResume(@RequestBody String sessionValues,
			@PathVariable("resumeId") String resumeId);

	/**
	 * Save opinion about a resume.
	 * @param sessionValues the request body
	 * @param resumeId the {@code resumeId} path variable
	 * @return the response body
	 */
	@Operation(
		summary = "Save opinion about a resume",
		description = "When does it occur? When the recruiter evaluates a resume for the first time. "
			+ "What does it do? Records the recruiter's opinion about the given resume."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Recruiter's email address.</li>"
			+ "<li><b>resumeId</b> – Required. The unique identifier of the resume.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{}")) },
			responseCode = "200",
			description = "Opinion saved successfully."),
	})
	@PostMapping("/resumes/{resumeId}/opinion")
	Map<String, Object> saveOpinionAboutThisResume(@RequestBody String sessionValues,
			@PathVariable("resumeId") String resumeId);
}
