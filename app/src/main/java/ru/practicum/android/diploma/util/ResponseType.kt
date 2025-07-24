package ru.practicum.android.diploma.util

enum class ResponseType(val code: Int) {
    NO_CONNECTION(-1),
    SEARCH_SUCCESS(2),
    SERVER_ERROR(5),
    REQUEST_FAILED(1),
    ID_OTHER_REGIONS(1001),
    UNKNOWN(-999)
}

fun Int.toResponseType(): ResponseType =
    ResponseType.values().find { it.code == this } ?: ResponseType.UNKNOWN
