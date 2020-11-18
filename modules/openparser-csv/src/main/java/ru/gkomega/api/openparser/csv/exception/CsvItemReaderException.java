package ru.gkomega.api.openparser.csv.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.batch.item.ItemReaderException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.gkomega.api.openparser.commons.helper.MessageSourceHelper;

import static ru.gkomega.api.openparser.csv.enumeration.CsvErrorTemplateType.CSV_READER_OPERATION_ERROR;

/**
 * Csv {@link ItemReaderException} implementation
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CsvItemReaderException extends ItemReaderException {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 700988848278656159L;

    /**
     * {@link CsvItemReaderException} constructor with initial input message
     *
     * @param message - initial input message {@link String}
     */
    public CsvItemReaderException(final String message) {
        super(message);
    }

    /**
     * {@link CsvItemReaderException} constructor with initial input message and {@link Throwable}
     *
     * @param message - initial input message {@link String}
     * @param cause   - initial input cause target {@link Throwable}
     */
    public CsvItemReaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns {@link CsvItemReaderException} by input parameters
     *
     * @param message - initial input description {@link String}
     * @return {@link CsvItemReaderException}
     */
    @NonNull
    public static CsvItemReaderException throwError(final String message) {
        throw new CsvItemReaderException(message);
    }

    /**
     * Returns {@link CsvItemReaderException} by input parameters
     *
     * @param message - initial input message {@link String}
     * @return {@link CsvItemReaderException}
     */
    @NonNull
    public static CsvItemReaderException throwInvalidOperation(final String message) {
        throw new CsvItemReaderException(message);
    }

    /**
     * Returns {@link CsvItemReaderException} by input parameters
     *
     * @param args - initial input description {@link Object} arguments
     * @return {@link CsvItemReaderException}
     */
    @NonNull
    public static CsvItemReaderException throwInvalidCsvReader(@Nullable final Object... args) {
        throw throwInvalidCsvReaderWith(CSV_READER_OPERATION_ERROR.getCode(), args);
    }

    /**
     * Returns {@link CsvItemReaderException} by input parameters
     *
     * @param messageId - initial input message {@link String} identifier
     * @param args      - initial input description {@link Object} arguments
     * @return {@link CsvItemReaderException}
     */
    @NonNull
    public static CsvItemReaderException throwInvalidCsvReaderWith(final String messageId,
                                                                   @Nullable final Object... args) {
        throw throwError(MessageSourceHelper.getMessage(messageId, args));
    }
}
