package com.niaw.launchnea.utils

fun abbreviate(name: String, maxWords: Int = 4): String {
    if (name == "China Aerospace Science and Technology Corporation") {
        return "CASC"
    }

    val bracketContent = Regex("""\((.*?)\)|\[(.*?)]""").find(name)
        ?.groups?.get(1)?.value
        ?: Regex("""\((.*?)\)|\[(.*?)]""").find(name)?.groups?.get(2)?.value

    if (!bracketContent.isNullOrBlank()) {
        return bracketContent
    }

    val stopWords = setOf(
        "and", "or", "of", "the", "for", "in", "on", "at", "to", "a", "an"
    )

    val words = name.trim()
        .split("\\s+".toRegex())
        .filter { it.lowercase() !in stopWords }

    return if (words.size >= maxWords) {
        words.joinToString("") { it.firstOrNull()?.uppercaseChar().toString() }
    } else {
        name
    }
}