package com.toptal.expenses.serialization;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Factory and configurator of {@link ObjectMapper}
 * 
 * @author jorge.santoro
 */
public class ObjectMapperFactory {

	private static SimpleModule getDefaultModule() {
		SimpleModule module = new SimpleModule("DefaultModule", new Version(0, 0, 1, null, null, null));
		module.addDeserializer(DateTime.class, new JodaDateTimeJsonDeserializer());
		module.addSerializer(DateTime.class, new JodaDateTimeJsonSerializer());
		module.addDeserializer(LocalDate.class, new JodaLocalDateJsonDeserializer());
		module.addSerializer(LocalDate.class, new JodaLocalDateJsonSerializer());
		return module;
	}

	public static ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.registerModule(getDefaultModule());

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

		return objectMapper;
	}

}
