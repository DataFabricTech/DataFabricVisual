package com.mobigen.framework.test;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.result.PrintingResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest
@Disabled
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
public abstract class AbstractRestDocTest {
	@Autowired
	private WebApplicationContext context;

	protected MockMvc mockMvc;

	protected MockHttpSession mockSession;

	@BeforeEach
	public void setup(RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(documentationConfiguration(restDocumentation).operationPreprocessors()
						.withResponseDefaults(prettyPrint()))
				.apply(springSecurity()).addFilters(new CharacterEncodingFilter("UTF-8", true)).build();

		this.mockSession = new MockHttpSession();
	}

	// Test 결과를 console에 출력
	protected ResultHandler print() throws Exception {
		return this.print(false, false);
	}

	protected ResultHandler print(boolean ignoreBody, boolean samplingBody) throws Exception {
		PrintStream ps = new PrintStream(System.out, true, "UTF-8");
		return new PrintWriterPrintingResultHandler(new PrintWriter(ps, true), ignoreBody, samplingBody);
	}

	protected String getJsonResultData(String result) throws Exception {
		JSONObject jsonObject = new JSONObject(result);
		String data = jsonObject.getString("data");
		return data;
	}

	private static class PrintWriterPrintingResultHandler extends PrintingResultHandler {
		public PrintWriterPrintingResultHandler(PrintWriter writer, boolean ignoreBody, boolean samplingBody) {
			super(new ResultValuePrinter() {
				@Override
				public void printHeading(String heading) {
					writer.println();
					writer.println(String.format("%s:", heading));
				}

				@Override
				public void printValue(String label, @Nullable Object value) {
					if (ignoreBody && label.equals("Body")) {
						return;
					}

					if (value != null) {
						if (value.getClass().isArray()) {
							value = CollectionUtils.arrayToList(value);
						} else if (value instanceof String) {
							if (samplingBody) {
								String v = value.toString();
								if (v != null && v.length() > (1024 * 10)) {
									value = v.substring(0, (1024 * 10) - 6) + "......";
								}
							}
						}
					}

					writer.println(String.format("%17s = %s", label, value));
				}
			});
		}
	}

	protected String expectedJsonResultStr = "{result:1, errorMessage:\"\"}";
}