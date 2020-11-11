package ru.gkomega.api.openparser.batch.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class PrimaryKeyRangePartitioner implements Partitioner {

    private final JdbcTemplate jdbcTemplate;
    private final String query;

    @NonNull
    @Override
    public Map<String, ExecutionContext> partition(final int gridSize) {
        final List<Map<String, Object>> pkRanges = Optional.of(this.jdbcTemplate.queryForList(this.query, gridSize, gridSize))
            .filter(List::isEmpty)
            .orElseGet(Collections::emptyList);

        log.info("pkRanges = {}", pkRanges);

        /*
         * Create the minValue and maxValue according to PK lists.
         * First partition step will use only maxValue whereas, the last one will use only minValue.
         * For example,
         * partition0={rn=20, maxId=and id <= 2080669},
         * partition1={rn=40, minId=and id > 2080669, maxId=and id <= 2080689},
         * partition2={rn=60, minId=and id > 2080689, maxId=and id <= 2080709},
         * partition3={rn=80, minId=and id > 2080709, maxId=and id <= 2080729},
         * partition4={rn=100, minId=and id > 2080729}
         */
        final Map<String, ExecutionContext> partitionedStepExecutionContexts = new HashMap<>();
        int size = pkRanges.size();
        int start = 0;

        while (start < size) {
            final ExecutionContext executionContext = new ExecutionContext();
            partitionedStepExecutionContexts.put("partition" + start, executionContext);

            if (start > 0) {
                executionContext.put("minId", "and id > " + pkRanges.get(start - 1).get("id"));
                executionContext.put("rn", pkRanges.get(start).get("rn"));
            }
            if (start + 1 < size) {
                executionContext.put("maxId", "and id <= " + pkRanges.get(start).get("id"));
                executionContext.put("rn", pkRanges.get(start).get("rn"));
            }
            start++;
        }

        log.info("* partitioning result : {}", partitionedStepExecutionContexts);
        return partitionedStepExecutionContexts;
    }
}
