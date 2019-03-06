/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.tools.rest.builder.internal.freemarker.tool.java.parser;

import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.util.CamelCaseUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.tools.rest.builder.internal.freemarker.tool.java.JavaMethodParameter;
import com.liferay.portal.tools.rest.builder.internal.freemarker.tool.java.JavaMethodSignature;
import com.liferay.portal.tools.rest.builder.internal.freemarker.tool.java.parser.util.OpenAPIParserUtil;
import com.liferay.portal.vulcan.multipart.MultipartBody;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.yaml.config.ConfigYAML;
import com.liferay.portal.vulcan.yaml.openapi.Content;
import com.liferay.portal.vulcan.yaml.openapi.Get;
import com.liferay.portal.vulcan.yaml.openapi.OpenAPIYAML;
import com.liferay.portal.vulcan.yaml.openapi.Operation;
import com.liferay.portal.vulcan.yaml.openapi.Parameter;
import com.liferay.portal.vulcan.yaml.openapi.PathItem;
import com.liferay.portal.vulcan.yaml.openapi.RequestBody;
import com.liferay.portal.vulcan.yaml.openapi.Response;
import com.liferay.portal.vulcan.yaml.openapi.Schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Peter Shin
 */
public class ResourceOpenAPIParser {

	public static List<JavaMethodSignature> getJavaMethodSignatures(
		ConfigYAML configYAML, OpenAPIYAML openAPIYAML, String schemaName) {

		Map<String, PathItem> pathItems = openAPIYAML.getPathItems();

		if (pathItems == null) {
			return Collections.emptyList();
		}

		Map<String, String> javaDataTypeMap =
			OpenAPIParserUtil.getJavaDataTypeMap(configYAML, openAPIYAML);
		List<JavaMethodSignature> javaMethodSignatures = new ArrayList<>();

		for (Map.Entry<String, PathItem> entry : pathItems.entrySet()) {
			String path = entry.getKey();
			PathItem pathItem = entry.getValue();

			_visitOperations(
				pathItem,
				operation -> {
					String returnType = _getReturnType(
						javaDataTypeMap, operation);
					List<String> tags = operation.getTags();

					if (!_isSchemaMethod(
							javaDataTypeMap, returnType, schemaName, tags)) {

						return;
					}

					List<JavaMethodParameter> javaMethodParameters =
						_getJavaMethodParameters(javaDataTypeMap, operation);
					String methodName = _getMethodName(
						operation, path, returnType, schemaName);

					javaMethodSignatures.add(
						new JavaMethodSignature(
							path, pathItem, operation, schemaName,
							javaMethodParameters, methodName, returnType));
				});
		}

		return javaMethodSignatures;
	}

	public static String getMethodAnnotations(
		JavaMethodSignature javaMethodSignature) {

		String path = javaMethodSignature.getPath();
		Operation operation = javaMethodSignature.getOperation();

		Set<String> methodAnnotations = new TreeSet<>();

		methodAnnotations.add("@Path(\"" + path + "\")");

		String httpMethod = OpenAPIParserUtil.getHTTPMethod(operation);

		methodAnnotations.add("@" + StringUtil.toUpperCase(httpMethod));

		String methodAnnotation = _getMethodAnnotationConsumes(operation);

		if (Validator.isNotNull(methodAnnotation)) {
			methodAnnotations.add(methodAnnotation);
		}

		methodAnnotation = _getMethodAnnotationProduces(operation);

		if (Validator.isNotNull(methodAnnotation)) {
			methodAnnotations.add(methodAnnotation);
		}

		return StringUtil.merge(methodAnnotations, "\n");
	}

	public static String getOAuth2ScopesParameters(
		List<JavaMethodParameter> javaMethodParameters) {

		StringBuilder sb = new StringBuilder();

		for (JavaMethodParameter javaMethodParameter : javaMethodParameters) {
			sb.append(javaMethodParameter.getParameterType());

			sb.append(',');
		}

		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}

		return sb.toString();
	}

	public static String getParameters(
		List<JavaMethodParameter> javaMethodParameters, Operation operation,
		boolean annotation) {

		StringBuilder sb = new StringBuilder();

		for (JavaMethodParameter javaMethodParameter : javaMethodParameters) {
			String parameterAnnotation = null;

			if (annotation) {
				parameterAnnotation = _getParameterAnnotation(
					javaMethodParameter, operation);
			}

			String parameter = OpenAPIParserUtil.getParameter(
				javaMethodParameter, parameterAnnotation);

			sb.append(parameter);

			sb.append(',');
		}

		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}

		return sb.toString();
	}

	private static List<JavaMethodParameter> _getJavaMethodParameters(
		Map<String, String> javaDataTypeMap, Operation operation) {

		if ((operation == null) || (operation.getParameters() == null)) {
			return Collections.emptyList();
		}

		List<JavaMethodParameter> javaMethodParameters = new ArrayList<>();

		List<Parameter> parameters = operation.getParameters();

		Set<String> parameterNames = new HashSet<>();

		for (Parameter parameter : parameters) {
			parameterNames.add(parameter.getName());
		}

		for (Parameter parameter : parameters) {
			String parameterName = parameter.getName();

			if (StringUtil.equals(parameterName, "Accept-Language") ||
				StringUtil.equals(parameterName, "filter") ||
				StringUtil.equals(parameterName, "sort")) {

				continue;
			}

			if (StringUtil.equals(parameterName, "page") ||
				StringUtil.equals(parameterName, "pageSize")) {

				if (parameterNames.contains("page") &&
					parameterNames.contains("pageSize")) {

					continue;
				}
			}

			javaMethodParameters.add(
				new JavaMethodParameter(
					CamelCaseUtil.toCamelCase(parameterName),
					OpenAPIParserUtil.getJavaDataType(
						javaDataTypeMap, parameter.getSchema())));
		}

		if (parameterNames.contains("filter")) {
			JavaMethodParameter javaMethodParameter = new JavaMethodParameter(
				"filter", Filter.class.getName());

			javaMethodParameters.add(javaMethodParameter);
		}

		if (parameterNames.contains("page") &&
			parameterNames.contains("pageSize")) {

			JavaMethodParameter javaMethodParameter = new JavaMethodParameter(
				"pagination", Pagination.class.getName());

			javaMethodParameters.add(javaMethodParameter);
		}

		if (parameterNames.contains("sort")) {
			JavaMethodParameter javaMethodParameter = new JavaMethodParameter(
				"sorts", Sort[].class.getName());

			javaMethodParameters.add(javaMethodParameter);
		}

		RequestBody requestBody = operation.getRequestBody();

		if (requestBody != null) {
			JavaMethodParameter multipartBodyJavaMethodParameter = null;

			Map<String, Content> contents = requestBody.getContent();

			for (Map.Entry<String, Content> entry : contents.entrySet()) {
				if (Objects.equals(entry.getKey(), "multipart/form-data")) {
					multipartBodyJavaMethodParameter = new JavaMethodParameter(
						"multipartBody", MultipartBody.class.getName());
				}
			}

			if (multipartBodyJavaMethodParameter == null) {
				for (Content content : contents.values()) {
					String parameterType = OpenAPIParserUtil.getJavaDataType(
						javaDataTypeMap, content.getSchema());

					if (Long.class.isInstance(parameterType)) {
						javaMethodParameters.add(
							new JavaMethodParameter(
								"referenceId", parameterType));
					}
					else {
						String simpleClassName = parameterType.substring(
							parameterType.lastIndexOf(".") + 1);

						String parameterName = StringUtil.lowerCaseFirstLetter(
							simpleClassName);

						javaMethodParameters.add(
							new JavaMethodParameter(
								parameterName,
								javaDataTypeMap.get(simpleClassName)));
					}
				}
			}
			else {
				javaMethodParameters.add(multipartBodyJavaMethodParameter);
			}
		}

		return javaMethodParameters;
	}

	private static String _getMethodAnnotationConsumes(Operation operation) {
		RequestBody requestBody = operation.getRequestBody();

		if (requestBody == null) {
			return null;
		}

		Map<String, Content> contents = requestBody.getContent();

		if ((contents == null) || contents.isEmpty()) {
			return null;
		}

		List<String> mediaTypes = new ArrayList<>(contents.keySet());

		if (mediaTypes.isEmpty()) {
			return null;
		}

		Collections.sort(mediaTypes);

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < mediaTypes.size(); i++) {
			sb.append(StringUtil.quote(mediaTypes.get(i), "\""));

			if (i < (mediaTypes.size() - 1)) {
				sb.append(", ");
			}
		}

		if (mediaTypes.size() > 1) {
			return "@Consumes({" + sb.toString() + "})";
		}

		return "@Consumes(" + sb.toString() + ")";
	}

	private static String _getMethodAnnotationProduces(Operation operation) {
		Map<String, Response> responses = operation.getResponses();

		if (responses.isEmpty()) {
			return null;
		}

		List<String> mediaTypes = new ArrayList<>();

		for (Response response : responses.values()) {
			Map<String, Content> contents = response.getContent();

			if ((contents == null) || contents.isEmpty()) {
				continue;
			}

			mediaTypes.addAll(new ArrayList<>(contents.keySet()));
		}

		if (mediaTypes.isEmpty()) {
			return null;
		}

		Collections.sort(mediaTypes);

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < mediaTypes.size(); i++) {
			sb.append(StringUtil.quote(mediaTypes.get(i), "\""));

			if (i < (mediaTypes.size() - 1)) {
				sb.append(", ");
			}
		}

		if (mediaTypes.size() > 1) {
			return "@Produces({" + sb.toString() + "})";
		}

		return "@Produces(" + sb.toString() + ")";
	}

	private static String _getMethodName(
		Operation operation, String path, String returnType,
		String schemaName) {

		List<String> methodNameSegments = new ArrayList<>();

		String httpMethod = OpenAPIParserUtil.getHTTPMethod(operation);

		methodNameSegments.add(httpMethod);

		List<Parameter> parameters = Collections.emptyList();

		if (operation.getParameters() != null) {
			parameters = operation.getParameters();
		}

		Stream<Parameter> stream = parameters.stream();

		parameters = stream.filter(
			parameter -> {
				if (StringUtil.equals(parameter.getIn(), "path")) {
					return true;
				}

				return false;
			}
		).collect(
			Collectors.toList()
		);

		for (Parameter parameter : parameters) {
			String text = parameter.getName();

			text = CamelCaseUtil.toCamelCase(text.replace("-id", ""));

			methodNameSegments.add(StringUtil.upperCaseFirstLetter(text));
		}

		if (httpMethod.equals("get") &&
			returnType.startsWith(Page.class.getName() + "<")) {

			String className = returnType.substring(5, returnType.length() - 1);

			String simpleClassName = className.substring(
				className.lastIndexOf(".") + 1);

			if (Objects.equals(simpleClassName, schemaName)) {
				methodNameSegments.add(TextFormatter.formatPlural(schemaName));
			}
		}

		String[] pathSegments = path.split("/");

		if (httpMethod.equals("post") &&
			(pathSegments.length >
				(methodNameSegments.size() + parameters.size()))) {

			String pathSegment =
				pathSegments[methodNameSegments.size() + parameters.size()];

			String text = CamelCaseUtil.toCamelCase(pathSegment);

			text = StringUtil.upperCaseFirstLetter(text);

			if (text.equals(TextFormatter.formatPlural(schemaName))) {
				methodNameSegments.add(schemaName);
			}
		}

		if (pathSegments.length >
				(methodNameSegments.size() + parameters.size())) {

			String pathSegment =
				pathSegments[methodNameSegments.size() + parameters.size()];

			String text = CamelCaseUtil.toCamelCase(pathSegment);

			methodNameSegments.add(StringUtil.upperCaseFirstLetter(text));
		}

		if (StringUtil.startsWith(returnType, Page.class.getName() + "<")) {
			methodNameSegments.add("Page");
		}

		return String.join("", methodNameSegments);
	}

	private static String _getParameterAnnotation(
		JavaMethodParameter javaMethodParameter, Operation operation) {

		List<Parameter> parameters = operation.getParameters();

		Set<String> parameterNames = new HashSet<>();

		for (Parameter parameter : parameters) {
			parameterNames.add(parameter.getName());
		}

		String parameterType = javaMethodParameter.getParameterType();

		if (Objects.equals(parameterType, Filter.class.getName()) &&
			parameterNames.contains("filter")) {

			return "@Context";
		}

		if (Objects.equals(parameterType, Pagination.class.getName()) &&
			parameterNames.contains("page") &&
			parameterNames.contains("pageSize")) {

			return "@Context";
		}

		if (Objects.equals(parameterType, Sort[].class.getName()) &&
			parameterNames.contains("sort")) {

			return "@Context";
		}

		for (Parameter parameter : operation.getParameters()) {
			String parameterName = CamelCaseUtil.toCamelCase(
				parameter.getName());

			if (!Objects.equals(
					parameterName, javaMethodParameter.getParameterName())) {

				continue;
			}

			Schema schema = parameter.getSchema();

			if (schema.getType() != null) {
				StringBuilder sb = new StringBuilder();

				sb.append("@");
				sb.append(StringUtil.upperCaseFirstLetter(parameter.getIn()));
				sb.append("Param(\"");
				sb.append(parameter.getName());
				sb.append("\")");

				return sb.toString();
			}
		}

		return "";
	}

	private static String _getReturnType(
		Map<String, String> javaDataTypeMap, Operation operation) {

		Map<String, Response> responses = operation.getResponses();

		if (responses.isEmpty()) {
			return boolean.class.getName();
		}

		for (Response response : responses.values()) {
			Map<String, Content> contents = response.getContent();

			if ((contents == null) || (contents.values() == null)) {
				continue;
			}

			for (Content content : contents.values()) {
				Schema schema = content.getSchema();

				if (schema == null) {
					continue;
				}

				String returnType = OpenAPIParserUtil.getJavaDataType(
					javaDataTypeMap, schema);

				if (returnType.startsWith("[")) {
					StringBuilder sb = new StringBuilder();

					sb.append(Page.class.getName());
					sb.append("<");
					sb.append(
						OpenAPIParserUtil.getElementClassName(returnType));
					sb.append(">");

					return sb.toString();
				}

				String schemaReference = schema.getReference();

				if ((schemaReference == null) ||
					!schemaReference.startsWith("#/components/schemas/")) {

					continue;
				}

				return javaDataTypeMap.get(
					OpenAPIParserUtil.getReferenceName(schemaReference));
			}
		}

		if (Get.class.isInstance(operation)) {
			return String.class.getName();
		}

		return boolean.class.getName();
	}

	private static boolean _isSchemaMethod(
		Map<String, String> javaDataTypeMap, String returnType,
		String schemaName, List<String> tags) {

		if (!tags.isEmpty()) {
			if (tags.contains(schemaName)) {
				return true;
			}

			return false;
		}

		if (returnType.equals(javaDataTypeMap.get(schemaName))) {
			return true;
		}

		if (returnType.startsWith(Page.class.getName() + "<") &&
			returnType.endsWith(">")) {

			String pageClassName = Page.class.getName();

			String className = returnType.substring(
				pageClassName.length() + 1, returnType.length() - 1);

			if (className.equals(javaDataTypeMap.get(schemaName))) {
				return true;
			}
		}

		return false;
	}

	private static void _visitOperations(
		PathItem pathItem, Consumer<Operation> consumer) {

		if (pathItem.getDelete() != null) {
			consumer.accept(pathItem.getDelete());
		}

		if (pathItem.getGet() != null) {
			consumer.accept(pathItem.getGet());
		}

		if (pathItem.getHead() != null) {
			consumer.accept(pathItem.getHead());
		}

		if (pathItem.getOptions() != null) {
			consumer.accept(pathItem.getOptions());
		}

		if (pathItem.getPatch() != null) {
			consumer.accept(pathItem.getPatch());
		}

		if (pathItem.getPost() != null) {
			consumer.accept(pathItem.getPost());
		}

		if (pathItem.getPut() != null) {
			consumer.accept(pathItem.getPut());
		}
	}

}