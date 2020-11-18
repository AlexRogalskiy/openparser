package ru.gkomega.api.openparser.file.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.batch.item.ItemWriterException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.gkomega.api.openparser.commons.helper.MessageSourceHelper;

import static ru.gkomega.api.openparser.file.enumeration.FileErrorTemplateType.FILE_WRITER_OPERATION_ERROR;

/**
 * File {@link ItemWriterException} implementation
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FileItemWriterException extends ItemWriterException {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 7511381630190346954L;

    /**
     * {@link FileItemWriterException} constructor with initial input message
     *
     * @param message - initial input message {@link String}
     */
    public FileItemWriterException(final String message) {
        super(message);
    }

    /**
     * {@link FileItemWriterException} constructor with initial input message and {@link Throwable}
     *
     * @param message - initial input message {@link String}
     * @param cause   - initial input cause target {@link Throwable}
     */
    public FileItemWriterException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns {@link FileItemWriterException} by input parameters
     *
     * @param message - initial input description {@link String}
     * @return {@link FileItemWriterException}
     */
    @NonNull
    public static FileItemWriterException throwError(final String message) {
        throw new FileItemWriterException(message);
    }

    /**
     * Returns {@link FileItemWriterException} by input parameters
     *
     * @param args - initial input description {@link Object} arguments
     * @return {@link FileItemWriterException}
     */
    @NonNull
    public static FileItemWriterException throwInvalidFileWriter(@Nullable final Object... args) {
        throw throwInvalidFileWriterWith(FILE_WRITER_OPERATION_ERROR.getCode(), args);
    }

    /**
     * Returns {@link FileItemWriterException} by input parameters
     *
     * @param messageId - initial input message {@link String} identifier
     * @param args      - initial input description {@link Object} arguments
     * @return {@link FileItemWriterException}
     */
    @NonNull
    public static FileItemWriterException throwInvalidFileWriterWith(final String messageId,
                                                                     @Nullable final Object... args) {
        throw throwError(MessageSourceHelper.getMessage(messageId, args));
    }
}
