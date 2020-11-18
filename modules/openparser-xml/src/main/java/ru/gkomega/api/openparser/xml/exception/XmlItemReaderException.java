package ru.gkomega.api.openparser.xml.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.batch.item.ItemReaderException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.gkomega.api.openparser.commons.helper.MessageSourceHelper;

import static ru.gkomega.api.openparser.xml.enumeration.XmlErrorTemplateType.XML_READER_OPERATION_ERROR;

/**
 * Xml {@link ItemReaderException} implementation
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class XmlItemReaderException extends ItemReaderException {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = -5781440721154646853L;

    /**
     * {@link XmlItemReaderException} constructor with initial input message
     *
     * @param message - initial input message {@link String}
     */
    public XmlItemReaderException(final String message) {
        super(message);
    }

    /**
     * {@link XmlItemReaderException} constructor with initial input message and {@link Throwable}
     *
     * @param message - initial input message {@link String}
     * @param cause   - initial input cause target {@link Throwable}
     */
    public XmlItemReaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns {@link XmlItemReaderException} by input parameters
     *
     * @param message - initial input description {@link String}
     * @return {@link XmlItemReaderException}
     */
    @NonNull
    public static XmlItemReaderException throwError(final String message) {
        throw new XmlItemReaderException(message);
    }

    /**
     * Returns {@link XmlItemReaderException} by input parameters
     *
     * @param args - initial input description {@link Object} arguments
     * @return {@link XmlItemReaderException}
     */
    @NonNull
    public static XmlItemReaderException throwInvalidXmlReader(@Nullable final Object... args) {
        throw throwInvalidXmlReaderWith(XML_READER_OPERATION_ERROR.getCode(), args);
    }

    /**
     * Returns {@link XmlItemReaderException} by input parameters
     *
     * @param messageId - initial input message {@link String} identifier
     * @param args      - initial input description {@link Object} arguments
     * @return {@link XmlItemReaderException}
     */
    @NonNull
    public static XmlItemReaderException throwInvalidXmlReaderWith(final String messageId,
                                                                   @Nullable final Object... args) {
        throw throwError(MessageSourceHelper.getMessage(messageId, args));
    }
}
