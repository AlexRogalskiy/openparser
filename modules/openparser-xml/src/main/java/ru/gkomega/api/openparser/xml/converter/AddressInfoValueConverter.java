package ru.gkomega.api.openparser.xml.converter;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import ru.gkomega.api.openparser.xml.model.dto.AddressDto;

public class AddressInfoValueConverter implements SingleValueConverter {
    /**
     * Default field delimiter
     */
    private static final String DELIMITER = ",";

    @Override
    public boolean canConvert(final Class clazz) {
        return AddressDto.class.isAssignableFrom(clazz);
    }

    @Override
    public String toString(final Object obj) {
        final AddressDto address = (AddressDto) obj;
        return String.join(
            DELIMITER,
            address.getZip(),
            address.getRegion(),
            address.getDistrict(),
            address.getCity(),
            address.getStreet(),
            address.getBuilding()
        );
    }

    @Override
    public Object fromString(final String str) {
        final String[] values = str.split(DELIMITER);
        return AddressDto.builder()
            .zip(values[0])
            .region(values[1])
            .district(values[2])
            .city(values[3])
            .street(values[4])
            .building(values[5])
            .build();
    }
}
