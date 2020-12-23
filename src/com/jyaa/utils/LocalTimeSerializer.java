package com.jyaa.utils;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LocalTimeSerializer extends StdSerializer<LocalTime>{

	public LocalTimeSerializer() {
		// TODO Auto-generated constructor stub
		super(LocalTime.class);
	}

	@Override
	public void serialize(LocalTime value, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonGenerationException {
		generator.writeString(value.format(DateTimeFormatter.ISO_LOCAL_TIME));
		// TODO Auto-generated method stub
		
	}

}
