package ru.gkomega.api.openparser.xstream.configuration;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

import java.io.Writer;

public class CustomJsonHierarchicalStreamDriver extends JsonHierarchicalStreamDriver {

    @Override
    public HierarchicalStreamWriter createWriter(final Writer writer) {
        return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
    }
}
