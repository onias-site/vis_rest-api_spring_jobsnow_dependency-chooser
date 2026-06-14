package com.vis.rest.api;

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

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.PathParameter;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springdoc.core.customizers.GlobalOpenApiCustomizer;

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
import com.jn.business.login.JnBusinessSessionValidate;
import com.jn.business.messages.JnBusinessNotifyError;
import com.jn.mensageria.JnFunctionMensageriaSender;
import com.vis.rest.api.endpoints.VisRestApiResume;


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
		CcpDependencyInjection.loadAllDependencies(new CcpGsonJsonHandler());
		
		boolean localEnvironment = CcpRestApiUtils.isLocalEnvironment();	
		CcpDependencyInjection.loadAllDependencies
		(
				localEnvironment ? CcpLocalInstances.mensageriaSender : new CcpGcpPubSubMensageriaSender(),
				localEnvironment ? CcpLocalCacheInstances.map : new CcpGcpMemCache(),
				localEnvironment ? CcpLocalInstances.bucket : new CcpGcpFileBucket(),
				new CcpApacheTikaTextExtractor(),
				new CcpElasticSearchDbRequest(),
				new CcpMindrotPasswordHandler()
				,new CcpGcpMainAuthentication()
				,new CcpElasticSerchDbBulk()
				,new CcpElasticSearchCrud()
				,new CcpApacheMimeHttp() 
		);

		CcpRestApiExceptionHandlerSpring.genericExceptionHandler = new JnFunctionMensageriaSender(JnBusinessNotifyError.INSTANCE);
		SpringApplication.run(VisRestApiSpringStarter.class, args);
	}
	
	@Bean
	public GlobalOpenApiCustomizer missingPathParamsCustomizer() {
		return openApi -> {
			if (openApi.getPaths() == null) return;
			Pattern p = Pattern.compile("\\{(\\w+)\\}");
			openApi.getPaths().forEach((pathTemplate, pathItem) -> {
				Set<String> templateVars = new HashSet<>();
				Matcher m = p.matcher(pathTemplate);
				while (m.find()) templateVars.add(m.group(1));
				if (templateVars.isEmpty()) return;
				pathItem.readOperations().forEach(op -> {
					Set<String> declared = new HashSet<>();
					if (op.getParameters() != null) {
						op.getParameters().stream()
							.filter(param -> "path".equals(param.getIn()))
							.forEach(param -> declared.add(param.getName()));
					}
					templateVars.stream()
						.filter(v -> !declared.contains(v))
						.forEach(v -> op.addParametersItem(
							new PathParameter().name(v).required(true).schema(new StringSchema())
						));
				});
			});
		};
	}

	@Bean
	public OpenAPI visOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("JobsNow VIS API")
						.description("REST API for the VIS module: resume management, positions, recruiters, companies and skills.")
						.version("1.0"));
	}

	@Bean
	public WebMvcConfigurer swaggerResourceHandler() {
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/webjars/**")
						.addResourceLocations("classpath:/META-INF/resources/webjars/");
				registry.addResourceHandler("/swagger-ui/**")
						.addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");
			}
		};
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
		CcpPutSessionValuesAndExecuteTaskFilter filter = new CcpPutSessionValuesAndExecuteTaskFilter(JnBusinessSessionValidate.INSTANCE);
		filtro.setFilter(filter);
		filtro.addUrlPatterns("/resume/*", "/position/*");
		return filtro;
	}
}
