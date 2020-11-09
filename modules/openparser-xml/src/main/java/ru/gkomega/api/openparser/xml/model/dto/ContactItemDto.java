package ru.gkomega.api.openparser.xml.model.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.gkomega.api.openparser.commons.annotation.NullOrNotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
//@XStreamAlias("КонтактнаяИнформация")
public class ContactItemDto implements Serializable {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 8013605854885968418L;

    @Valid
    @NullOrNotEmpty(message = "{model.catalog.contact.element.nullOrNotEmpty}")
//    @XStreamAlias("element")
    @XStreamImplicit(itemFieldName = "element")
    private List<@NotNull ContactItemInfoDto> contactItemInfoList;

    @Data
    @Validated
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactItemInfoDto {

        @XStreamAlias("Ref_Key")
        private UUID refKey;

        @XStreamAlias("LineNumber")
        private int lineNumber;

        @XStreamAlias("Тип")
        private String contactType;

        @XStreamAlias("Вид_Key")
        private UUID viewKey;

        @XStreamAlias("Представление")
        private String view;

        @XStreamOmitField
        @XStreamAlias("ЗначенияПолей")
        private String viewData;

        @XStreamAlias("Страна")
        private String country;

        @XStreamAlias("Регион")
        private String region;

        @XStreamAlias("Город")
        private String city;

        @XStreamAlias("АдресЭП")
        private String address;

        @XStreamAlias("ДоменноеИмяСервера")
        private String domainName;

        @XStreamAlias("НомерТелефона")
        private String fullPhoneNumber;

        @XStreamAlias("НомерТелефонаБезКодов")
        private String phoneNumber;

        @XStreamAlias("ВидДляСписка_Key")
        private UUID viewListKey;

        @XStreamAlias("ДействуетС")
        private LocalDateTime activeAt;
    }
}
