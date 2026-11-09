package com.mobigen.framework.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

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
	@Override
	public void serialize(String value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

		String strNum = "";
		if (value != null && !value.equals(""))
		{
			if( "-".equals(value) ) {
				strNum = value;
			} else if( value.indexOf(".") > 0 ) {
				strNum = String.format("%,.2f", Double.parseDouble(value));
			} else {
				strNum = String.format("%,d", Math.round(Double.parseDouble(value)));
			}
		}

		jgen.writeString(strNum);
	}
}
