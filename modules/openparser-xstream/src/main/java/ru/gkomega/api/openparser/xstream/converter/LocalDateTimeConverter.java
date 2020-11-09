package ru.gkomega.api.openparser.xstream.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.springframework.context.i18n.LocaleContextHolder;
import ru.gkomega.api.openparser.commons.helper.SimpleDateFormatThreadLocal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class LocalDateTimeConverter implements Converter {

    public static final ThreadLocal<SimpleDateFormat> LOCALE_DATE_TIME_FORMAT =
        new SimpleDateFormatThreadLocal("yyyy-MM-dd'T'HH:mm:ss", LocaleContextHolder.getLocale());

    @Override
    public boolean canConvert(final Class clazz) {
        return LocalDateTime.class.isAssignableFrom(clazz);
    }

    @Override
    public void marshal(final Object value,
                        final HierarchicalStreamWriter writer,
                        final MarshallingContext arg2) {
        writer.setValue(LOCALE_DATE_TIME_FORMAT.get().format(value));
    }

    @Override
    public Object unmarshal(final HierarchicalStreamReader reader,
                            final UnmarshallingContext context) {
        try {
            return LOCALE_DATE_TIME_FORMAT.get().parse(reader.getValue());
        } catch (ParseException e) {
            return null;
        }
    }
}
