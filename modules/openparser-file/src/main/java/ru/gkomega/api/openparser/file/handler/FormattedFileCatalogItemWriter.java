package ru.gkomega.api.openparser.file.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.FormatterLineAggregator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import ru.gkomega.api.openparser.batch.property.ResourceProperty;
import ru.gkomega.api.openparser.file.model.CustomerCreditDto;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
public class FormattedFileCatalogItemWriter extends FlatFileItemWriter<CustomerCreditDto> {

    private final ResourceProperty resourceProperty;

    @PostConstruct
    private void initialize() {
        final ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        final Resource resource = patternResolver.getResource(this.resourceProperty.getPathPattern());
        this.setResource(resource);

        final BeanWrapperFieldExtractor<CustomerCreditDto> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"name", "credit"});
        fieldExtractor.afterPropertiesSet();

        final FormatterLineAggregator<CustomerCreditDto> lineAggregator = new FormatterLineAggregator<>();
        lineAggregator.setFormat("%-9s%-2.0f");
        lineAggregator.setFieldExtractor(fieldExtractor);

        this.setLineAggregator(lineAggregator);
    }
}
