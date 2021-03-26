package converter

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.system.exitProcess

fun main() {
    userInput()
}

private fun userInput() {
    println("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
    val userChoise = readLine()!!
    if (userChoise == "/exit") exitProcess(1)
    getUSerNumber(userChoise)
}

private fun getUSerNumber(userChoise: String) {
    var resault = ""
    val massUserChois = userChoise.split(" ")
    println(
        "Enter number in base ${massUserChois[0]}" +
                " to convert to base ${massUserChois[1]} (To go back type /back)"
    )
    val numberFromConvert = readLine()!!
    if (numberFromConvert == "/back") userInput()
    else {
        if(numberFromConvert.contains(".")) resault = convertBigDecimal(numberFromConvert, massUserChois)
        else resault = convertDecimalPartNumber(numberFromConvert, massUserChois)
        println("Conversion result: $resault")
        getUSerNumber(massUserChois.joinToString(" ", "", ""))
    }
}

private fun convertBigDecimal(number: String, massUserChois: List<String>): String {
    val wholePart = number.substringBefore(".")
    val fractalPart = number.substringAfter(".")
    var fractalPartConver = ""
    var wholePartConvert = ""
    if (massUserChois[0] == "10") {
        fractalPartConver = convertFractalBaseToDecimal(fractalPart, massUserChois[1].toInt())
    }
    else if (massUserChois[1] == "10") {
        fractalPartConver = convertFractalDecimaltoBase(fractalPart, massUserChois[0].toInt())
    }
    else {
        val preConvert =  convertFractalDecimaltoBase(fractalPart, massUserChois[0].toInt()).substringAfter(".")
        fractalPartConver = convertFractalBaseToDecimal(preConvert, massUserChois[1].toInt())
    }
    wholePartConvert = convertDecimalPartNumber(wholePart, massUserChois)

    return  (wholePartConvert +"."+ fractalPartConver.substringAfter("."))
}

private fun convertDecimalPartNumber(number: String, massUserChois: List<String>): String {
    val decimalNumber = BigInteger(number, massUserChois[0].toInt())
    val output: String = decimalNumber.toString(massUserChois[1].toInt())
    return output
}

private fun convertFractalDecimaltoBase(fractalNumber: String, base: Int): String {
    var output = ""
    val digits = "0123456789"
    var fractalNumberStr = fractalNumber
    for (i in 0..fractalNumberStr.length - 1) {
        var currenSumbol: String = fractalNumberStr.get(i).toString()
        if(!digits.contains(currenSumbol)){
            currenSumbol = getNumberForSumbol(currenSumbol)
        }
        if (output.isEmpty()) {
            val bidDecimalPass = currenSumbol.toBigDecimal()
                .multiply((Math.pow(base.toDouble(), -i.toDouble() - 1.toDouble())).toBigDecimal())
            output = bidDecimalPass.toString()

        } else {
            val bidDecimalPass = output.toBigDecimal()
                .plus(
                    currenSumbol.toBigDecimal()
                        .multiply((Math.pow(base.toDouble(), -i.toDouble() - 1.toDouble())).toBigDecimal())
                )
            output = bidDecimalPass.toString()
        }
    }
    return output
}

private fun convertFractalBaseToDecimal(fractalNumber: String, base: Int): String {
    var output = ""
    var fractalNumberStr = "0." + fractalNumber
    for (i in 1..5) {
        fractalNumberStr = (fractalNumberStr.toBigDecimal().multiply(base.toBigDecimal())).toString()
        if (fractalNumberStr.toBigDecimal() > BigDecimal.ONE) {
            if(fractalNumberStr.toBigDecimal() >= BigDecimal.TEN){
                fractalNumberStr = getSumbolForNumber(fractalNumberStr)
                output += fractalNumberStr[0]
                fractalNumberStr = fractalNumberStr.substring(1)
                fractalNumberStr = "0"+ fractalNumberStr
            }
            else{
                output += fractalNumberStr[0]
                fractalNumberStr = fractalNumberStr.substring(1)
                fractalNumberStr = "0" + fractalNumberStr
            }

        }
        else{
            output += fractalNumberStr[0]
        }
    }
    output = output.substringAfter(".")
    return output.substring(0,5)
}

fun getSumbolForNumber(fractalNumberStr: String) : String {
    val firctTwoDigits = (fractalNumberStr.get(0).toString() + fractalNumberStr.get(1).toString()).toInt()
    var output = ""
    val albhabet = "abcdefghijklmnopqrstuvwxyz"
    for (i in 0..albhabet.length-1){
        if(firctTwoDigits == i+10){
            output = albhabet.get(i).toString()
            break
        }
    }
    return output
}

fun getNumberForSumbol(sumbol : String) : String {
    var output = ""
    val albhabet = "abcdefghijklmnopqrstuvwxyz"
    for (i in 0..albhabet.length-1){
        if(sumbol == albhabet.get(i).toString()){
            output = (i+10).toString()
            break
        }
    }
    return output
}

