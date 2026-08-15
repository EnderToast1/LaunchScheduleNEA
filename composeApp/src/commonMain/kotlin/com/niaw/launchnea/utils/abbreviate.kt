package com.niaw.launchnea.utils


fun abbreviate(name: String?, maxWords: Int = 4): String {
    if (name == "China Aerospace Science and Technology Corporation") {
        return "CASC"
    }
    if (name!=null) {

        if(name.contains("/")){
            return name.substring(0,name.indexOf('/')).trim()
        }
        if(name.contains("|")){
            return name.substring(0,name.indexOf('|')).trim()
        }

        // Regex pattern explanation:
        // \((.*?)\)   → matches (anything) and captures the "anything" in group 1
        // |           → OR
        // \[(.*?)]    → matches [anything] and captures the "anything" in group 2
        val regex = Regex("""\((.*?)\) | \[(.*?)]""")

        val bracketContent = regex.find(name)?.let { match ->
            match.groups[1]?.value ?: match.groups[2]?.value          //Find any value in brackets
        }

        if (!bracketContent.isNullOrBlank()) {
            return bracketContent
        }

        val ignoredWords = setOf(
            "and", "or", "for", "in", "on", "at", "to", "an"
        )

        val words = name.trim()
            .split("\\s+".toRegex())
            .filter { it.lowercase() !in ignoredWords }

        return if (words.size >= maxWords) {
            words.joinToString("") { it.firstOrNull()?.uppercaseChar().toString() }
        } else {
            name
        }
    }
    return ""
}