package com.vis.rest.open.api;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OpenAPI contract for the positions of the logged-in user (screen "Meu Recrutamento - Vagas") at
 * {@code /recruiter/{email}/positions}. The list of the screen is read by id through the endpoint of the groups of js.
 */
@Tag(name = "Positions", description = "The positions the logged-in user registers: create, edit, delete, inactivate and reactivate.")
public interface VisOpenApiPosition {

	/**
	 * Create a position.
	 * @param sessionValues the request body with the values of the session
	 * @return the response body
	 */
	@Operation(
		summary = "Create a position",
		description = "When does it occur? When the logged-in user saves the modal of \"Criar vaga\". "
			+ "What does it do? Saves a new position whose recruiter is the e-mail of the user. The title is part of the key: a title the user already has answers 409. "
			+ "The expiration of a new position starts and ends from the current date on. The writing is asynchronous: the answer comes before the list shows the position."
			+ "<br/><br/><b>Path variables:</b><ul><li><b>email</b> – Required. The logged-in user's e-mail.</li></ul>"
			+ "<b>Request body fields (JSON):</b><ul>"
			+ "<li><b>title</b> – Required. 10 to 100 characters.</li>"
			+ "<li><b>description</b> – Required. 100 to 5,000 characters.</li>"
			+ "<li><b>resumeType</b>, <b>seniority</b>, <b>ddd</b>, <b>disponibility</b>, <b>temporallyJobTime</b> – Required.</li>"
			+ "<li><b>clt</b>, <b>pj</b> – Salary ranges {min, max} from 1,000 to 100,000; at least one of them.</li>"
			+ "<li><b>expiration</b> – Required. {min, max}, timestamps in milliseconds (as text), from the current date on, max not before min.</li>"
			+ "<li><b>sort</b> – Required. List of {type: asc|desc, field: [pj|clt|experience|shouldSkillsTotal]}.</li>"
			+ "<li><b>contact</b> – Required. List of {type: whatsapp|email|link, value}.</li>"
			+ "<li><b>spokenLanguage</b>, <b>skill</b>, <b>negotiableClaim</b>, <b>pcd</b>, <b>travel</b> – Optional.</li>"
			+ "<li><b>mustSendInGroups</b>, <b>mustSendToResumes</b> (default true), <b>frequencyType</b> (none|instantly|hourly|daily|weekly, default daily) – Optional.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = { @Content(schema = @Schema(example = "{\"title\": \"Desenvolvedor Java pleno\", \"seniority\": \"pleno\"}")) }, responseCode = "200", description = "The position, as it will be saved."),
		@ApiResponse(responseCode = "401", description = "No valid login."),
		@ApiResponse(responseCode = "409", description = "The user already has a position with this title."),
		@ApiResponse(responseCode = "422", description = "Validation error — one or more required fields are missing or contain invalid values."),
	})
	@PostMapping("/recruiter/{email}/positions")
	Map<String, Object> createPosition(@RequestBody Map<String, Object> sessionValues);

	/**
	 * Edit a position.
	 * @param sessionValues the request body with the values of the session
	 * @return the response body
	 */
	@Operation(
		summary = "Edit a position",
		description = "When does it occur? When the logged-in user saves the modal of \"Editar\". "
			+ "What does it do? Saves again the position of the user with the title informed (the title does not change), with the same fields of the creation; "
			+ "the expiration only needs the max not before the min. The writing is asynchronous."
	)
	@ApiResponses({
		@ApiResponse(content = { @Content(schema = @Schema(example = "{\"title\": \"Desenvolvedor Java pleno\", \"seniority\": \"pleno\"}")) }, responseCode = "200", description = "The position, as it will be saved."),
		@ApiResponse(responseCode = "401", description = "No valid login."),
		@ApiResponse(responseCode = "404", description = "The user has no position with this title."),
		@ApiResponse(responseCode = "422", description = "Validation error — one or more required fields are missing or contain invalid values."),
	})
	@PostMapping("/recruiter/{email}/positions/edit")
	Map<String, Object> editPosition(@RequestBody Map<String, Object> sessionValues);

	/**
	 * Delete a position.
	 * @param sessionValues the request body with the values of the session
	 * @return the response body
	 */
	@Operation(
		summary = "Delete a position",
		description = "When does it occur? When the logged-in user confirms \"Excluir\" in a line of the tab of the positions not expired. "
			+ "What does it do? Moves the position to the deleted positions (vis_inactive_position): it leaves both tabs. The writing is asynchronous."
			+ "<br/><br/><b>Request body fields (JSON):</b><ul><li><b>title</b> – Required. The title of the position.</li></ul>"
	)
	@ApiResponses({
		@ApiResponse(content = { @Content(schema = @Schema(example = "{\"title\": \"Desenvolvedor Java pleno\"}")) }, responseCode = "200", description = "The position deleted."),
		@ApiResponse(responseCode = "401", description = "No valid login."),
		@ApiResponse(responseCode = "404", description = "The user has no position with this title."),
		@ApiResponse(responseCode = "422", description = "Validation error — the title is missing."),
	})
	@PostMapping("/recruiter/{email}/positions/delete")
	Map<String, Object> deletePosition(@RequestBody Map<String, Object> sessionValues);

	/**
	 * Inactivate a position.
	 * @param sessionValues the request body with the values of the session
	 * @return the response body
	 */
	@Operation(
		summary = "Inactivate a position",
		description = "When does it occur? When the logged-in user confirms \"Inativar\" in a line of the tab of the positions not expired. "
			+ "What does it do? The position expires one minute before now (the min of the expiration goes back to it when it was later) and goes to the tab of the expired positions. The writing is asynchronous."
			+ "<br/><br/><b>Request body fields (JSON):</b><ul><li><b>title</b> – Required. The title of the position.</li></ul>"
	)
	@ApiResponses({
		@ApiResponse(content = { @Content(schema = @Schema(example = "{\"title\": \"Desenvolvedor Java pleno\", \"expiration\": {\"min\": 1763000000000, \"max\": 1763700000000}}")) }, responseCode = "200", description = "The new expiration of the position."),
		@ApiResponse(responseCode = "401", description = "No valid login."),
		@ApiResponse(responseCode = "404", description = "The user has no position with this title."),
		@ApiResponse(responseCode = "422", description = "Validation error — the title is missing."),
	})
	@PostMapping("/recruiter/{email}/positions/inactivate")
	Map<String, Object> inactivatePosition(@RequestBody Map<String, Object> sessionValues);

	/**
	 * Reactivate a position.
	 * @param sessionValues the request body with the values of the session
	 * @return the response body
	 */
	@Operation(
		summary = "Reactivate a position",
		description = "When does it occur? When the logged-in user saves the modal of \"Reativar\" in a line of the tab of the expired positions. "
			+ "What does it do? The position gets the new end of the expiration and goes back to the tab of the positions not expired. The writing is asynchronous."
			+ "<br/><br/><b>Request body fields (JSON):</b><ul>"
			+ "<li><b>title</b> – Required. The title of the position.</li>"
			+ "<li><b>max</b> – Required. The new end of the expiration, timestamp in milliseconds (as text), from the current date on.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = { @Content(schema = @Schema(example = "{\"title\": \"Desenvolvedor Java pleno\", \"expiration\": {\"min\": 1763000000000, \"max\": 1765000000000}}")) }, responseCode = "200", description = "The new expiration of the position."),
		@ApiResponse(responseCode = "401", description = "No valid login."),
		@ApiResponse(responseCode = "404", description = "The user has no position with this title."),
		@ApiResponse(responseCode = "422", description = "Validation error — the title or the max is missing, or the max is before the current date."),
	})
	@PostMapping("/recruiter/{email}/positions/reactivate")
	Map<String, Object> reactivatePosition(@RequestBody Map<String, Object> sessionValues);
}
