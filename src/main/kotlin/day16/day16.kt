package day16

import java.io.File
import java.math.BigInteger




fun hexToBinary(hex: String): String =
    when (hex) {
        "0" -> "0000"
        "1" -> "0001"
        "2" -> "0010"
        "3" -> "0011"
        "4" -> "0100"
        "5" -> "0101"
        "6" -> "0110"
        "7" -> "0111"
        "8" -> "1000"
        "9" -> "1001"
        "A" -> "1010"
        "B" -> "1011"
        "C" -> "1100"
        "D" -> "1101"
        "E" -> "1110"
        "F" -> "1111"
        else -> "".also { println("asdasdsad") }
    }

fun String.binStrToInt(): Int = //BigInteger(this, 2)
    Integer.parseInt(this, 2)

fun String.binStrToBigInt(): BigInteger = BigInteger(this, 2)//.also { println("$this -> $it") }

data class Packet(
    val version: Int,
    val id: Int,
    val subpackets: List<Packet>,
    val literalValue: BigInteger
) {
    val isLiteral = subpackets.isEmpty()

    fun getValue(): BigInteger {
        if (this.isLiteral) {
            return this.literalValue
        } else {
            val values = subpackets.map { it.getValue() }

            return when (this.id) {
                0 -> {
                    values.sumOf { it }
                }
                1 -> {
                    var prod = BigInteger("1")
                    for (pack in values) {
                        prod *= pack
                    }
                    prod
                }
                2 -> {
                    values.minOf { it }
                }
                3 -> {
                    values.maxOf { it }
                }
                5 -> {
                    if (values[0] > values[1]) 1L.toBigInt() else 0L.toBigInt()
                }
                6 -> {
                    if (values[0] < values[1]) 1L.toBigInt() else 0L.toBigInt()
                }
                7 -> {
                    if (values[0] == values[1]) 1L.toBigInt() else 0L.toBigInt()
                }
                else -> {
                    0L.toBigInt()
                }
            }
        }
    }
}

// Returns packet and string residue
fun parse(input: String): Pair<Packet, String> {
    val version = input.substring(0, 3)
    val id = input.substring(3, 6)
    if (id == "100") {
        // literal
        var currBegIdx = 6
        var currLength = 5
        while (input[currBegIdx] != '0') {
            currBegIdx += 5
            currLength += 5
        }
        val value = input.substring(6, 6+currLength).binStrToBigInt()
        val wholeLength = 6 + currLength

        return Packet(version.binStrToInt(), id.binStrToInt(), listOf(), value) to input.substring(wholeLength)
    }
    else {
        val subId = input[6]
        if (subId == '0') {
            // Length
            val length = input.substring(7, 22).binStrToInt()
            val subpackets = mutableListOf<Packet>()
            var currentLength = 0
            val initialResidue = input.substring(22)
            var residue = input.substring(22)
            while (currentLength < length) {
                val res = parse(residue)
                residue = res.second
                currentLength = initialResidue.length - residue.length
                subpackets.add(res.first)
            }

            return Packet(version.binStrToInt(), id.binStrToInt(), subpackets, 0L.toBigInt()) to residue
        }
        else {
            // Quantity
            val qty = input.substring(7, 18).binStrToInt()
            var currentQty = 0
            val subpackets = mutableListOf<Packet>()
            var residue = input.substring(18)
            while (currentQty < qty) {
                val res = parse(residue)
                residue = res.second
                subpackets.add(res.first)
                currentQty++
            }

            return Packet(version.binStrToInt(), id.binStrToInt(), subpackets, 0L.toBigInt()) to residue
        }
    }
}


fun solve(hex: String): BigInteger {
    val inp = hex.toCharArray().map { hexToBinary(it.toString()) }.joinToString("")
    println(inp)
//    println(decodePacket2(inp))
    return parse(inp).first.getValue()
}

fun Long.toBigInt(): BigInteger =
    BigInteger(this.toString())

fun main(args: Array<String>) {
    val lines = File("files/day16.txt").readLines()[0]
    println(solve("C200B40A82") == 3L.toBigInt())
    println(solve("04005AC33890") == 54L.toBigInt())
    println(solve("880086C3E88112") == 7L.toBigInt())
    println(solve("CE00C43D881120") == 9L.toBigInt())
    println(solve("D8005AC2A8F0") == 1L.toBigInt())
    println(solve("F600BC2D8F") == 0L.toBigInt())
    println(solve("9C005AC2F8F0") == 0L.toBigInt())
    println(solve("9C0141080250320F1802104A08") == 1L.toBigInt())

    println(solve(lines))
}
