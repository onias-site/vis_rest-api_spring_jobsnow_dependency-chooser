package com.vis.rest.api.swagger;

import java.util.Map;

public interface VisOpenApiCompany {

	Map<String, Object> searchCompaniesByTheirFirstThreeInitials(String search);

}
