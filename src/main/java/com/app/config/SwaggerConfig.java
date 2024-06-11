package com.app.config;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import com.app.common.dto.ApiDocumentResponseDTO.Success.HeaderSuccess;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@SuppressWarnings({ "rawtypes", "deprecation" })
public class SwaggerConfig {
	
	/**
	 * inner class 형식 DTO(1개의 모델 안에 N개 이상의 클래스를 지정한 경우) 사용시 스웨거3 문서의 리턴 타입 중복현상 때문에 리턴타입 다시 커스텀 
	 * 
	 * @return : OperationCustomizer
	 * @user   : guney
	 * @date   : 2024. 6. 6.
	 * @since  : 1.0
	 */
	@Bean
    public OperationCustomizer customResponseSchemaCustomizer(OpenAPI openAPI) {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            
        	if (operation.getResponses() != null && operation.getResponses().containsKey("200")) {
                ApiResponse apiResponse = operation.getResponses().get("200");
                
                if (ObjectUtils.isNotEmpty(apiResponse.getContent())) {
                	MediaType content = apiResponse.getContent().get("application/json");
                	
                	Schema<?> originalSchema = content.getSchema();
                	
                	//Success 타입인지 확인
                	if (originalSchema != null && originalSchema.get$ref() != null && originalSchema.get$ref().equals("#/components/schemas/ApiBodyDocumentDTO.Success")) {
                		//Success<?> Success 안의 ? 클래스 동적 추출 
                		Class<?> genericType = getGenericType(handlerMethod);
                		
                		//리턴 타입 다시 셋팅 
                		if (genericType != null) {
                			// 제네릭 타입의 스키마를 동적으로 생성 및 추가합니다.
                            String genericTypeName = genericType.getSimpleName();

                            // Success 스키마를 생성하고 설정합니다.
                            Schema<?> successSchema = createSuccessSchema(genericTypeName);
                            content.setSchema(successSchema);
                		}
                	}
                }
            }
            
            return operation;
        };
    }
	
	private Schema<?> createSuccessSchema(String genericTypeName) {
		// Success 스키마를 생성합니다.
        Schema<?> successSchema = new Schema<>();
        successSchema.setType("object");
        
        Schema<HeaderSuccess> headerSchema = new Schema<>();
        headerSchema.set$ref("#/components/schemas/ApiBodyDocumentDTO.HeaderSuccess");

        
        Schema<?> dataSchema = new Schema<>();
        dataSchema.set$ref("#/components/schemas/" + genericTypeName);

        Schema<Object> pageSchema = new Schema<>();
        pageSchema.set$ref("#/components/schemas/PageDTO");
        
        successSchema.addProperties("header", headerSchema);
        successSchema.addProperties("data", dataSchema);
        successSchema.addProperties("page", pageSchema);

        return successSchema;
    }

    /**
     * 제네릭 타입, 클래스인지 검증 후 true면 class 리턴
     * 
     * @parma  : HandlerMethod handlerMethod
     * @return : Class<?>
     * @user   : guney
     * @date   : 2024. 6. 6.
     * @since  : 1.0
     */
    private Class<?> getGenericType(HandlerMethod handlerMethod) {
        try {
            Type returnType = handlerMethod.getMethod().getGenericReturnType();
            
            if (returnType instanceof ParameterizedType parameterizedType) {
                parameterizedType = (ParameterizedType) returnType;
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                
                if (typeArguments.length > 0 && typeArguments[0] instanceof Class<?>) {
                    return (Class<?>) typeArguments[0];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private Map<String, Schema<?>> getSchemasFromProject() {
        Map<String, Schema<?>> schemaMap = new HashMap<>();
        try (ScanResult scanResult = new ClassGraph().acceptPackages("com.app.*").scan()) {
            for (ClassInfo classInfo : scanResult.getAllClasses()) {
                Class<?> clazz = classInfo.loadClass();
                // 클래스 이름에 "DTO"가 포함된 경우에만 추가
                if (clazz.getSimpleName().toUpperCase().contains("DTO")) {
                    String schemaName = clazz.getSimpleName();
                    Schema<?> schema = convertClassToSchema(clazz);
                    schemaMap.put(schemaName, schema);
                    
                }
            }
        }
        
        return schemaMap;
    }
    
    private Schema<?> convertClassToSchema(Class<?> clazz) {
        Schema<Object> schema = new Schema<>();
        Map<String, Schema> properties = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            Schema<?> fieldSchema = new Schema<>();
            fieldSchema.setType(getSchemaType(field.getType()));
            properties.put(field.getName(), fieldSchema);
        }
        schema.setProperties(properties);
        return schema;
    }

    private String getSchemaType(Class<?> type) {
        if (type == String.class) {
            return "string";
        } else if ((type == Integer.class || type == int.class) || (type == Long.class || type == long.class)) {
            return "integer";
        } else if ((type == Float.class || type == float.class) || (type == Double.class || type == double.class)) {
            return "number";
        } else if (type == Boolean.class || type == boolean.class) {
            return "boolean";
        } else {
            return "object";
        }
    }

    @Bean
    public OpenAPI openAPI(){
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");
        
        Components components = new Components().addSecuritySchemes("bearerAuth", securityScheme);

        // DTO 클래스들을 스캔하여 스키마로 추가
        Map<String, Schema<?>> schemas = getSchemasFromProject();
        schemas.forEach(components::addSchemas);
        
        return new OpenAPI()
        		.info(new Info().title("RestApi").description("Swagger redocly 프로젝트").version("v1"))
                .components(components)
                .security(Arrays.asList(securityRequirement));
    }
    
    @Bean
    public GroupedOpenApi apiDocCommon() {
        return GroupedOpenApi.builder()
            .group("공통 API")
            .displayName("공통")
            .pathsToMatch("/com/**")
            .build();
    }
    
    @Bean
    public GroupedOpenApi apiDocSample() {
        return GroupedOpenApi.builder()
            .group("B_샘플 API")
            .displayName("샘플 (sample)")
            .pathsToMatch("/api/**")
            .build();
    }
    
}