package ru.gkomega.api.openparser.xml.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import ru.gkomega.api.openparser.xml.model.entity.CatalogItemEntity;
import ru.gkomega.api.openparser.xml.service.EntityDaoService;

import javax.persistence.PersistenceException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogItemWriter implements ItemWriter<CatalogItemEntity> {

    private final EntityDaoService entityDaoService;

    @Override
    public void write(final List<? extends CatalogItemEntity> list) {
        list.forEach(this::saveCatalogItem);
    }

    private void saveCatalogItem(final CatalogItemEntity itemEntity) {
        log.info(">>> Saving catalog item: {}", itemEntity);

        try {
            itemEntity.setContractorItemList(null);
            this.entityDaoService.update(itemEntity);
        } catch (Exception ex) {
            throw new PersistenceException("Could not store catalog item", ex);
        }
    }
}
