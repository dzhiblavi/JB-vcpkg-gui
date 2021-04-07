package util

import java.io.File
import java.util.concurrent.TimeUnit

fun String.runCommand(
    workingDir: File = File("."),
    timeoutAmount: Long = 60,
    timeoutUnit: TimeUnit = TimeUnit.MINUTES
): Pair<Int, String> {
    val builder = ProcessBuilder("\\s".toRegex().split(this))
        .redirectErrorStream(true)
        .directory(workingDir)

    val proc = builder.start()

    val stdout = proc.inputStream.bufferedReader().readText()

    if (proc.waitFor(timeoutAmount, timeoutUnit)) {
        println("Finished: '$this', ${proc.exitValue()}")
        return Pair(proc.exitValue(), stdout)
    } else {
        throw Exception("Failed to execute in limited time: '$this'")
    }
}
