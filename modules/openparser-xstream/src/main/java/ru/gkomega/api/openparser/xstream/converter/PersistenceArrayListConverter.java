package ru.gkomega.api.openparser.xstream.converter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.persistence.FilePersistenceStrategy;
import com.thoughtworks.xstream.persistence.XmlArrayList;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public final class PersistenceArrayListConverter implements Converter {

    private final XStream xstream;

    public void marshal(final Object source,
                        final HierarchicalStreamWriter writer,
                        final MarshallingContext context) {
        final File dir = new File(System.getProperty("user.home"), "documents");
        final XmlArrayList list = new XmlArrayList(new FilePersistenceStrategy(dir, this.xstream));
        context.convertAnother(dir);
        list.addAll((Collection<?>) source); // generate the external files
    }

    public Object unmarshal(final HierarchicalStreamReader reader,
                            final UnmarshallingContext context) {
        final File directory = (File) context.convertAnother(null, File.class);
        final XmlArrayList persistentList = new XmlArrayList(new FilePersistenceStrategy(directory, this.xstream));
        final List<Object> list = new ArrayList<>(persistentList); // read all files
        persistentList.clear(); // remove files
        return list;
    }

    @Override
    public boolean canConvert(final Class type) {
        return type == ArrayList.class;
    }
}
