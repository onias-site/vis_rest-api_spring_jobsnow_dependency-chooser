package com.vis.rest.open.api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OpenAPI contract for the actions of the recruiter that come from the daily e-mail of resumes: the links of the
 * e-mail (signed for the recruiter, no login) at {@code /recruiter-email-link}, and the screen "see all" (logged in) at
 * {@code /recruiter/{email}/resumes-of-the-week}.
 */
@Tag(name = "Recruiter Resumes", description = "The daily e-mail of resumes to the recruiters: opening a resume, giving it an alias, unsubscribing and seeing every resume of the last e-mail.")
public interface VisOpenApiRecruiterResumes {

	/**
	 * Open a resume of the e-mail.
	 * @param body the request body
	 * @return the response body
	 */
	@Operation(
		summary = "Open a resume of the e-mail",
		description = "When does it occur? When the recruiter confirms, in the front end, that they want to open a resume of the e-mail of resumes "
			+ "(the confirmation keeps the robots of the e-mail providers, which click every link, from counting as a view). "
			+ "What does it do? Saves the view of the resume by the recruiter (the date of the last view comes from its history) with the alias "
			+ "the recruiter already gave to it, or a suggested one, and answers the LinkedIn of the resume, opened by the front end in a new tab, and the alias."
			+ "<br/><br/><b>Request body fields (JSON):</b><ul>"
			+ "<li><b>email</b> – Required. The recruiter's e-mail, as in the link.</li>"
			+ "<li><b>resumeId</b> – Required. The id of the resume, as in the link.</li>"
			+ "<li><b>token</b> – Required. The signature of the link.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{"
				+ "\"linkedinAddress\": \"https://www.linkedin.com/in/someone\","
				+ "\"alias\": \"JAVA_SPRING_SQL_R$ 9.000,00_São Paulo – SP_12 Anos\""
				+ "}")) },
			responseCode = "200",
			description = "View saved."),
		@ApiResponse(responseCode = "401", description = "The link was not signed for this recruiter and resume."),
		@ApiResponse(responseCode = "404", description = "The resume is no longer active."),
		@ApiResponse(responseCode = "422", description = "Validation error — one or more required fields are missing or contain invalid values."),
	})
	@PostMapping("/recruiter-email-link/view-resume")
	Map<String, Object> viewResume(@RequestBody Map<String, Object> body);

	/**
	 * Save the alias of a resume of the e-mail.
	 * @param body the request body
	 * @return the response body
	 */
	@Operation(
		summary = "Save the alias of a resume of the e-mail",
		description = "When does it occur? When the recruiter, after opening a resume of the e-mail, clicks \"save view\". "
			+ "What does it do? Saves the alias the recruiter gave to the resume: the system does not keep the resume in a readable form, so the "
			+ "next e-mails show the resume by this alias."
			+ "<br/><br/><b>Request body fields (JSON):</b><ul>"
			+ "<li><b>email</b> – Required. The recruiter's e-mail, as in the link.</li>"
			+ "<li><b>resumeId</b> – Required. The id of the resume, as in the link.</li>"
			+ "<li><b>token</b> – Required. The signature of the link.</li>"
			+ "<li><b>alias</b> – Required. The alias of the resume. Min 5, max 200 characters.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Alias saved."),
		@ApiResponse(responseCode = "401", description = "The link was not signed for this recruiter and resume."),
		@ApiResponse(responseCode = "404", description = "The resume is no longer active."),
		@ApiResponse(responseCode = "422", description = "Validation error — one or more required fields are missing or contain invalid values."),
	})
	@PostMapping("/recruiter-email-link/alias")
	Map<String, Object> saveAlias(@RequestBody Map<String, Object> body);

	/**
	 * Stop receiving the e-mail of resumes.
	 * @param body the request body
	 * @return the response body
	 */
	@Operation(
		summary = "Stop receiving the e-mail of resumes",
		description = "When does it occur? When the recruiter confirms, in the front end, the link \"click here\" of the question about no longer "
			+ "receiving the e-mail. "
			+ "What does it do? Moves the recruiter to the inactive recruiters; a recruiter already inactive gets the same answer."
			+ "<br/><br/><b>Request body fields (JSON):</b><ul>"
			+ "<li><b>email</b> – Required. The recruiter's e-mail, as in the link.</li>"
			+ "<li><b>token</b> – Required. The signature of the link.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "The recruiter no longer receives the e-mail."),
		@ApiResponse(responseCode = "401", description = "The link was not signed for this recruiter."),
		@ApiResponse(responseCode = "422", description = "Validation error — one or more required fields are missing or contain invalid values."),
	})
	@PostMapping("/recruiter-email-link/unsubscribe")
	Map<String, Object> unsubscribe(@RequestBody Map<String, Object> body);

	/**
	 * Every resume of the last e-mail.
	 * @param sessionValues the session values
	 * @return the response body
	 */
	@Operation(
		summary = "Every resume of the last e-mail",
		description = "When does it occur? When the logged-in recruiter opens the screen \"see all\" of the e-mail of resumes, which shows only the first 40. "
			+ "What does it do? Answers every resume of the last e-mail of the recruiter, in the order and with the texts of the e-mail. The list "
			+ "is written before the e-mail goes out and kept for a day."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. The logged-in recruiter's e-mail.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "The resumes of the last e-mail."),
		@ApiResponse(responseCode = "401", description = "No valid login."),
		@ApiResponse(responseCode = "404", description = "No e-mail of resumes for this recruiter in the last day."),
	})
	@GetMapping("/recruiter/{email}/resumes-of-the-week")
	Map<String, Object> getResumesOfTheWeek(@RequestBody Map<String, Object> sessionValues);
}
