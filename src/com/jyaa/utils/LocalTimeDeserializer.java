package com.jyaa.utils;

import java.io.IOException;
import java.time.LocalTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalTimeDeserializer extends StdDeserializer<LocalTime>{

	public LocalTimeDeserializer(){
		// TODO Auto-generated constructor stub
		super(LocalTime.class);
	}

	@Override
	public LocalTime deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return LocalTime.parse(parser.readValueAs(String.class));
	}

}
