package ru.gkomega.api.openparser.xml.mapper.propertymap;

import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import ru.gkomega.api.openparser.xml.model.dto.CatalogItemDto;
import ru.gkomega.api.openparser.xml.model.entity.CatalogItemEntity;

/**
 * {@link CatalogItemDto} to {@link CatalogItemEntity} {@link PropertyMap} binding configuration
 */
@Component
public class CatalogItemDtoToEntityPropertyMap extends PropertyMap<CatalogItemDto, CatalogItemEntity> {
    /**
     * {@link CatalogItemEntity} {@link PropertyMap} configuration
     */
    @Override
    protected void configure() {
        // mapping destination properties
        this.map(this.source.getRefKey()).setRefKey(null);
        this.map(this.source.getAuthor()).setAuthor(null);
        this.map(this.source.getCategory()).setCategory(null);
        this.map(this.source.getSummary()).setSummary(null);
        this.map(this.source.getTitle()).setTitle(null);
        this.map(this.source.getUpdatedAt()).setUpdatedAt(null);
        this.map(this.source.getContractorItemList()).setContractorItems(null);
    }
}
