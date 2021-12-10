package day10

import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val lines = File("files/day10.txt").readLines().map {it.toCharArray()}

    var score = 0
    var scores = mutableListOf<Long>()

    val leftBrackets = setOf('<', '[', '{', '(')

    for (line in lines) {
        var s = ArrayDeque<Char>()
        var wasBad = false;

        for (bracket in line) {
            if (bracket in leftBrackets) {
                s.push(bracket)
            }
            else {
                if (s.peek() == '<' && bracket == '>') {
                    s.pop()
                }
                else if (s.peek() == '[' && bracket == ']') {
                    s.pop()
                }
                else if (s.peek() == '{' && bracket == '}') {
                    s.pop()
                }
                else if (s.peek() == '(' && bracket == ')') {
                    s.pop()
                }
                else {
                    if (bracket == ')') {
                        score += 3
                    }
                    else if (bracket == '}') {
                        score += 1197
                    }
                    else if (bracket == ']') {
                        score += 57
                    }
                    else if (bracket == '>') {
                        score += 25137
                    }

                    wasBad = true;
                    break;
                }
            }
        }

        var localScore: Long = 0
        while (!s.isEmpty() && !wasBad) {
            localScore *= 5
            if (s.peek() == '(') {
                localScore += 1
            }
            else if (s.peek() == '[') {
                localScore += 2
            }
            else if (s.peek() == '{') {
                localScore += 3
            }
            else if (s.peek() == '<') {
                localScore += 4
            }
            else {
                println("error")
            }

            s.pop()
        }

        if (localScore != 0L) {
            scores.add(localScore)
        }

    }

    println(score)
    println(scores.sorted()[scores.size / 2])
}
