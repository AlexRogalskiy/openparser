package ru.gkomega.api.openparser.xml.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Access(AccessType.FIELD)
@Table(name = "CatalogItems")
@NoArgsConstructor
@AllArgsConstructor
@BatchSize(size = 10)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class CatalogItemEntity extends BaseItemEntity<UUID> {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 1261432457095363629L;

    @Column(name = "refKey", nullable = false)
    @Type(type = "uuid-char")
    private UUID refKey;

    @Column(name = "category")
    private String category;

    @Column(name = "title")
    private String title;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "author")
    private String author;

    @Lob
    @Column(name = "summary", length = 1024)
    private String summary;

    @BatchSize(size = 10)
    @JsonBackReference
    @OneToMany(mappedBy = "catalogItem", targetEntity = ContractorItemEntity.class, cascade = javax.persistence.CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Column(name = "contractorItems")
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.TRUE)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @NotFound(action = NotFoundAction.IGNORE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ContractorItemEntity> contractorItems = new ArrayList<>();

    public void setContractorItems(final List<ContractorItemEntity> contractorItems) {
        Optional.ofNullable(contractorItems)
            .orElseGet(Collections::emptyList)
            .forEach(this::addContractorItem);
    }

    public void addContractorItem(final ContractorItemEntity contractorItemEntity) {
        this.contractorItems.add(contractorItemEntity);
        contractorItemEntity.setCatalogItem(this);
    }

    public void removeContractorItem(final ContractorItemEntity contractorItemEntity) {
        //contractorItemEntity.setCatalogItem(null);
        this.contractorItems.remove(contractorItemEntity);
    }
}
