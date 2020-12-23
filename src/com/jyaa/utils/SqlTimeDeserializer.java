package com.jyaa.utils;

import java.io.IOException;
import java.sql.Time;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class SqlTimeDeserializer extends JsonDeserializer<Time> {

	@Override
    public Time deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if(jp.getValueAsString().length() == 8)
        	return Time.valueOf(jp.getValueAsString());
        return Time.valueOf(jp.getValueAsString() + ":00");
    }
}
