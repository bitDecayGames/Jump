package com.bitdecay.jump.json;

import java.io.IOException;

import com.bitdecay.jump.render.JumperRenderState;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class JumperStateDeserializer extends JsonDeserializer {

	@Override
	public JumperRenderState deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		String nodeText = node.get(1).asText();
		return JumperRenderState.valueOf(nodeText);
	}

}
