package ru.gkomega.api.openparser.xstream.configuration;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.WriterWrapper;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

import java.io.Writer;

public class DashedJsonStreamDriver extends JsonHierarchicalStreamDriver {

    @Override
    public HierarchicalStreamWriter createWriter(final Writer out) {
        return new WriterWrapper(super.createWriter(out)) {

            @Override
            public void startNode(final String name) {
                this.startNode(name, null);
            }

            @Override
            public void startNode(final String name, final Class clazz) {
                this.wrapped.startNode(name.replace('-', '_'));
            }
        };
    }
}
