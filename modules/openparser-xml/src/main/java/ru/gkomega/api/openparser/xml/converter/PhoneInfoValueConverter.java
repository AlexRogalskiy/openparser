package ru.gkomega.api.openparser.xml.converter;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import ru.gkomega.api.openparser.xml.model.dto.PhoneDto;

public class PhoneInfoValueConverter implements SingleValueConverter {
    /**
     * Default field delimiter
     */
    private static final String DELIMITER = ",";

    @Override
    public boolean canConvert(final Class clazz) {
        return PhoneDto.class.isAssignableFrom(clazz);
    }

    @Override
    public String toString(final Object obj) {
        final PhoneDto phone = (PhoneDto) obj;
        return String.join(
            DELIMITER,
            phone.getCityCode(),
            phone.getCountryCode(),
            phone.getPhoneNumber()
        );
    }

    @Override
    public Object fromString(final String str) {
        final String[] values = str.split(",");
        return PhoneDto.builder()
            .cityCode(values[0])
            .countryCode(values[1])
            .phoneNumber(values[2])
            .build();
    }
}
