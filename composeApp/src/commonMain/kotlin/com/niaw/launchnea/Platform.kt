package com.niaw.launchnea

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform