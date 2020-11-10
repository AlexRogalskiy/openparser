package ru.gkomega.api.openparser.xstream.configuration;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.core.ReferenceByXPathMarshallingStrategy;
import com.thoughtworks.xstream.hibernate.mapper.HibernateMapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Role;
import ru.gkomega.api.openparser.xstream.property.XStreamProperty;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
@ConditionalOnProperty(prefix = XStreamProperty.PROPERTY_PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(XStreamProperty.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Description("OpenParser XStream configuration")
public abstract class XStreamConfiguration {
    /**
     * Default bean naming conventions
     */
    public static final String XSTREAM_PROVIDER_BEAN_NAME = "xStreamProvider";
    public static final String XSTREAM_DEFAULT_PROVIDER_BEAN_NAME = "xStreamDefaultProvider";
    public static final String XSTREAM_CONFIGURATION_PROVIDER_BEAN_NAME = "xstreamConfigurationProvider";

    public static final String XSTREAM_APPLICATION_CUSTOMIZER_PROVIDER_BEAN_NAME = "xStreamApplicationCustomizer";
    public static final String XSTREAM_PERMISSION_CUSTOMIZER_PROVIDER_BEAN_NAME = "xStreamPermissionCustomizerProvider";
    public static final String XSTREAM_CONVERTER_CUSTOMIZER_PROVIDER_BEAN_NAME = "xStreamConverterCustomizer";

    @Bean(XSTREAM_PROVIDER_BEAN_NAME)
    @ConditionalOnMissingBean(name = XSTREAM_PROVIDER_BEAN_NAME)
    @Description("XStream configuration provider bean")
    public BiFunction<Optional<XStream>, XStreamProperty.XStreamConfiguration, XStream> xStreamProvider(final ObjectProvider<List<Function<XStreamProperty.XStreamConfiguration, XStreamCustomizer>>> xStreamCustomizerProvider) {
        return (xStreamSupplier, property) -> {
            final XStream xStream = xStreamSupplier.orElseGet(() -> Optional.ofNullable(property.getStreamDriver()).map(XStream::new).orElseGet(XStream::new));
            xStreamCustomizerProvider.ifAvailable(cc -> cc.stream().map(c -> c.apply(property)).forEach(c -> c.customize(xStream)));
            return xStream;
        };
    }

    @Bean(XSTREAM_DEFAULT_PROVIDER_BEAN_NAME)
    @ConditionalOnMissingBean(name = XSTREAM_DEFAULT_PROVIDER_BEAN_NAME)
    @Description("XStream default configuration provider bean")
    public Function<XStreamProperty.XStreamConfiguration, XStream> xStreamDefaultProvider(final BiFunction<Optional<XStream>, XStreamProperty.XStreamConfiguration, XStream> xStreamProvider) {
        return property -> xStreamProvider.apply(Optional.empty(), property);
    }

    @Bean(XSTREAM_APPLICATION_CUSTOMIZER_PROVIDER_BEAN_NAME)
    @ConditionalOnMissingBean(name = XSTREAM_APPLICATION_CUSTOMIZER_PROVIDER_BEAN_NAME)
    @Description("XStream application customizer provider bean")
    public Function<XStreamProperty.XStreamConfiguration, XStreamCustomizer> xStreamApplicationCustomizerProvider() {
        return property -> xStream -> {
            xStream.setMarshallingStrategy(new ReferenceByXPathMarshallingStrategy(ReferenceByXPathMarshallingStrategy.RELATIVE));
            xStream.setMode(property.getMode());
            xStream.autodetectAnnotations(property.isAutodetectAnnotations());
            xStream.ignoreUnknownElements(property.getIgnorePattern());
        };
    }

    @Bean(XSTREAM_PERMISSION_CUSTOMIZER_PROVIDER_BEAN_NAME)
    @ConditionalOnMissingBean(name = XSTREAM_PERMISSION_CUSTOMIZER_PROVIDER_BEAN_NAME)
    @Description("XStream permission customizer provider bean")
    public Function<XStreamProperty.XStreamConfiguration, XStreamCustomizer> xStreamPermissionCustomizerProvider() {
        return property -> xStream -> {
            xStream.addPermission(NoTypePermission.NONE);
            xStream.addPermission(NullPermission.NULL);
            xStream.addPermission(PrimitiveTypePermission.PRIMITIVES);
        };
    }

    @Bean(XSTREAM_CONVERTER_CUSTOMIZER_PROVIDER_BEAN_NAME)
    @ConditionalOnMissingBean(name = XSTREAM_CONVERTER_CUSTOMIZER_PROVIDER_BEAN_NAME)
    @Description("XStream converter customizer provider bean")
    public Function<XStreamProperty.XStreamConfiguration, XStreamCustomizer> xStreamConverterCustomizerProvider(final ObjectProvider<List<Converter>> xStreamConverterProvider) {
        return property -> xStream -> xStreamConverterProvider.ifAvailable(c -> c.forEach(xStream::registerConverter));
    }

    @Bean(XSTREAM_CONFIGURATION_PROVIDER_BEAN_NAME)
    @ConditionalOnMissingBean(name = XSTREAM_CONFIGURATION_PROVIDER_BEAN_NAME)
    @Description("XStream configuration factory bean")
    public XStreamConfigurationProvider xstreamConfigurationProvider(final XStreamProperty property) {
        return name -> property.getSettings().get(name);
    }

    @Bean
    @ConditionalOnMissingBean
    @Description("XStream supplier bean")
    public Supplier<XStream> xStreamSupplier() {
        return () -> new XStream() {
            @Override
            protected MapperWrapper wrapMapper(final MapperWrapper next) {
                return new HibernateMapper(next);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    @Description("XStream filtered supplier bean")
    public Supplier<XStream> xStreamFilteredSupplier(final ObjectProvider<Set<String>> filteredElements) {
        return () -> new XStream() {
            @Override
            protected MapperWrapper wrapMapper(final MapperWrapper next) {
                final Set<String> elements = filteredElements.getIfAvailable(Collections::emptySet);
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(final Class definedIn,
                                                         final String fieldName) {
                        if (elements.contains(fieldName)) {
                            return false;
                        }
                        return super.shouldSerializeMember(definedIn, fieldName);
                    }
                };
            }
        };
    }
}
