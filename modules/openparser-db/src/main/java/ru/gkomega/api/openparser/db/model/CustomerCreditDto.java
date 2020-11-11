package ru.gkomega.api.openparser.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreditDto implements Serializable {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 8982801832846359829L;

    private int id;
    private String name;
    private BigDecimal credit;
}
