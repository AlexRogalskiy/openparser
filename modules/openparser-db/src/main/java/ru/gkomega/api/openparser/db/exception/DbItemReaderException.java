package ru.gkomega.api.openparser.db.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.batch.item.ItemReaderException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.gkomega.api.openparser.commons.helper.MessageSourceHelper;

import static ru.gkomega.api.openparser.db.enumeration.DbErrorTemplateType.DB_READER_OPERATION_ERROR;

/**
 * Database {@link ItemReaderException} implementation
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DbItemReaderException extends ItemReaderException {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 7142644440041132078L;

    /**
     * {@link DbItemReaderException} constructor with initial input message
     *
     * @param message - initial input message {@link String}
     */
    public DbItemReaderException(final String message) {
        super(message);
    }

    /**
     * {@link DbItemReaderException} constructor with initial input message and {@link Throwable}
     *
     * @param message - initial input message {@link String}
     * @param cause   - initial input cause target {@link Throwable}
     */
    public DbItemReaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns {@link DbItemReaderException} by input parameters
     *
     * @param message - initial input description {@link String}
     * @return {@link DbItemReaderException}
     */
    @NonNull
    public static DbItemReaderException throwError(final String message) {
        throw new DbItemReaderException(message);
    }

    /**
     * Returns {@link DbItemReaderException} by input parameters
     *
     * @param message - initial input message {@link String}
     * @return {@link DbItemReaderException}
     */
    @NonNull
    public static DbItemReaderException throwInvalidOperation(final String message) {
        throw new DbItemReaderException(message);
    }

    /**
     * Returns {@link DbItemReaderException} by input parameters
     *
     * @param args - initial input description {@link Object} arguments
     * @return {@link DbItemReaderException}
     */
    @NonNull
    public static DbItemReaderException throwInvalidDbReader(@Nullable final Object... args) {
        throw throwInvalidDbReaderWith(DB_READER_OPERATION_ERROR.getCode(), args);
    }

    /**
     * Returns {@link DbItemReaderException} by input parameters
     *
     * @param messageId - initial input message {@link String} identifier
     * @param args      - initial input description {@link Object} arguments
     * @return {@link DbItemReaderException}
     */
    @NonNull
    public static DbItemReaderException throwInvalidDbReaderWith(final String messageId,
                                                                 @Nullable final Object... args) {
        throw throwError(MessageSourceHelper.getMessage(messageId, args));
    }
}
