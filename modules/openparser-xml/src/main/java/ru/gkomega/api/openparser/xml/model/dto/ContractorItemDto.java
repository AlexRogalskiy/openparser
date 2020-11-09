package ru.gkomega.api.openparser.xml.model.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.gkomega.api.openparser.commons.annotation.Inn;
import ru.gkomega.api.openparser.commons.annotation.Kpp;
import ru.gkomega.api.openparser.commons.annotation.NullOrNotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("content")
public class ContractorItemDto implements Serializable {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 7519233850304400795L;

    @Valid
    @NotNull(message = "{model.catalog.contractor.properties.notNull}")
    @XStreamAlias("properties")
    private ContractorItemInfoDto contractorItemInfo;

    @Data
    @Validated
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractorItemInfoDto {

        @XStreamAlias("Ref_Key")
        private UUID refKey;

        @XStreamAlias("DataVersion")
        private String dataVersion;

        @XStreamAlias("DeletionMark")
        private boolean deleted;

        @XStreamAlias("Parent_Key")
        private UUID parentKey;

        @XStreamAlias("IsFolder")
        private boolean folder;

        @XStreamAlias("Code")
        private String code;

        @XStreamAlias("Description")
        private String description;

        @XStreamAlias("ГруппаДоступа_Key")
        private UUID groupAccessKey;

        @Inn(message = "{model.contractor.inn.notValid}")
        @XStreamAlias("ИНН")
        private String inn;

        @XStreamAlias("КодПоОКПО")
        private String okpo;

        @XStreamAlias("Комментарий")
        private String comments;

        @Kpp(message = "{model.contractor.kpp.notValid}")
        @XStreamAlias("КПП")
        private String kpp;

        @XStreamAlias("НаименованиеПолное")
        private String fullName;

        @XStreamAlias("ОсновнойБанковскийСчет_Key")
        private Optional<UUID> bankAccountKey;

        @XStreamAlias("Ответственный_Key")
        private Optional<UUID> responsibilityKey;

        @XStreamAlias("РегистрационныйНомер")
        private Optional<UUID> registeredNumber;

        @XStreamAlias("ФизЛицо_Key")
        private Optional<UUID> fizKey;

        @XStreamAlias("ЮрФизЛицо")
        private Optional<UUID> jurFizKey;

        @XStreamAlias("сфпCoMagicID")
        private String magicId;

        @XStreamOmitField
        @XStreamAlias("ДополнительныеРеквизиты")
        private Object additionalInfo;

        @XStreamOmitField
        @XStreamAlias("Predefined")
        private boolean predefined;

        @XStreamOmitField
        @XStreamAlias("PredefinedDataName")
        private String predefinedDataName;

        @Valid
        @NullOrNotEmpty(message = "{model.contractor.contact.nullOrNotEmpty}")
        @XStreamImplicit(itemFieldName = "КонтактнаяИнформация")
        private List<@NotNull ContactItemDto> contactItemList;
    }
}
