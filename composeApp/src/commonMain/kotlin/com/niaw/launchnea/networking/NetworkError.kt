package com.niaw.launchnea.networking

enum class NetworkError {
    REQUEST_TIMEOUT,
    CONNECTION_TIMEOUT,
    SOCKET_TIMEOUT,
    CONFLICT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    NO_DATA,
    SERVER_ERROR,
    SERIALIZATION,
    UNKNOWN;
}
