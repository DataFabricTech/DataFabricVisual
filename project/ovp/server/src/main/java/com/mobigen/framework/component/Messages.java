package com.mobigen.framework.component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Component
public class Messages {
	private final MessageSource messageSource;
	private MessageSourceAccessor accessor;

	@PostConstruct
	private void init() {
		accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
	}

	public String get(String code) {
		try {
			return accessor.getMessage(code, LocaleContextHolder.getLocale());
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	public String get(String code, Object[] args) {
		try {
			return accessor.getMessage(code, args, LocaleContextHolder.getLocale());
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
}
