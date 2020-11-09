package ru.gkomega.api.openparser.xml.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@MappedSuperclass
public abstract class BaseItemEntity<ID extends Serializable> extends AuditEntity<ID> {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = -8687844415736578845L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    @Type(type = "uuid-char")
    private ID id;

    @Override
    public boolean isNew() {
        return Objects.isNull(this.id);
    }
}
