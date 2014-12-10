package com.toptal.expenses.serialization;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Json deserializer of a {@link DateTime}
 * 
 * @author jorge.santoro
 */
public class JodaDateTimeJsonDeserializer extends JsonDeserializer<DateTime> {

	private DateTimeFormatter formatter;

	public JodaDateTimeJsonDeserializer() {
		this.formatter = ISODateTimeFormat.dateHourMinuteSecond();
	}

	@Override
	public DateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonToken t = jp.getCurrentToken();
		if (t == JsonToken.VALUE_STRING) {
			String str = jp.getText().trim();
			if (str.length() == 0) {
				return null;
			}
			try {
				return this.formatter.parseDateTime(str);
			} catch (IllegalArgumentException e) {
				throw new JsonParseException("Invalid date format \'" + str
						+ "\'. A string \'yyyy-MM-ddThh:mm:ssZ\' was expected.", jp.getCurrentLocation(), e);
			}
		}
		throw new JsonParseException("Invalid date format. A string \'yyyy-MM-ddThh:mm:ssZ\' was expected.",
				jp.getCurrentLocation());
	}
}
