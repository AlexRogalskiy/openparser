package ru.gkomega.api.openparser.db.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.batch.item.ItemWriterException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.gkomega.api.openparser.commons.helper.MessageSourceHelper;

import static ru.gkomega.api.openparser.db.enumeration.DbErrorTemplateType.DB_WRITER_OPERATION_ERROR;

/**
 * Database {@link ItemWriterException} implementation
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DbItemWriterException extends ItemWriterException {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 7704189620663563806L;

    /**
     * {@link DbItemWriterException} constructor with initial input message
     *
     * @param message - initial input message {@link String}
     */
    public DbItemWriterException(final String message) {
        super(message);
    }

    /**
     * {@link DbItemWriterException} constructor with initial input message and {@link Throwable}
     *
     * @param message - initial input message {@link String}
     * @param cause   - initial input cause target {@link Throwable}
     */
    public DbItemWriterException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns {@link DbItemWriterException} by input parameters
     *
     * @param message - initial input description {@link String}
     * @return {@link DbItemWriterException}
     */
    @NonNull
    public static DbItemWriterException throwError(final String message) {
        throw new DbItemWriterException(message);
    }

    /**
     * Returns {@link DbItemWriterException} by input parameters
     *
     * @param message - initial input message {@link String}
     * @return {@link DbItemWriterException}
     */
    @NonNull
    public static DbItemWriterException throwInvalidOperation(final String message) {
        throw new DbItemWriterException(message);
    }

    /**
     * Returns {@link DbItemWriterException} by input parameters
     *
     * @param args - initial input description {@link Object} arguments
     * @return {@link DbItemWriterException}
     */
    @NonNull
    public static DbItemWriterException throwInvalidDbWriter(@Nullable final Object... args) {
        throw throwInvalidDbWriterWith(DB_WRITER_OPERATION_ERROR.getCode(), args);
    }

    /**
     * Returns {@link DbItemWriterException} by input parameters
     *
     * @param messageId - initial input message {@link String} identifier
     * @param args      - initial input description {@link Object} arguments
     * @return {@link DbItemWriterException}
     */
    @NonNull
    public static DbItemWriterException throwInvalidDbWriterWith(final String messageId,
                                                                 @Nullable final Object... args) {
        throw throwError(MessageSourceHelper.getMessage(messageId, args));
    }
}
