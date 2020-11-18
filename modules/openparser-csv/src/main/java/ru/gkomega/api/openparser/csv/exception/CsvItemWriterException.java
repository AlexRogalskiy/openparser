package ru.gkomega.api.openparser.csv.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.batch.item.ItemWriterException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.gkomega.api.openparser.commons.helper.MessageSourceHelper;

import static ru.gkomega.api.openparser.csv.enumeration.CsvErrorTemplateType.CSV_WRITER_OPERATION_ERROR;

/**
 * Csv {@link ItemWriterException} implementation
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CsvItemWriterException extends ItemWriterException {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 5086942605548122217L;

    /**
     * {@link CsvItemWriterException} constructor with initial input message
     *
     * @param message - initial input message {@link String}
     */
    public CsvItemWriterException(final String message) {
        super(message);
    }

    /**
     * {@link CsvItemWriterException} constructor with initial input message and {@link Throwable}
     *
     * @param message - initial input message {@link String}
     * @param cause   - initial input cause target {@link Throwable}
     */
    public CsvItemWriterException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns {@link CsvItemWriterException} by input parameters
     *
     * @param message - initial input description {@link String}
     * @return {@link CsvItemWriterException}
     */
    @NonNull
    public static CsvItemWriterException throwError(final String message) {
        throw new CsvItemWriterException(message);
    }

    /**
     * Returns {@link CsvItemWriterException} by input parameters
     *
     * @param args - initial input description {@link Object} arguments
     * @return {@link CsvItemWriterException}
     */
    @NonNull
    public static CsvItemWriterException throwInvalidCsvWriter(@Nullable final Object... args) {
        throw throwInvalidCsvWriterWith(CSV_WRITER_OPERATION_ERROR.getCode(), args);
    }

    /**
     * Returns {@link CsvItemWriterException} by input parameters
     *
     * @param messageId - initial input message {@link String} identifier
     * @param args      - initial input description {@link Object} arguments
     * @return {@link CsvItemWriterException}
     */
    @NonNull
    public static CsvItemWriterException throwInvalidCsvWriterWith(final String messageId,
                                                                   @Nullable final Object... args) {
        throw throwError(MessageSourceHelper.getMessage(messageId, args));
    }
}
