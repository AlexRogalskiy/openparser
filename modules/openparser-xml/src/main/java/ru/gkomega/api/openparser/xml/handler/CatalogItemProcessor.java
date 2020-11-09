package ru.gkomega.api.openparser.xml.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.gkomega.api.openparser.xml.model.dto.CatalogItemDto;
import ru.gkomega.api.openparser.xml.model.entity.CatalogItemEntity;

import java.util.Objects;

import static org.springframework.batch.core.ExitStatus.COMPLETED;
import static org.springframework.batch.core.ExitStatus.FAILED;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogItemProcessor extends ItemListenerSupport<CatalogItemDto, CatalogItemEntity> implements ItemProcessor<CatalogItemDto, CatalogItemEntity> {

    private final CatalogItemValidator itemValidator;
    private final ModelMapper modelMapper;

    private StepExecution stepExecution;

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void beforeProcess(@NonNull final CatalogItemDto item) {
        super.beforeProcess(item);
        if (Objects.isNull(this.itemValidator.process(item))) {
            this.stepExecution.setExitStatus(FAILED);
        }
    }

    @Override
    public void afterProcess(@NonNull final CatalogItemDto item,
                             final CatalogItemEntity result) {
        super.afterProcess(item, result);
        this.stepExecution.setExitStatus(COMPLETED);
    }

    @Override
    public CatalogItemEntity process(@NonNull final CatalogItemDto item) {
        log.info(">>> Transforming input catalog item ({})", item);
        return this.modelMapper.map(item, CatalogItemEntity.class);
    }
}
