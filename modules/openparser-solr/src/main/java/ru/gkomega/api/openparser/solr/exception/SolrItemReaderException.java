package ru.gkomega.api.openparser.solr.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.batch.item.ItemReaderException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.gkomega.api.openparser.commons.helper.MessageSourceHelper;

import static ru.gkomega.api.openparser.solr.enumeration.SolrErrorTemplateType.SOLR_READER_OPERATION_ERROR;

/**
 * Solr {@link ItemReaderException} implementation
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SolrItemReaderException extends ItemReaderException {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = -9016375177574042457L;

    /**
     * {@link SolrItemReaderException} constructor with initial input message
     *
     * @param message - initial input message {@link String}
     */
    public SolrItemReaderException(final String message) {
        super(message);
    }

    /**
     * {@link SolrItemReaderException} constructor with initial input message and {@link Throwable}
     *
     * @param message - initial input message {@link String}
     * @param cause   - initial input cause target {@link Throwable}
     */
    public SolrItemReaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns {@link SolrItemReaderException} by input parameters
     *
     * @param message - initial input description {@link String}
     * @return {@link SolrItemReaderException}
     */
    @NonNull
    public static SolrItemReaderException throwError(final String message) {
        throw new SolrItemReaderException(message);
    }

    /**
     * Returns {@link SolrItemReaderException} by input parameters
     *
     * @param args - initial input description {@link Object} arguments
     * @return {@link SolrItemReaderException}
     */
    @NonNull
    public static SolrItemReaderException throwInvalidSolrReader(@Nullable final Object... args) {
        throw throwInvalidSolrReaderWith(SOLR_READER_OPERATION_ERROR.getCode(), args);
    }

    /**
     * Returns {@link SolrItemReaderException} by input parameters
     *
     * @param messageId - initial input message {@link String} identifier
     * @param args      - initial input description {@link Object} arguments
     * @return {@link SolrItemReaderException}
     */
    @NonNull
    public static SolrItemReaderException throwInvalidSolrReaderWith(final String messageId,
                                                                     @Nullable final Object... args) {
        throw throwError(MessageSourceHelper.getMessage(messageId, args));
    }
}
