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

	@Operation(
		summary = "Search companies by name prefix (autocomplete)",
		description = "When does it occur? When the user starts typing a company name and the frontend needs autocomplete suggestions. "
			+ "What does it do? Returns a list of companies whose names start with the given prefix (case-insensitive). "
			+ "If only 3 characters are typed, all companies with that exact prefix are returned without further filtering."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>search</b> – Required. Company name prefix. Min 3, max 20 characters. "
			+ "The value is automatically converted to uppercase before the search.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{\"companies\": [\"GOOGLE\", \"GLOBO\"]}")) },
			responseCode = "200",
			description = "List of companies matching the given prefix. "
				+ "If no matches are found, the list contains the search term itself."),
	})
	@GetMapping("/search/autocomplete/{search}")
	Map<String, Object> searchCompaniesByTheirFirstThreeInitials(@PathVariable("search") String search);
}
