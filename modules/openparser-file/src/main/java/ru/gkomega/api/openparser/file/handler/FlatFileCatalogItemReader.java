package ru.gkomega.api.openparser.file.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import ru.gkomega.api.openparser.batch.property.ResourceProperty;
import ru.gkomega.api.openparser.file.model.CustomerCreditDto;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
public class FlatFileCatalogItemReader extends FlatFileItemReader<CustomerCreditDto> {

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

        final BeanWrapperFieldSetMapper<CustomerCreditDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(CustomerCreditDto.class);

        final DefaultLineMapper<CustomerCreditDto> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        this.setLineMapper(defaultLineMapper);
    }
}
