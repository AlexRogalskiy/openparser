package ru.gkomega.api.openparser.xml.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "ContactItems")
@NoArgsConstructor
@AllArgsConstructor
@BatchSize(size = 10)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ContactItemEntity extends BaseItemEntity<UUID> {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = -6089988666482188649L;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, optional = false)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "contractorItemId", referencedColumnName = "id", nullable = false)
    private ContractorItemEntity contractorItem;

    @Column(name = "refKey")
    @Type(type = "uuid-char")
    private UUID refKey;

    @Column(name = "lineNumber")
    private int lineNumber;

    @Column(name = "contactType")
    private String contactType;

    @Column(name = "viewKey")
    @Type(type = "uuid-char")
    private UUID viewKey;

    @Column(name = "view")
    private String view;

    @Column(name = "country")
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "domainName")
    private String domainName;

    @Column(name = "fullPhoneNumber")
    private String fullPhoneNumber;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "viewListKey")
    @Type(type = "uuid-char")
    private UUID viewListKey;

    @Column(name = "activeAt")
    private LocalDateTime activeAt;
}
