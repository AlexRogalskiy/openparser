package ru.gkomega.api.openparser.xstream.converter;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateConverter implements Converter {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public boolean canConvert(final Class clazz) {
        return Date.class.isAssignableFrom(clazz);
    }

    @Override
    public void marshal(final Object value,
                        final HierarchicalStreamWriter writer,
                        final MarshallingContext arg2) {
        final Date date = (Date) value;
        writer.setValue(formatter.format(date));
    }

    @Override
    public Object unmarshal(final HierarchicalStreamReader reader,
                            final UnmarshallingContext arg1) {
        final GregorianCalendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(formatter.parse(reader.getValue()));
        } catch (ParseException e) {
            throw new ConversionException(e.getMessage(), e);
        }
        return calendar;
    }
}