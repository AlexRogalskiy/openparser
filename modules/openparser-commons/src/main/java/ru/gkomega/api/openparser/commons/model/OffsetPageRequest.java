package ru.gkomega.api.openparser.commons.model;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

public class OffsetPageRequest implements Pageable {

    private final long offset;
    private final int limit;
    private final Sort sort;

    public OffsetPageRequest(long offset, int limit) {
        this(offset, limit, null);
    }

    public OffsetPageRequest(long offset, int limit, Sort sort) {
        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return this.limit;
    }

    @Override
    public long getOffset() {
        return this.offset;
    }

    @NonNull
    @Override
    public Sort getSort() {
        return this.sort;
    }

    @NonNull
    @Override
    public Pageable next() {
        return new OffsetPageRequest(
            this.getOffset() + this.getPageSize(),
            this.getPageSize(),
            this.getSort()
        );
    }

    @NonNull
    @Override
    public Pageable previousOrFirst() {
        return new OffsetPageRequest(
            Math.max(0, this.getOffset() - this.getPageSize()),
            this.getPageSize(),
            this.getSort()
        );
    }

    @NonNull
    @Override
    public Pageable first() {
        return new OffsetPageRequest(0, this.getPageSize(), this.getSort());
    }

    @Override
    public boolean hasPrevious() {
        return this.getOffset() > 0;
    }
}
