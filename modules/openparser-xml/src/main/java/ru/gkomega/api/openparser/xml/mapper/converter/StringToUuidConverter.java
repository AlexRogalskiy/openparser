package ru.gkomega.api.openparser.xml.mapper.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * {@link String} to {@link UUID} {@link Converter} implementation
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StringToUuidConverter implements Converter<String, UUID> {

    private final ConversionService conversionService;

    /**
     * Returns converted {@link UUID} from {@link String} by input {@link MappingContext}
     *
     * @param context - initial input {@link MappingContext} to convert from
     * @return converted {@link UUID}
     */
    @Nullable
    @Override
    public UUID convert(final MappingContext<String, UUID> context) {
        try {
            return Optional.ofNullable(context.getSource())
                .map(value -> this.conversionService.convert(value, UUID.class))
                .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}
