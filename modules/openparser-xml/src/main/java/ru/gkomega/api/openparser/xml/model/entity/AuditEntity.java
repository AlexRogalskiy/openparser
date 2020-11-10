package ru.gkomega.api.openparser.xml.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.annotations.*;
import org.hibernate.tuple.ValueGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.domain.Auditable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

import static org.apache.commons.lang3.ThreadUtils.getSystemThreadGroup;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@MappedSuperclass
public abstract class AuditEntity<ID extends Serializable> implements Auditable<String, ID, Instant>, Serializable {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = -1818342610029508440L;

    @PastOrPresent
    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Column(name = "created", nullable = false, updatable = false)
    private Instant createdDate;

    @CreatedBy
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "created_by", nullable = false, updatable = false, length = 256)
    @GeneratorType(type = PrincipalUserGenerator.class, when = GenerationTime.INSERT)
    private String createdBy;

    @PastOrPresent
    @UpdateTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Column(name = "modified", insertable = false)
    private Instant lastModifiedDate;

    @LastModifiedBy
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "modified_by", insertable = false, length = 256)
    @GeneratorType(type = PrincipalUserGenerator.class, when = GenerationTime.ALWAYS)
    private String lastModifiedBy;

    /**
     * Returns the user who modified the entity lastly.
     *
     * @return the lastModifiedBy
     */
    @NonNull
    @Override
    public Optional<Instant> getLastModifiedDate() {
        return Optional.ofNullable(this.lastModifiedDate);
    }

    /**
     * Returns the user who modified the entity lastly.
     *
     * @return the lastModifiedBy
     */
    @NonNull
    @Override
    public Optional<String> getLastModifiedBy() {
        return Optional.ofNullable(this.lastModifiedBy);
    }

    /**
     * Returns the creation date of the entity.
     *
     * @return the createdDate
     */
    @NonNull
    @Override
    public Optional<Instant> getCreatedDate() {
        return Optional.ofNullable(this.createdDate);
    }

    /**
     * Returns the user who created this entity.
     *
     * @return the createdBy
     */
    @NonNull
    @Override
    public Optional<String> getCreatedBy() {
        return Optional.ofNullable(this.createdBy);
    }

    /**
     * {@link String} {@link ValueGenerator} implementation
     */
    private static class PrincipalUserGenerator implements ValueGenerator<String> {
        /**
         * @see ValueGenerator
         */
        @Override
        public String generateValue(final Session session,
                                    final Object owner) {
            return getSystemThreadGroup().getName();
        }
    }
}
