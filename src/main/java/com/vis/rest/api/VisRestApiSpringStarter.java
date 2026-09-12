package com.vis.rest.api;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.implementations.cache.gcp.memcache.CcpGcpMemCache;
import com.ccp.implementations.db.bulk.elasticsearch.CcpElasticSerchDbBulk;
import com.ccp.implementations.db.crud.elasticsearch.CcpElasticSearchCrud;
import com.ccp.implementations.db.utils.elasticsearch.CcpElasticSearchDbRequest;
import com.ccp.implementations.file.bucket.gcp.CcpGcpFileBucket;
import com.ccp.implementations.http.apache.mime.CcpApacheMimeHttp;
import com.ccp.implementations.json.gson.CcpGsonJsonHandler;
import com.ccp.implementations.main.authentication.gcp.oauth.CcpGcpMainAuthentication;
import com.ccp.implementations.mensageria.sender.gcp.pubsub.CcpGcpPubSubMensageriaSender;
import com.ccp.implementations.password.mindrot.CcpMindrotPasswordHandler;
import com.ccp.implementations.text.extractor.apache.tika.CcpApacheTikaTextExtractor;
import com.ccp.local.testings.implementations.CcpLocalInstances;
import com.ccp.local.testings.implementations.cache.CcpLocalCacheInstances;
import com.ccp.rest.api.spring.exceptions.handler.CcpRestApiExceptionHandlerSpring;
import com.ccp.rest.api.spring.servlet.filters.CcpPutSessionValuesAndExecuteTaskFilter;
import com.ccp.rest.api.spring.servlet.filters.CcpValidEmailFilter;
import com.ccp.rest.api.utils.CcpRestApiUtils;
import com.jn.business.messages.JnBusinessNotifyError;
import com.jn.mensageria.JnFunctionMensageriaSender;
import com.jn.services.JnServiceLogin;
import com.vis.rest.api.endpoints.VisRestApiResume;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.PathParameter;
import io.swagger.v3.oas.models.Paths;
import java.util.stream.Stream;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;

/**
 * Ponto de entrada da API REST do módulo VIS (Visualização). Inicializa as dependências do framework
 * (incluindo {@code CcpApacheTikaTextExtractor}, exclusivo deste módulo), configura filtros de
 * validação de e-mail e sessão para os paths {@code /resume/*} e {@code /position/*}.
 */

@EnableWebMvc
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@ComponentScan(basePackageClasses = {
		VisRestApiResume.class,
		CcpRestApiExceptionHandlerSpring.class,
})
@SpringBootApplication
public class VisRestApiSpringStarter {
	
	public static void main(String[] args) {
		CcpGsonJsonHandler ccpGsonJsonHandler = new CcpGsonJsonHandler();
		CcpDependencyInjection.loadAllDependencies(ccpGsonJsonHandler);
		
		boolean localEnvironment = CcpRestApiUtils.isLocalEnvironment();	
		CcpApacheTikaTextExtractor ccpApacheTikaTextExtractor = new CcpApacheTikaTextExtractor();
		CcpElasticSearchDbRequest ccpElasticSearchDbRequest = new CcpElasticSearchDbRequest();
		CcpMindrotPasswordHandler ccpMindrotPasswordHandler = new CcpMindrotPasswordHandler();
		CcpGcpMainAuthentication ccpGcpMainAuthentication = new CcpGcpMainAuthentication();
		CcpElasticSerchDbBulk ccpElasticSerchDbBulk = new CcpElasticSerchDbBulk();
		CcpElasticSearchCrud ccpElasticSearchCrud = new CcpElasticSearchCrud();
		CcpApacheMimeHttp ccpApacheMimeHttp = new CcpApacheMimeHttp();
		CcpDependencyInjection.loadAllDependencies
		(
				localEnvironment ? CcpLocalInstances.syncMensageriaListener : new CcpGcpPubSubMensageriaSender(),
				localEnvironment ? CcpLocalCacheInstances.map : new CcpGcpMemCache(),
				localEnvironment ? CcpLocalInstances.bucket : new CcpGcpFileBucket(),
				ccpApacheTikaTextExtractor,
				ccpElasticSearchDbRequest,
				ccpMindrotPasswordHandler
				,
				ccpGcpMainAuthentication,
				ccpElasticSerchDbBulk,
				ccpElasticSearchCrud,ccpApacheMimeHttp 
		);

		CcpRestApiExceptionHandlerSpring.genericExceptionHandler = new JnFunctionMensageriaSender(JnBusinessNotifyError.instance);
		SpringApplication.run(VisRestApiSpringStarter.class, args);
	}
	
	@Bean
	public GlobalOpenApiCustomizer missingPathParamsCustomizer() {
		return openApi -> {
			Paths paths = openApi.getPaths();
			boolean pathsIgual = paths == null;
			if (pathsIgual) return;
			Pattern p = Pattern.compile("\\{(\\w+)\\}");
			Paths paths2 = openApi.getPaths();
			paths2.forEach((pathTemplate, pathItem) -> {
				Set<String> templateVars = new HashSet<>();
				Matcher m = p.matcher(pathTemplate);
				while (m.find()) {
					String group = m.group(1);
					templateVars.add(group);
					}
					boolean templateVarsEmpty = templateVars.isEmpty();
					if (templateVarsEmpty) return;
					var readOperations = pathItem.readOperations();
					readOperations.forEach(op -> {
					Set<String> declared = new HashSet<>();
					var parameters = op.getParameters();
					boolean parametersDiferente = parameters != null;
					if (parametersDiferente) {
						var parameters2 = op.getParameters();
						var stream = parameters2.stream();
						var filter2 = stream
							.filter(param -> "path".equals(param.getIn()));
							filter2
							.forEach(param -> declared.add(param.getName()));
					}
					Stream<String> stream2 = templateVars.stream();
					var filter3 = stream2
						.filter(v -> !declared.contains(v));
						filter3
						.forEach(v -> op.addParametersItem(
							new PathParameter().name(v).required(true).schema(new StringSchema())
						));
				});
			});
		};
	}

	@Bean
	public OpenAPI visOpenAPI() {
		OpenAPI openAPI = new OpenAPI();
		Info info2 = new Info();
		Info title = info2
						.title("JobsNow VIS API");
						Info description = title
						.description("REST API for the VIS module: resume management, positions, recruiters, companies and skills.");
						Info version = description
						.version("1.0");
						OpenAPI info = openAPI
						.info(version);
						return info;
	}

	@Bean
	public WebMvcConfigurer swaggerResourceHandler() {
		var webMvcConfigurer = new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				ResourceHandlerRegistration addResourceHandler = registry.addResourceHandler("/webjars/**");
				addResourceHandler
						.addResourceLocations("classpath:/META-INF/resources/webjars/");
						ResourceHandlerRegistration addResourceHandler2 = registry.addResourceHandler("/swagger-ui/**");
						addResourceHandler2
						.addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");
			}
		};
		return webMvcConfigurer;
	}

	@Bean
	public FilterRegistrationBean<CcpValidEmailFilter> emailFilter() {
		FilterRegistrationBean<CcpValidEmailFilter> filtro = new FilterRegistrationBean<>();
		CcpValidEmailFilter emailSyntaxFilter = CcpValidEmailFilter.getEmailSyntaxFilter("resume/");
		filtro.setFilter(emailSyntaxFilter);
		filtro.addUrlPatterns("/resume/*", "/position/*");
		
		return filtro;
	}
	
	@Bean
	public FilterRegistrationBean<CcpPutSessionValuesAndExecuteTaskFilter> validateSessionFilter() {
		FilterRegistrationBean<CcpPutSessionValuesAndExecuteTaskFilter> filtro = new FilterRegistrationBean<>();
		CcpPutSessionValuesAndExecuteTaskFilter filter = new CcpPutSessionValuesAndExecuteTaskFilter(JnServiceLogin.ValidateLogin);
		filtro.setFilter(filter);
		filtro.addUrlPatterns("/resume/*", "/position/*");
		return filtro;
	}
}
