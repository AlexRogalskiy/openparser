package ru.gkomega.api.openparser.db.configuration;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FormatterLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.gkomega.api.openparser.batch.handler.ElapsedTimeJobListener;
import ru.gkomega.api.openparser.batch.handler.SequenceRunIdIncrementer;
import ru.gkomega.api.openparser.db.model.CustomerCreditDto;

@Configuration
public abstract class DbBatchItemConfiguration {

    @Bean
    public SequenceRunIdIncrementer jobParametersIncrementer(final JdbcTemplate jdbcTemplate) {
        return new SequenceRunIdIncrementer(jdbcTemplate);
    }

    @Bean
    @Scope("prototype")
    public DefaultLineMapper<CustomerCreditDto> lineMapper(final BeanWrapperFieldSetMapper<CustomerCreditDto> beanWrapperFieldSetMapper,
                                                           final DelimitedLineTokenizer delimitedLineTokenizer) {
        final DefaultLineMapper<CustomerCreditDto> lineMapper = new DefaultLineMapper<>();
        lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
        lineMapper.setLineTokenizer(delimitedLineTokenizer);
        return lineMapper;
    }

    @Bean
    @Scope("prototype")
    public DelimitedLineTokenizer delimitedLineTokenizer() {
        final DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setNames("name", "surName", "age", "dateOfBirth", "address");
        return tokenizer;
    }

    @Bean
    @Scope("prototype")
    public BeanWrapperFieldSetMapper<CustomerCreditDto> beanWrapperFieldSetMapper() {
        final BeanWrapperFieldSetMapper<CustomerCreditDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(CustomerCreditDto.class);
        return fieldSetMapper;
    }

    @Bean
    @Scope("prototype")
    public FormatterLineAggregator<CustomerCreditDto> lineAggregator(final BeanWrapperFieldExtractor<CustomerCreditDto> beanWrapperFieldExtractor) {
        final FormatterLineAggregator<CustomerCreditDto> lineAggregator = new FormatterLineAggregator<>();
        lineAggregator.setFieldExtractor(beanWrapperFieldExtractor);
        lineAggregator.setFormat("%-5s, %-5s, %-5d, %-20s, %20s");
        return lineAggregator;
    }

    @Bean
    @Scope("prototype")
    public BeanWrapperFieldExtractor<CustomerCreditDto> beanWrapperFieldExtractor() {
        final BeanWrapperFieldExtractor<CustomerCreditDto> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"surName", "name", "age", "address", "dateOfBirth"});
        return fieldExtractor;
    }

    @Bean
    public BeanPropertyRowMapper<CustomerCreditDto> beanPropertyRowMapper() {
        return new BeanPropertyRowMapper<>(CustomerCreditDto.class);
    }

    @Bean
    public ElapsedTimeJobListener elapsedTimeJobListener() {
        return new ElapsedTimeJobListener();
    }
}
