package ru.gkomega.api.openparser.xstream.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UuidConverter implements Converter {
    /**
     * Default guid pattern
     */
    public static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");

    @Override
    public boolean canConvert(final Class clazz) {
        return UUID.class.isAssignableFrom(clazz);
    }

    @Override
    public void marshal(final Object value,
                        final HierarchicalStreamWriter writer,
                        final MarshallingContext arg2) {
        writer.setValue(value.toString());
    }

    @Override
    public Object unmarshal(final HierarchicalStreamReader reader,
                            final UnmarshallingContext context) {
        return Optional.ofNullable(reader.getValue())
            .map(Object::toString)
            .map(UUID_PATTERN::matcher)
            .filter(Matcher::find)
            .map(Matcher::group)
            .map(this.mapToUuid())
            .orElse(null);
    }

    private Function<String, UUID> mapToUuid() {
        return value -> {
            try {
                return UUID.fromString(value);
            } catch (Exception e) {
                return null;
            }
        };
    }
}
