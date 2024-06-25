package com.mobigen.framework.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Bytes 형식의 데이터를 MegeBytes 형식으로 변경
 * @author rhcpn_000
 *
 */
public class ByteToMegaByteSerializer extends JsonSerializer<String> {
	@Override
	public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		String strNum = "";
		if (value != null && !value.isEmpty()) {
			long lvalue = Math.round(Double.parseDouble(value)/1024/1024);
			strNum = String.format("%,d", lvalue);
		}

		jgen.writeString(strNum);
	}
}
