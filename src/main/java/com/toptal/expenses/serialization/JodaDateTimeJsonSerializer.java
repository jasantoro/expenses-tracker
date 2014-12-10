package com.toptal.expenses.serialization;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Json serializer of a {@link DateTime}
 * 
 * @author jorge.santoro
 */
public class JodaDateTimeJsonSerializer extends JsonSerializer<DateTime> {

	private DateTimeFormatter formatter;

	public JodaDateTimeJsonSerializer() {
		this.formatter = ISODateTimeFormat.dateHourMinuteSecond();
	}

	@Override
	public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeString(this.formatter.print(value));
	}

}
