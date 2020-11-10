package ru.gkomega.api.openparser.xml.handler;

import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.oxm.Unmarshaller;
import ru.gkomega.api.openparser.xml.model.dto.CatalogItemDto;
import ru.gkomega.api.openparser.xml.property.XmlResourceProperty;

public class XmlFileCatalogItemReader extends StaxEventItemReader<CatalogItemDto> {

    public XmlFileCatalogItemReader(final XmlResourceProperty resourceProperty,
                                    final Unmarshaller unmarshaller) {
        this.setFragmentRootElementNames(resourceProperty.getRootElements());
        this.setSaveState(false);
        this.setUnmarshaller(unmarshaller);
    }
}
