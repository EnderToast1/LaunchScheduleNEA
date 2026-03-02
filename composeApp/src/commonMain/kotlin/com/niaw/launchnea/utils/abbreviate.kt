package com.niaw.launchnea.utils

fun abbreviate(name: String, maxWords: Int = 4): String {
    if (name=="China Aerospace Science and Technology Corporation") {//special case as They do not abbreviate normally
        return "CASC"
    }

    val bracketContent = Regex("""\((.*?)\)|\[(.*?)]""").find(name)//Checks if there is any () or []
        ?.groups?.get(1)?.value  // group 1 is the content inside ()
        ?: Regex("""\((.*?)\)|\[(.*?)]""").find(name)?.groups?.get(2)?.value // group 2 is inside []

    if (!bracketContent.isNullOrBlank()) {
        return bracketContent
    }

    val words = name.trim().split("\\s+".toRegex())
    return if (words.size >= maxWords) {
        // Take first letter of each word, join uppercase
        words.joinToString("") { it.firstOrNull()?.uppercaseChar().toString() }
    } else {
        name
    }
}