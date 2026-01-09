package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.constantes.CcpOtherConstants;
import com.vis.rest.api.swagger.VisOpenApiCompany;
import com.vis.services.VisServiceCompany;

//FIXME SCHEDULE PARA SALVAR EMPRESAS INDESEJADAS
//
@CrossOrigin
@RestController
@RequestMapping("companies")
public class VisRestApiCompany implements VisOpenApiCompany {

	
	@GetMapping("/search/autocomplete/{search}")
	public Map<String, Object> searchCompaniesByTheirFirstThreeInitials(@PathVariable("search") String search){
		
		var json = CcpOtherConstants.EMPTY_JSON
					.put(VisServiceCompany.FieldsToSearchCompaniesByTheirFirstThreeInitials.search, search.toUpperCase())
				;
		
		var execute = VisServiceCompany.SearchCompaniesByTheirFirstThreeInitials.execute(json.content);
		
		return execute;
	}
}
