package com.mobigen.framework.serializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Date를 YYYY-MM-DD 형태로 반환 한다
 *
 * @author rhcpn_000
 */
public class DateToYYYYMMDDSerializer extends com.fasterxml.jackson.databind.JsonSerializer<Date> {
	@Override
	public void serialize(Date value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

		String dateToStr = "";
		if (value != null)
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			dateToStr = format.format(value);
		}

		jgen.writeString(dateToStr);
	}
}
