package ru.gkomega.api.openparser.db.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerCreditDtoRowMapper implements RowMapper<CustomerCreditDto> {
    /**
     * Default field mappings
     */
    public static final String ID_COLUMN = "id";
    public static final String NAME_COLUMN = "name";
    public static final String CREDIT_COLUMN = "credit";

    public CustomerCreditDto mapRow(final ResultSet resultSet, final int rowNum) throws SQLException {
        final CustomerCreditDto customerCredit = new CustomerCreditDto();
        customerCredit.setId(resultSet.getInt(ID_COLUMN));
        customerCredit.setName(resultSet.getString(NAME_COLUMN));
        customerCredit.setCredit(resultSet.getBigDecimal(CREDIT_COLUMN));
        return customerCredit;
    }
}
