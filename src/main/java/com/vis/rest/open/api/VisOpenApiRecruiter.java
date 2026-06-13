package com.vis.rest.open.api;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
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

	@Operation(
		summary = "Get already seen resumes by opinion type",
		description = "When does it occur? When the recruiter wants to view resumes they have already evaluated. "
			+ "What does it do? Returns a grouped list of resumes the recruiter has seen, filtered by opinion type."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Recruiter's email address.</li>"
			+ "<li><b>opinionType</b> – Required. The type of opinion used to filter resumes (e.g., 'liked', 'disliked').</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{}")) },
			responseCode = "200",
			description = "List of already-seen resumes returned grouped by opinion type."),
	})
	@GetMapping("/resumes/seen/{opinionType}")
	Map<String, Object> getAlreadySeenResumes(@RequestBody String sessionValues,
			@PathVariable("opinionType") String opinionType);

	@Operation(
		summary = "Get positions from this recruiter",
		description = "When does it occur? When the recruiter accesses their positions dashboard. "
			+ "What does it do? Returns a grouped list of positions belonging to this recruiter, filtered by status."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Recruiter's email address.</li>"
			+ "<li><b>positionStatus</b> – Required. Status filter for positions (e.g., 'active', 'inactive').</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{}")) },
			responseCode = "200",
			description = "List of positions returned grouped by status."),
	})
	@GetMapping("/positions/{positionStatus}")
	Map<String, Object> getPositionsFromThisRecruiter(@RequestBody String sessionValues,
			@PathVariable("positionStatus") String positionStatus);

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
