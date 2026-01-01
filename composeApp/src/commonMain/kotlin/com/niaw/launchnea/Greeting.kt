package com.niaw.launchnea

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.ZonedDateTime.of

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}