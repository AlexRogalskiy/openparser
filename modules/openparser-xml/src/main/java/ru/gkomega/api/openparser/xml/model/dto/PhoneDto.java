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
public class PhoneDto implements Serializable {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 1944435855602959462L;

    @XStreamAlias("КодСтраны")
    private String countryCode;

    @XStreamAlias("КодГорода")
    private String cityCode;

    @XStreamAlias("НомерТелефона")
    private String phoneNumber;
}
