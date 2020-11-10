package ru.gkomega.api.openparser.xml.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.convention.NamingConventions;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.List;

@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser Xml configuration")
public class XmlConfiguration {

    @Bean
    @Description("Conversion service bean")
    public ConversionService conversionService(final ObjectProvider<List<org.springframework.core.convert.converter.Converter<?, ?>>> convertersProvider) {
        final DefaultConversionService defaultConversionService = new DefaultConversionService();
        convertersProvider.ifAvailable(cc -> cc.forEach(defaultConversionService::addConverter));
        return defaultConversionService;
    }

    @Bean
    @Description("Model mapper configuration bean")
    public ModelMapper modelMapper(final ObjectProvider<List<ModelMapperCustomizer>> modelMapperCustomizersProvider) {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapperCustomizersProvider.ifAvailable(cc -> cc.forEach(c -> c.customize(modelMapper)));
        return modelMapper;
    }

    @Bean
    @Description("Model mapper configuration customizer bean")
    public ModelMapperCustomizer configurationModelMapperCustomizer() {
        return modelMapper -> modelMapper.getConfiguration()
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
            .setMethodAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PUBLIC)
            .setSourceNamingConvention(NamingConventions.JAVABEANS_MUTATOR)
            .setSourceNamingConvention(NamingConventions.JAVABEANS_ACCESSOR)
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setSourceNameTokenizer(NameTokenizers.CAMEL_CASE)
            .setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE)
            .setAmbiguityIgnored(true)
            .setSkipNullEnabled(true)
            .setFieldMatchingEnabled(true)
            .setFullTypeMatchingRequired(true)
            .setImplicitMappingEnabled(true);
    }

    @Bean
    @Description("Model mapper converters customizer bean")
    public ModelMapperCustomizer converterModelMapperCustomizer(final ObjectProvider<List<org.modelmapper.Converter<?, ?>>> converterListProvider) {
        return modelMapper -> converterListProvider.ifAvailable(c -> c.forEach(modelMapper::addConverter));
    }

    @Bean
    @Description("Model mapper property mappings customizer bean")
    public ModelMapperCustomizer propertyMappingModelMapperCustomizer(final ObjectProvider<List<PropertyMap<?, ?>>> propertyMapListProvider) {
        return modelMapper -> propertyMapListProvider.ifAvailable(c -> c.forEach(modelMapper::addMappings));
    }
}
