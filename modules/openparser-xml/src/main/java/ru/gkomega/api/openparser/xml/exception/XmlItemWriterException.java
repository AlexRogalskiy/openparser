package ru.gkomega.api.openparser.xml.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.batch.item.ItemWriterException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.gkomega.api.openparser.commons.helper.MessageSourceHelper;

import static ru.gkomega.api.openparser.xml.enumeration.XmlErrorTemplateType.XML_WRITER_OPERATION_ERROR;

/**
 * Xml {@link ItemWriterException} implementation
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class XmlItemWriterException extends ItemWriterException {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 1814843382507399570L;

    /**
     * {@link XmlItemWriterException} constructor with initial input message
     *
     * @param message - initial input message {@link String}
     */
    public XmlItemWriterException(final String message) {
        super(message);
    }

    /**
     * {@link XmlItemWriterException} constructor with initial input message and {@link Throwable}
     *
     * @param message - initial input message {@link String}
     * @param cause   - initial input cause target {@link Throwable}
     */
    public XmlItemWriterException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns {@link XmlItemWriterException} by input parameters
     *
     * @param message - initial input description {@link String}
     * @return {@link XmlItemWriterException}
     */
    @NonNull
    public static XmlItemWriterException throwError(final String message) {
        throw new XmlItemWriterException(message);
    }

    /**
     * Returns {@link XmlItemWriterException} by input parameters
     *
     * @param args - initial input description {@link Object} arguments
     * @return {@link XmlItemWriterException}
     */
    @NonNull
    public static XmlItemWriterException throwInvalidXmlWriter(@Nullable final Object... args) {
        throw throwInvalidXmlWriterWith(XML_WRITER_OPERATION_ERROR.getCode(), args);
    }

    /**
     * Returns {@link XmlItemWriterException} by input parameters
     *
     * @param messageId - initial input message {@link String} identifier
     * @param args      - initial input description {@link Object} arguments
     * @return {@link XmlItemWriterException}
     */
    @NonNull
    public static XmlItemWriterException throwInvalidXmlWriterWith(final String messageId,
                                                                   @Nullable final Object... args) {
        throw throwError(MessageSourceHelper.getMessage(messageId, args));
    }
}
