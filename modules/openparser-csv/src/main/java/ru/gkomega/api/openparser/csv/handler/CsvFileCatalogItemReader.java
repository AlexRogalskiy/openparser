package ru.gkomega.api.openparser.csv.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import ru.gkomega.api.openparser.batch.property.ResourceProperty;
import ru.gkomega.api.openparser.csv.model.CatalogItemDto;

import javax.annotation.PostConstruct;

import static org.apache.commons.lang3.ArrayUtils.toArray;

@RequiredArgsConstructor
public class CsvFileCatalogItemReader extends FlatFileItemReader<CatalogItemDto> {

    private final ResourceProperty resourceProperty;

    @PostConstruct
    private void initialize() {
        final ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        final Resource resource = patternResolver.getResource(this.resourceProperty.getPathPattern());
        this.setResource(resource);

        final DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_COMMA);
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("firstName", "lastName", "country", "year");

        final BeanWrapperFieldSetMapper<CatalogItemDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(CatalogItemDto.class);

        final DefaultLineMapper<CatalogItemDto> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        this.setComments(toArray("#"));
        this.setLineMapper(defaultLineMapper);
    }
}
