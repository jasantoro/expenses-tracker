package com.toptal.expenses.serialization;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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
public class JodaLocalDateJsonSerializer extends JsonSerializer<LocalDate> {

	private DateTimeFormatter formatter;

	public JodaLocalDateJsonSerializer() {
		this.formatter = ISODateTimeFormat.date();
	}

	@Override
	public void serialize(LocalDate value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeString(this.formatter.print(value));
	}

}
