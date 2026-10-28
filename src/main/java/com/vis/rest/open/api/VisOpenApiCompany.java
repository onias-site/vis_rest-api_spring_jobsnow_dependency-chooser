package com.vis.rest.open.api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OpenAPI contract for company operations at path {@code /companies}.
 * Provides autocomplete search for companies by name prefix.
 */
@RequestMapping("companies")
@Tag(name = "Companies", description = "Operations for searching and managing companies in the VIS module.")
public interface VisOpenApiCompany {

	/**
	 * Search companies by name prefix (autocomplete).
	 * @param search the {@code search} path variable
	 * @return the response body
	 */
	@Operation(
		summary = "Search companies by name prefix (autocomplete)",
		description = "When does it occur? When the user starts typing a company name and the frontend needs autocomplete suggestions. "
			+ "What does it do? Returns, in alphabetical order, the companies whose names start with the given prefix (case-insensitive). "
			+ "A company is the first part of the e-mail domain of a recruiter with a professional domain (a public webmail provider is no company), capitalized."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>search</b> – Required. Company name prefix. Min 3, max 50 characters. "
			+ "The comparison ignores case; a search with no match comes back as typed.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{\"companies\": [\"Globo\", \"Google\"]}")) },
			responseCode = "200",
			description = "List of companies matching the given prefix. "
				+ "If no matches are found, the list contains the search term itself."),
	})
	@GetMapping("/search/autocomplete/{search}")
	Map<String, Object> searchCompaniesByTheirFirstThreeInitials(@PathVariable("search") String search);
}
