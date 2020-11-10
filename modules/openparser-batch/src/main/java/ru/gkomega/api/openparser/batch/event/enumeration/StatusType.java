package ru.gkomega.api.openparser.batch.event.enumeration;

public enum StatusType {
    INITIALIZED,
    REGISTERED,
    CANCELLED,
    INVALID;

    /**
     * Returns binary value whether current status is (@code REGISTERED)
     *
     * @param status initial input {@link StatusType} to operate by
     * @return true - if current status is {@code REGISTERED}, false - otherwise
     */
    public static boolean isRegistered(final StatusType status) {
        return REGISTERED.equals(status);
    }
}
