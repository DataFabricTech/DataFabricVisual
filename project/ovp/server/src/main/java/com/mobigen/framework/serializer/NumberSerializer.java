package com.mobigen.framework.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * String 형식의 숫자를 세자리 단위로 구분 한다.
 * example)
 *   @JsonSerialize(using=MyDateSerializer.class)
     public Date getBirthDay() {
         return birthDay;
     }
 * @author rhcpn_000
 */
public class NumberSerializer extends com.fasterxml.jackson.databind.JsonSerializer<String> {
	private static final String NEGATIVE_SIGN = "-";
	private static final String DECIMAL_POINT = ".";
	private static final String NUMBER_FORMAT_DECIMAL = "%,.2f";
	private static final String NUMBER_FORMAT_INTEGER = "%,d";

	@Override
	public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		String strNum;
		if (value == null || value.isEmpty()) {
			strNum = "";
		} else if (NEGATIVE_SIGN.equals(value)) {
			strNum = value;
		} else if (value.contains(DECIMAL_POINT)) {
			strNum = String.format(NUMBER_FORMAT_DECIMAL, Double.parseDouble(value));
		} else {
			strNum = String.format(NUMBER_FORMAT_INTEGER, Math.round(Double.parseDouble(value)));
		}

		jgen.writeString(strNum);
	}
}
