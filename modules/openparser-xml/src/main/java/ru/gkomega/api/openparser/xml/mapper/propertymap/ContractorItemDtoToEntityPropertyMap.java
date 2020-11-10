package ru.gkomega.api.openparser.xml.mapper.propertymap;

import lombok.RequiredArgsConstructor;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import ru.gkomega.api.openparser.xml.mapper.converter.ContactItemDtoListToEntityListConverter;
import ru.gkomega.api.openparser.xml.mapper.converter.StringToUuidConverter;
import ru.gkomega.api.openparser.xml.model.dto.ContractorItemDto;
import ru.gkomega.api.openparser.xml.model.entity.ContractorItemEntity;

/**
 * {@link ContractorItemDto} to {@link ContractorItemEntity} {@link PropertyMap} binding configuration
 */
@Component
@RequiredArgsConstructor
public class ContractorItemDtoToEntityPropertyMap extends PropertyMap<ContractorItemDto, ContractorItemEntity> {

    private final ContactItemDtoListToEntityListConverter contactItemDtoListToEntityListConverter;
    private final StringToUuidConverter stringToUuidConverter;

    /**
     * {@link ContractorItemEntity} {@link PropertyMap} configuration
     */
    @Override
    protected void configure() {
        // mapping destination properties
        this.map(this.source.getContractorItemInfo().getRefKey()).setRefKey(null);
        this.map(this.source.getContractorItemInfo().getCode()).setCode(null);
        this.map(this.source.getContractorItemInfo().getComments()).setComments(null);
        this.map(this.source.getContractorItemInfo().getDataVersion()).setDataVersion(null);
        this.map(this.source.getContractorItemInfo().getDescription()).setDescription(null);
        this.map(this.source.getContractorItemInfo().getFullName()).setFullName(null);
        this.map(this.source.getContractorItemInfo().getGroupAccessKey()).setGroupAccessKey(null);
        this.map(this.source.getContractorItemInfo().getInn()).setInn(null);
        this.map(this.source.getContractorItemInfo().getKpp()).setKpp(null);
        this.map(this.source.getContractorItemInfo().getMagicId()).setMagicId(null);
        this.map(this.source.getContractorItemInfo().getOkpo()).setOkpo(null);
        this.map(this.source.getContractorItemInfo().getParentKey()).setParentKey(null);

        this.map().setDeleted(this.source.getContractorItemInfo().isDeleted());
        this.map().setFolder(this.source.getContractorItemInfo().isFolder());

        // mapping destination properties via converters
        this.using(this.stringToUuidConverter).map(this.source.getContractorItemInfo().getBankAccountKey()).setBankAccountKey(null);
        this.using(this.stringToUuidConverter).map(this.source.getContractorItemInfo().getFizKey()).setFizKey(null);
        this.using(this.stringToUuidConverter).map(this.source.getContractorItemInfo().getJurFizKey()).setJurFizKey(null);
        this.using(this.stringToUuidConverter).map(this.source.getContractorItemInfo().getRegisteredNumber()).setRegisteredNumber(null);
        this.using(this.stringToUuidConverter).map(this.source.getContractorItemInfo().getResponsibilityKey()).setResponsibilityKey(null);
        this.using(this.contactItemDtoListToEntityListConverter).map(this.source.getContractorItemInfo().getContactItemList()).setContactItems(null);
    }
}
