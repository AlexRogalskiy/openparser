package ru.gkomega.api.openparser.file.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.batch.item.ItemReaderException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.gkomega.api.openparser.commons.helper.MessageSourceHelper;

import static ru.gkomega.api.openparser.file.enumeration.FileErrorTemplateType.FILE_READER_OPERATION_ERROR;

/**
 * File {@link ItemReaderException} implementation
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FileItemReaderException extends ItemReaderException {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 6211315950127447978L;

    /**
     * {@link FileItemReaderException} constructor with initial input message
     *
     * @param message - initial input message {@link String}
     */
    public FileItemReaderException(final String message) {
        super(message);
    }

    /**
     * {@link FileItemReaderException} constructor with initial input message and {@link Throwable}
     *
     * @param message - initial input message {@link String}
     * @param cause   - initial input cause target {@link Throwable}
     */
    public FileItemReaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns {@link FileItemReaderException} by input parameters
     *
     * @param message - initial input description {@link String}
     * @return {@link FileItemReaderException}
     */
    @NonNull
    public static FileItemReaderException throwError(final String message) {
        throw new FileItemReaderException(message);
    }

    /**
     * Returns {@link FileItemReaderException} by input parameters
     *
     * @param args - initial input description {@link Object} arguments
     * @return {@link FileItemReaderException}
     */
    @NonNull
    public static FileItemReaderException throwInvalidFileReader(@Nullable final Object... args) {
        throw throwInvalidFileReaderWith(FILE_READER_OPERATION_ERROR.getCode(), args);
    }

    /**
     * Returns {@link FileItemReaderException} by input parameters
     *
     * @param messageId - initial input message {@link String} identifier
     * @param args      - initial input description {@link Object} arguments
     * @return {@link FileItemReaderException}
     */
    @NonNull
    public static FileItemReaderException throwInvalidFileReaderWith(final String messageId,
                                                                     @Nullable final Object... args) {
        throw throwError(MessageSourceHelper.getMessage(messageId, args));
    }
}
