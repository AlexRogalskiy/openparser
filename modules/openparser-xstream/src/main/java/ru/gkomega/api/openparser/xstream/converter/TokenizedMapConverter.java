package ru.gkomega.api.openparser.xstream.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

public class TokenizedMapConverter implements Converter {

    @Override
    public boolean canConvert(final Class clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public void marshal(final Object value,
                        final HierarchicalStreamWriter writer,
                        final MarshallingContext arg2) {
        final Map<String, String> result = (Map<String, String>) value;
        writer.setValue(collectionToCommaDelimitedString(result.entrySet()));
    }

    @Override
    public Object unmarshal(final HierarchicalStreamReader reader,
                            final UnmarshallingContext context) {
        final Map<String, String> result = new HashMap<>();
        if (isNotEmpty(reader.getValue())) {
            final String[] responseArray = reader.getValue().split("(\\r)?\\n");
            for (final String responseElement : responseArray) {
                final String[] responseParamArray = responseElement.split("=");
                result.put(responseParamArray[0], responseParamArray[1]);
            }
        }
        return result;
    }
}
