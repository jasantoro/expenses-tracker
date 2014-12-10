package com.toptal.expenses.serialization;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.SimpleType;

/**
 * Manages all the json representation in the application
 * 
 * @author jorge.santoro
 */
public class JsonFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonFactory.class);

	public static final JavaType MAP_TYPE = MapType.construct(HashMap.class, SimpleType.construct(String.class),
			SimpleType.construct(Object.class));
	public static JsonFactory instance = new JsonFactory();

	private ObjectMapper objectMapper;

	private JsonFactory() {
		this.objectMapper = ObjectMapperFactory.createObjectMapper();
	}

	public String toJson(ObjectMapper objectMapper, Object model) {
		try {
			return objectMapper.writeValueAsString(model);
		} catch (JsonGenerationException e) {
			LOGGER.warn("Could not generate JSON string from object: {}", toString(model), e);
		} catch (JsonMappingException e) {
			LOGGER.warn("Could not map to JSON string the object: {}", toString(model), e);
		} catch (IOException e) {
			LOGGER.warn("I/O Exception converting to JSON string from object: {}", toString(model), e);
		}
		return null;
	}

	public String toJson(Object model) {
		return this.toJson(this.objectMapper, model);
	}

	public void toJson(Writer writer, Object object) {
		try {
			this.objectMapper.writeValue(writer, object);
		} catch (JsonGenerationException e) {
			LOGGER.error("Could not generate JSON string from object: {}", toString(object), e);
		} catch (JsonMappingException e) {
			LOGGER.error("Could not map to JSON string the object: {}", toString(object), e);
		} catch (IOException e) {
			LOGGER.error("I/O Exception converting to JSON string from object: {}", toString(object), e);
		}
	}

	public <T> T fromJson(String content, JavaType type, boolean nullOnError) {
		return this.fromJson(new StringReader(content), type, nullOnError);
	}

	public <T> T fromJson(Reader reader, JavaType type, boolean nullOnError) {
		try {
			return this.objectMapper.readValue(reader, type);
		} catch (JsonParseException e) {
			if (!nullOnError) {
				throw new RuntimeException("Could not parse JSON from reader", e);
			}
		} catch (InvalidFormatException e) {
			if (!nullOnError) {
				throw new IllegalArgumentException(e);
			}
		} catch (IOException e) {
			if (!nullOnError) {
				throw new RuntimeException("Could not map the JSON to the type", e);
			}
		}
		return null;
	}

	public JsonNode readTree(String content) {
		try {
			return this.objectMapper.readTree(content);
		} catch (Exception e) {
			throw new RuntimeException("Error reading the content", e);
		}
	}

	public <T> T fromJson(String content, Class<T> clazz, boolean nullOnError) {
		return this.fromJson(new StringReader(content), clazz, nullOnError);
	}

	public <T> T fromJson(Reader reader, Class<T> clazz, boolean nullOnError) {
		return this.fromJson(reader, SimpleType.construct(clazz), nullOnError);
	}

	public <T> T fromJson(String content, JavaType type) {
		return this.fromJson(new StringReader(content), type);
	}

	public <T> T fromJson(Reader reader, JavaType type) {
		return this.fromJson(reader, type, true);
	}

	public <T> T fromJson(String content, Class<T> clazz) {
		return this.fromJson(new StringReader(content), clazz);
	}

	public <T> T fromJson(Reader reader, Class<T> clazz) {
		return this.fromJson(reader, SimpleType.construct(clazz), true);
	}

	private static String toString(Object object) {
		return ToStringBuilder.reflectionToString(object, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
