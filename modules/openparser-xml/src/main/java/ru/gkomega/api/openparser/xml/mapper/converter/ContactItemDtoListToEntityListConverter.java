package ru.gkomega.api.openparser.xml.mapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.gkomega.api.openparser.xml.model.dto.ContactItemDto;
import ru.gkomega.api.openparser.xml.model.entity.ContactItemEntity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

/**
 * Collection of {@link ContactItemDto}s to {@link ContactItemEntity}s {@link Converter} implementation
 */
@Component
public class ContactItemDtoListToEntityListConverter implements Converter<List<ContactItemDto>, List<ContactItemEntity>> {
    /**
     * Returns converted {@link UUID} from {@link String} by input {@link MappingContext}
     *
     * @param context - initial input {@link MappingContext} to convert from
     * @return converted {@link UUID}
     */
    @Nullable
    @Override
    public List<ContactItemEntity> convert(final MappingContext<List<ContactItemDto>, List<ContactItemEntity>> context) {
        return context.getSource()
            .stream()
            .map(ContactItemDto::getContactItemInfoList)
            .filter(not(CollectionUtils::isEmpty))
            .flatMap(Collection::stream)
            .map(this.mapToEntity())
            .collect(Collectors.toList());
    }

    private Function<ContactItemDto.ContactItemInfoDto, ContactItemEntity> mapToEntity() {
        return contactItemDto -> {
            final ContactItemEntity contactItemEntity = new ContactItemEntity();
            contactItemEntity.setActiveAt(contactItemDto.getActiveAt());
            contactItemEntity.setAddress(contactItemDto.getAddress());
            contactItemEntity.setCity(contactItemDto.getCity());
            contactItemEntity.setContactType(contactItemDto.getContactType());
            contactItemEntity.setCountry(contactItemDto.getCountry());
            contactItemEntity.setDomainName(contactItemDto.getDomainName());
            contactItemEntity.setFullPhoneNumber(contactItemDto.getFullPhoneNumber());
            contactItemEntity.setPhoneNumber(contactItemDto.getPhoneNumber());
            contactItemEntity.setRefKey(contactItemDto.getRefKey());
            contactItemEntity.setLineNumber(contactItemDto.getLineNumber());
            contactItemEntity.setRegion(contactItemDto.getRegion());
            contactItemEntity.setView(contactItemDto.getView());
            contactItemEntity.setViewKey(contactItemDto.getViewKey());
            contactItemEntity.setViewListKey(contactItemDto.getViewListKey());
            return contactItemEntity;
        };
    }
}
