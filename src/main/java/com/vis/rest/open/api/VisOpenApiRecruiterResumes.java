package com.vis.rest.open.api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
 * {@code /recruiter/{email}/resumes-of-the-week}; also the resumes the recruiter viewed in a month (logged in) at
 * {@code /recruiter/{email}/viewed-resumes/{month}}.
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

	/**
	 * The resumes viewed by the recruiter in a month.
	 * @param sessionValues the values of the session
	 * @param month the {@code month} path variable ({@code yyyyMM})
	 * @return the response body
	 */
	@Operation(
		summary = "The resumes viewed in a month",
		description = "When does it occur? When the logged-in recruiter wants to see which resumes they opened in a month. "
			+ "What does it do? Answers, read by id with no query, the list kept by every opening of a resume and every alias given to it: "
			+ "one item per resume, a JSON text (to be parsed by the front end) with the LinkedIn of the resume, the alias and the date and time "
			+ "(dd/MM/yyyy HH:mm:ss) of the last view in that month. A month with no view answers an empty list."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. The logged-in recruiter's e-mail.</li>"
			+ "<li><b>month</b> – Required. The month, as yyyyMM (for example, 202610).</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{"
				+ "\"month\": \"202610\","
				+ "\"viewedResume\": [\"{\\\"alias\\\":\\\"Dev Java sênior\\\",\\\"date\\\":\\\"21/10/2026 14:05:31\\\",\\\"linkedin\\\":\\\"https://www.linkedin.com/in/someone\\\"}\"]"
				+ "}")) },
			responseCode = "200",
			description = "The resumes viewed in the month."),
		@ApiResponse(responseCode = "401", description = "No valid login."),
		@ApiResponse(responseCode = "422", description = "Validation error — the month is not in the format yyyyMM."),
	})
	@GetMapping("/recruiter/{email}/viewed-resumes/{month}")
	Map<String, Object> getViewedResumesOfTheMonth(@RequestBody String sessionValues, @PathVariable("month") String month);

	/**
	 * The details of a resume viewed by the recruiter in a month.
	 * @param sessionValues the request body with the values of the session
	 * @param month the {@code month} path variable ({@code yyyyMM})
	 * @return the response body
	 */
	@Operation(
		summary = "The details of a viewed resume",
		description = "When does it occur? When the logged-in recruiter clicks \"ver detalhes\" in a line of the screen of the resumes viewed in a month. "
			+ "What does it do? Finds, read by id with no query, the e-mails whose resumes use the LinkedIn and answers each active resume as a row "
			+ "of the e-mail of resumes (what matters and regions, languages and skills, role and other information), without the dates of insertion "
			+ "and of last change, which only the history of the resume has. Only resumes the recruiter viewed in that month are answered."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. The logged-in recruiter's e-mail.</li>"
			+ "<li><b>month</b> – Required. The month of the view, as yyyyMM (for example, 202610).</li>"
			+ "</ul>"
			+ "<b>Request body fields (JSON):</b><ul>"
			+ "<li><b>linkedin</b> – Required. The LinkedIn of the viewed resume, as the list of the views shows it.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{"
				+ "\"linkedin\": \"https://www.linkedin.com/in/someone\","
				+ "\"resumes\": [{\"resumeId\": \"...\", \"whatMatters\": [\"CLT: R$ 9.000,00\"], \"regions\": [\"São Paulo – SP\"], "
				+ "\"languagesAndSkills\": [\"JAVA\"], \"job\": [\"Desenvolvedor\"], \"otherInformation\": [\"PCD: não\"], \"defaultAlias\": \"JAVA_...\"}]"
				+ "}")) },
			responseCode = "200",
			description = "The rows of the active resumes that use the LinkedIn (empty when none is active anymore)."),
		@ApiResponse(responseCode = "401", description = "No valid login."),
		@ApiResponse(responseCode = "404", description = "The LinkedIn is not among the views of the recruiter in the month."),
		@ApiResponse(responseCode = "422", description = "Validation error — the month is not in the format yyyyMM or the LinkedIn is not a LinkedIn profile address."),
	})
	@PostMapping("/recruiter/{email}/viewed-resumes/{month}/details")
	Map<String, Object> getViewedResumeDetails(@RequestBody String sessionValues, @PathVariable("month") String month);
}
