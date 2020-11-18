package ru.gkomega.api.openparser.solr.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.batch.item.ItemWriterException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.gkomega.api.openparser.commons.helper.MessageSourceHelper;

import static ru.gkomega.api.openparser.solr.enumeration.SolrErrorTemplateType.SOLR_WRITER_OPERATION_ERROR;

/**
 * Solr {@link ItemWriterException} implementation
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SolrItemWriterException extends ItemWriterException {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 660540002431724459L;

    /**
     * {@link SolrItemWriterException} constructor with initial input message
     *
     * @param message - initial input message {@link String}
     */
    public SolrItemWriterException(final String message) {
        super(message);
    }

    /**
     * {@link SolrItemWriterException} constructor with initial input message and {@link Throwable}
     *
     * @param message - initial input message {@link String}
     * @param cause   - initial input cause target {@link Throwable}
     */
    public SolrItemWriterException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns {@link SolrItemWriterException} by input parameters
     *
     * @param message - initial input description {@link String}
     * @return {@link SolrItemWriterException}
     */
    @NonNull
    public static SolrItemWriterException throwError(final String message) {
        throw new SolrItemWriterException(message);
    }

    /**
     * Returns {@link SolrItemWriterException} by input parameters
     *
     * @param args - initial input description {@link Object} arguments
     * @return {@link SolrItemWriterException}
     */
    @NonNull
    public static SolrItemWriterException throwInvalidSolrWriter(@Nullable final Object... args) {
        throw throwInvalidSolrWriterWith(SOLR_WRITER_OPERATION_ERROR.getCode(), args);
    }

    /**
     * Returns {@link SolrItemWriterException} by input parameters
     *
     * @param messageId - initial input message {@link String} identifier
     * @param args      - initial input description {@link Object} arguments
     * @return {@link SolrItemWriterException}
     */
    @NonNull
    public static SolrItemWriterException throwInvalidSolrWriterWith(final String messageId,
                                                                     @Nullable final Object... args) {
        throw throwError(MessageSourceHelper.getMessage(messageId, args));
    }
}
