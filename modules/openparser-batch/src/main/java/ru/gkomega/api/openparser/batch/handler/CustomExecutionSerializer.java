package ru.gkomega.api.openparser.batch.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Slf4j
public class CustomExecutionSerializer extends Jackson2ExecutionContextStringSerializer {

    @Override
    public void serialize(@NonNull final Map<String, Object> context,
                          @NonNull final OutputStream out) throws IOException {
        log.info(">>> Serializing context: {}", collectionToCommaDelimitedString(context.entrySet()));
        super.serialize(context, out);
    }

    @NonNull
    @Override
    public Map<String, Object> deserialize(@NonNull final InputStream in) throws IOException {
        final Map<String, Object> context = super.deserialize(in);
        log.info(">>> Deserializing context: {}", collectionToCommaDelimitedString(context.entrySet()));
        return context;
    }
}
