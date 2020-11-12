package ru.gkomega.api.openparser.xml.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Access(AccessType.FIELD)
@Table(name = "ContractorItems")
@NoArgsConstructor
@AllArgsConstructor
@BatchSize(size = 10)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ContractorItemEntity extends BaseItemEntity<UUID> {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 7227236304454621279L;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, optional = false)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "catalogItemId", referencedColumnName = "id", nullable = false)
    private CatalogItemEntity catalogItem;

    @Column(name = "refKey", nullable = false)
    @Type(type = "uuid-char")
    private UUID refKey;

    @Column(name = "dataVersion")
    private String dataVersion;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "parentKey")
    @Type(type = "uuid-char")
    private UUID parentKey;

    @Column(name = "folder")
    private boolean folder;

    @Column(name = "code")
    private String code;

    @Lob
    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "groupAccessKey")
    @Type(type = "uuid-char")
    private UUID groupAccessKey;

    @Column(name = "inn")
    private String inn;

    @Column(name = "okpo")
    private String okpo;

    @Lob
    @Column(name = "comments", length = 4096)
    private String comments;

    @Column(name = "kpp")
    private String kpp;

    @Lob
    @Column(name = "fullName", length = 1024)
    private String fullName;

    @Column(name = "bankAccountKey")
    @Type(type = "uuid-char")
    private UUID bankAccountKey;

    @Column(name = "responsibilityKey")
    @Type(type = "uuid-char")
    private UUID responsibilityKey;

    @Column(name = "registeredNumber")
    @Type(type = "uuid-char")
    private UUID registeredNumber;

    @Column(name = "fizKey")
    @Type(type = "uuid-char")
    private UUID fizKey;

    @Column(name = "jurFizKey")
    @Type(type = "uuid-char")
    private UUID jurFizKey;

    @Column(name = "magicId")
    private String magicId;

    @BatchSize(size = 10)
    @JsonBackReference
    @OneToMany(mappedBy = "contractorItem", targetEntity = ContactItemEntity.class, cascade = javax.persistence.CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Column(name = "contactItems")
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.TRUE)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @NotFound(action = NotFoundAction.IGNORE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ContactItemEntity> contactItems = new ArrayList<>();

    public void setContactItems(final List<ContactItemEntity> contactItems) {
        this.contactItems.clear();
        Optional.ofNullable(contactItems)
            .orElseGet(Collections::emptyList)
            .forEach(this::addContactItem);
    }

    public void addContactItem(final ContactItemEntity contactItemEntity) {
        this.contactItems.add(contactItemEntity);
        contactItemEntity.setContractorItem(this);
    }

    public void removeContactItem(final ContactItemEntity contactItemEntity) {
        this.contactItems.remove(contactItemEntity);
    }
}
