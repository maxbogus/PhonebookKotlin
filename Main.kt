package phonebook

import java.io.File
import java.sql.Time

const val PATH_TO_FOLDER = "/"

fun main() {
    val (findFile, leftToFind, directoryFile) = parseInput()
    println("Start searching...")
    val startTime = System.currentTimeMillis()
    performLinearSearch(directoryFile, leftToFind)
    val endTime = System.currentTimeMillis()
    val totalTime = endTime - startTime
    println("Found ${findFile.size} / ${findFile.size} entries. ${getTime(Time(totalTime).time)}")

}

private fun parseInput(): Triple<List<String>, List<String>, List<String>> {
    val findFile = File("${PATH_TO_FOLDER}find.txt").readLines()
    val leftToFind = mutableListOf<String>()
    for (item in findFile) {
        leftToFind.add(item)
    }
    val directoryFile = File("${PATH_TO_FOLDER}directory.txt").readLines()
    return Triple(findFile, leftToFind, directoryFile)
}

private fun performLinearSearch(
    directoryFile: List<String>,
    leftToFind: List<String>
) {
    val result = mutableListOf<String>()
    for (line in directoryFile) {
        for (needle in leftToFind) {
            if (line.contains(needle)) {
                if (!result.contains(needle)) {
                    result.add(line)
                }
            }
        }
    }
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
