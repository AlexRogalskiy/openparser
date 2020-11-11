package ru.gkomega.api.openparser.web.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.gkomega.api.openparser.web.configuration.WebMvcConfiguration;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@EnableWebMvc
@TypeQualifierDefault(ElementType.TYPE)
@Import(WebMvcConfiguration.class)
public @interface EnableWeb {
}
