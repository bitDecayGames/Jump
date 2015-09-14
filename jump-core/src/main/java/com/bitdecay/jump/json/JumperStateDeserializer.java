package com.bitdecay.jump.json;

import java.io.IOException;

import com.bitdecay.jump.state.JumperState;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class JumperStateDeserializer extends JsonDeserializer {

	@Override
	public JumperState deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		String nodeText = node.get(1).asText();
		return JumperState.valueOf(nodeText);
	}

}
