package ru.gkomega.api.openparser.commons.helper;

import org.springframework.context.i18n.LocaleContextHolder;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Default {@link SimpleDateFormat} {@link ThreadLocal} implementation
 */
public class SimpleDateFormatThreadLocal extends ThreadLocal<SimpleDateFormat> {
    /**
     * Default {@link TimeZone} instance
     */
    private static final TimeZone UTC = TimeZone.getTimeZone(ZoneId.of("UTC"));

    private final SimpleDateFormat format;

    public SimpleDateFormatThreadLocal(final String format) {
        this(format, LocaleContextHolder.getLocale());
    }

    public SimpleDateFormatThreadLocal(final String format,
                                       final Locale locale) {
        this(format, locale, UTC);
    }

    public SimpleDateFormatThreadLocal(final String format,
                                       final Locale locale,
                                       final TimeZone timeZone) {
        this.format = new SimpleDateFormat(format, locale);
        this.format.setTimeZone(timeZone);
    }

    @Override
    protected SimpleDateFormat initialValue() {
        return (SimpleDateFormat) this.format.clone();
    }
}
