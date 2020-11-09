package ru.gkomega.api.openparser.xml.model.domain;

import lombok.Data;
import lombok.Value;
import ru.gkomega.api.openparser.xml.enumeration.StatusType;

import java.io.Serializable;

@Data
@Value(staticConstructor = "of")
public class StepExecutionData implements Serializable {
    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 6479100880398576258L;

    /**
     * Event description
     */
    private String message;
    /**
     * Event status
     */
    private StatusType status;
}
