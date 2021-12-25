package day14

import java.io.File

data class Rule(
    val pair: String,
    val value: Char
)

fun getFreqMap(chars: String): Map<Char, Long> {
    val freq: MutableMap<Char, Long> = HashMap()
    for (c in chars) {
        freq.putIfAbsent(c, 0L)
        freq[c] = freq[c]!! + 1L
    }
    return freq
}

fun main(args: Array<String>) {
    val lines = File("files/day14.txt").readLines()
    var templ = lines[0]
    val rules = lines.filter { it != "" && it != templ }.map { it.split(" -> ").let { s -> Rule(s[0], s[1][0]) } }

    val allLettersCapital = mutableListOf<Char>()
    for (i in 65..90) {
        allLettersCapital.add(i.toChar())
    }

    val rulesByPair = rules.groupBy { it.pair }
    val charFreqMap = getFreqMap(templ).toMutableMap()
    var ruleFreqMap = mutableMapOf<String, Long>()

    for (l1 in allLettersCapital) {
        for (l2 in allLettersCapital) {
            ruleFreqMap.put("$l1$l2", 0)
        }
    }
    for (t in 0 until templ.length - 1) {
        val k = "${templ[t]}${templ[t + 1]}"
        ruleFreqMap[k] = ruleFreqMap[k]!! + 1
    }

    val steps = 40
    for (i in 0 until steps) {
        val newRuleFreqMap = HashMap(ruleFreqMap)

        ruleFreqMap
            .filter { it.value > 0 }
            .forEach { mapEntry ->
                val rule = mapEntry.key
                val byHowMany = mapEntry.value
                if (rulesByPair.containsKey(rule)) {
                    val ruleMapping = rulesByPair[rule]!!.first().value

                    newRuleFreqMap[rule] = newRuleFreqMap[rule]!! - byHowMany
                    newRuleFreqMap["${rule[0]}${ruleMapping}"] = newRuleFreqMap["${rule[0]}${ruleMapping}"]!! + byHowMany
                    newRuleFreqMap["${ruleMapping}${rule[1]}"] = newRuleFreqMap["${ruleMapping}${rule[1]}"]!! + byHowMany
                    charFreqMap.putIfAbsent(ruleMapping, 0)
                    charFreqMap[ruleMapping] = charFreqMap[ruleMapping]!! + byHowMany
                }
            }

        ruleFreqMap = newRuleFreqMap
    }

    val l = charFreqMap.toList().sortedBy { it.second }
    println("${l.last().first} ${l.first().first}")
    println(l.last().second - l.first().second)

    println("Hello World on day14!")
}
