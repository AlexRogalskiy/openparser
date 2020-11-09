package ru.gkomega.api.openparser.xml.mapper.converter;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * {@link String} to {@link UUID} {@link Converter} implementation
 */
@Slf4j
@Component
//@RequiredArgsConstructor
public class OptionalConverter implements Converter<Optional<Object>, Object> {

//    private final ConversionService conversionService;

    /**
     * Returns converted {@link UUID} from {@link String} by input {@link MappingContext}
     *
     * @param context - initial input {@link MappingContext} to convert from
     * @return converted {@link UUID}
     */
    @Nullable
    @Override
    public Object convert(final MappingContext<Optional<Object>, Object> context) {
        return context.getSource().orElse(null);
    }
}
