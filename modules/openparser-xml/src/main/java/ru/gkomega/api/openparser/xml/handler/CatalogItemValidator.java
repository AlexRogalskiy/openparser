package ru.gkomega.api.openparser.xml.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.stereotype.Component;
import ru.gkomega.api.openparser.xml.model.dto.CatalogItemDto;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class CatalogItemValidator extends BeanValidatingItemProcessor<CatalogItemDto> {

    @PostConstruct
    private void initialize() throws Exception {
        this.setFilter(true);
        this.afterPropertiesSet();
    }
}
