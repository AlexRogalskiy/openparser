package ru.gkomega.api.openparser.xml.model.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("ЗначенияПолей")
public class AddressDto implements Serializable {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = -2422128148414701941L;

    @XStreamAlias("Индекс")
    private String zip;

    @XStreamAlias("Регион")
    private String region;

    @XStreamAlias("Район")
    private String district;

    @XStreamAlias("НаселенныйПункт")
    private String city;

    @XStreamAlias("Улица")
    private String street;

    @XStreamAlias("Дом")
    private String building;
}
