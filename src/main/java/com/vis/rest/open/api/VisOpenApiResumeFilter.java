package com.vis.rest.open.api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OpenAPI contract for the search of resumes by filters (screen "Currículos - Buscar" and the position form) at
 * {@code /resume-filter}: public, with no session.
 */
@Tag(name = "Resume filter", description = "The search of resumes by the filters of a position: the count of the resumes of the filters, cached for 1 hour under the id of the filters, and the filters of an id.")
public interface VisOpenApiResumeFilter {

	/**
	 * The resumes of the filters chosen.
	 * @param filters the request body with the filters
	 * @return the response body
	 */
	@Operation(
		summary = "Resumes of the filters chosen",
		description = "When does it occur? When the user, logged in or not, adds, changes or removes any filter of the screen \"Currículos - Buscar\" or of the position form. Public, with no session. "
			+ "What does it do? The id of the filters (vis_resume_filter, discarded with the hour) is the key of the cache of their count: filters counted in the last hour are answered from the cache, with no search. "
			+ "Otherwise the filters are saved and their insertion runs the search of the position form (the groups of active resumes of each ddd and of the area, filtered by a Painless script inside Elasticsearch), caching the count for 1 hour. "
			+ "Answers the id of the filters (which the screen puts in its address, to read the filters back with GET /resume-filter/{id}), the ids of the resumes found (SHA-1 of the e-mail), how many they are and the averages of their CLT and PJ claims and of their years of experience. "
			+ "A filter left out does not filter."
			+ "<br/><br/><b>Request body fields (JSON), the fields of the position that filter the resumes, with the validations of the position:</b><ul>"
			+ "<li><b>resumeType</b> – Required, the only one. The area (1 information technology, 2 recruitment and selection, 3 other higher education professions, 4 operational professions).</li>"
			+ "<li><b>ddd</b> – 1 to 68 ddds; 10 (home office) or absent, every ddd.</li>"
			+ "<li><b>disponibility</b>, <b>temporallyJobTime</b> – The resumes that start in up to these days (0 to 30) / accept up to these months of temporary job (0 to 12); 0, no filter.</li>"
			+ "<li><b>seniority</b> – List of 1 to 6 of estag, junior, pleno, senior, especialista or unknown: the years of experience in the range of any of them.</li>"
			+ "<li><b>skill</b> – List of skills as the position keeps them, {skill, word, type, category}: the ones of the category skillsFromText are required, each one found in the resume or in a skill below it in the hierarchy.</li>"
			+ "<li><b>spokenLanguage</b> – List of {name, level}: each language at least in the level.</li>"
			+ "<li><b>negotiableClaim</b>, <b>pcd</b>, <b>travel</b> – True, only the resumes that have them.</li>"
			+ "<li><b>clt</b>, <b>pj</b> – {min, max} from 1,000 to 100,000: the claim inside the range.</li>"
			+ "<li><b>recruiterDomain</b> – The domain of the logged recruiter: the resumes that forbid it are left out.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = { @Content(schema = @Schema(example = "{\"id\": \"5f2c9e\", \"ids\": [\"a1b2\"], \"resumes\": 1, \"cltAverage\": 8000.0, \"pjAverage\": 12000.0, \"experienceAverage\": 6.5}")) }, responseCode = "200", description = "The id of the filters, the ids of the resumes found, how many they are and the averages of their claims and years of experience (each average absent when no resume found informed it)."),
		@ApiResponse(responseCode = "422", description = "Validation error — no area, an unknown area, ddd or seniority, a repeated ddd, an invalid skill or an invalid range."),
	})
	@PostMapping("/resume-filter")
	Map<String, Object> searchResumes(@RequestBody Map<String, Object> filters);

	/**
	 * The filters of an id.
	 * @param id the id of the filters
	 * @return the response body
	 */
	@Operation(
		summary = "Filters of an id",
		description = "When does it occur? When the screen \"Currículos - Buscar\" is opened with the id of the filters in its address (the link the screen itself made). Public, with no session. "
			+ "What does it do? Reads the filters of the id in vis_resume_filter, one read by id, so the screen fills them in. "
			+ "With the e-mail too (the link shared with the text of the position), the filters and the text that e-mail shared with them (vis_resume_filter_description) are read in one union all, and the text goes back in description; the screen fills its field in and shows the skills found in it. "
			+ "An e-mail that is not valid is ignored, as if the link had none."
			+ "<br/><br/><b>Path variables:</b><ul><li><b>id</b> – Required. The id the search of the filters answered.</li></ul>"
			+ "<b>Query parameters:</b><ul><li><b>email</b> – Optional. The e-mail of the user who shared the link with the text of the position.</li></ul>"
	)
	@ApiResponses({
		@ApiResponse(content = { @Content(schema = @Schema(example = "{\"resumeType\": 1, \"ddd\": [11, 21], \"seniority\": [\"pleno\"], \"description\": \"Desenvolvedor Java com Spring Boot...\"}")) }, responseCode = "200", description = "The filters, as the screen sent them to the search, plus description when the e-mail of the link shared a text with them."),
		@ApiResponse(responseCode = "404", description = "No filters of this id: they expired with their hour or never existed. The screen does nothing."),
		@ApiResponse(responseCode = "422", description = "Validation error — an id that is not hexadecimal or longer than 40 characters."),
	})
	@GetMapping("/resume-filter/{id}")
	Map<String, Object> getResumeFilter(@PathVariable("id") String id, @RequestParam(value = "email", required = false) String email);

	/**
	 * The text of the position shared with the link of the search.
	 * @param sessionValues the request body with the values of the session, the id of the filters and the text
	 * @return the response body
	 */
	@Operation(
		summary = "Text of the position shared with the link of the search",
		description = "When does it occur? When the user logged in clicks \"Copiar este link para enviar para alguém\" in the screen \"Currículos - Buscar\" with the text of the position filled in (not logged in, after confirming that the text goes in the link and logging in). "
			+ "What does it do? Saves the text in vis_resume_filter_description, under the id of the filters and the e-mail of the session (the same filters shared again replace the text). The link copied gets &email=<e-mail>, and GET /resume-filter/{id}?email= gives the text back."
			+ "<br/><br/><b>Request body fields (JSON):</b><ul>"
			+ "<li><b>filterId</b> – Required. The id the search of the filters answered.</li>"
			+ "<li><b>description</b> – Required. The text of the position, from 100 to 5,000 characters.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = { @Content(schema = @Schema(example = "{\"filterId\": \"5f2c9e\"}")) }, responseCode = "200", description = "The text was saved."),
		@ApiResponse(responseCode = "401", description = "No valid session."),
		@ApiResponse(responseCode = "422", description = "Validation error — no id of the filters, or a text shorter than 100 or longer than 5,000 characters."),
	})
	@PostMapping("/recruiter/{email}/resume-filter-description")
	Map<String, Object> saveResumeFilterDescription(@RequestBody Map<String, Object> sessionValues);
}
