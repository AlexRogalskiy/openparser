package ru.gkomega.api.openparser.xstream.annotation;

import org.springframework.context.annotation.Import;
import ru.gkomega.api.openparser.xstream.configuration.XStreamConfiguration;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
//@TypeQualifierDefault(ElementType.TYPE)
@Import(XStreamConfiguration.class)
public @interface EnableXStream {
}
