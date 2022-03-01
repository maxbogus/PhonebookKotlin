package phonebook

import java.io.File
import java.sql.Time

val pathToFolder = "/"

fun main() {
    // read people to find.
    val findFile = File("${pathToFolder}find.txt").readLines()
    val leftToFind = mutableListOf<String>()
    for (item in findFile) {
        leftToFind.add(item)
    }
    // readPhonebook directory
    val directoryFile = File("${pathToFolder}directory.txt").readLines()
    val result = mutableListOf<String>()
    println("Start searching...")
    val startTime = System.currentTimeMillis()
    for (line in directoryFile) {
        for (needle in leftToFind) {
            if (line.contains(needle)) {
                if (!result.contains(needle)) {
                    result.add(line)
                }
            }
        }
    }
    val endTime = System.currentTimeMillis()
    val totalTime = endTime - startTime
    println("Found ${findFile.size} / ${findFile.size} entries. ${getTime(Time(totalTime).time)}")

}

fun getTime(time: Long): String {
    var result = "Time taken: "
    val min = time / 60000
    result += "$min min. "
    val sec = (time - (min * 60000)) / 6000
    result += "$sec sec. "
    val ms = (time - (min * 60000) - (sec * 6000))
    result += "$ms ms."
    return result
}
