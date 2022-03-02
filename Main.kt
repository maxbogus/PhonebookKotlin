package phonebook

import java.io.File
import java.sql.Time

const val PATH_TO_FOLDER = "/"

data class SearchData(val findFile: List<String>, val leftToFind: List<String>, val directoryFile: List<String>)

fun main() {
    val searchData = parseInput()

    performLinearSearch(searchData)

    println("Start searching (bubble sort + jump search)...")
    val sortStartTime = System.currentTimeMillis()
    val sortedSearchData = performBubbleSort(searchData)
    val sortEndTime = System.currentTimeMillis()
    val sortTotalTime = sortStartTime - sortEndTime
    val jumpSearchStartTime = System.currentTimeMillis()
    performJumpSearch(sortedSearchData)
    val jumpSearchEndTime = System.currentTimeMillis()
    val jumpSearchTotalTime = jumpSearchStartTime - jumpSearchEndTime
    val totalSortSearchTime = sortTotalTime + jumpSearchTotalTime
    println(
        "Found ${sortedSearchData.findFile.size} / ${sortedSearchData.findFile.size} entries. ${
            getTime(
                Time(
                    totalSortSearchTime
                ).time
            )
        }"
    )
    println("Sorting time: ${getTime(Time(sortTotalTime).time)}")
    println("Searching time: ${getTime(Time(jumpSearchTotalTime).time)}")
}

fun performBubbleSort(searchData: SearchData): SearchData {
    return searchData
}

fun performJumpSearch(sortedSearchData: SearchData) {
    TODO()
}

private fun parseInput(): SearchData {
    val findFile = File("${PATH_TO_FOLDER}find.txt").readLines()
    val leftToFind = mutableListOf<String>()
    for (item in findFile) {
        leftToFind.add(item)
    }
    val directoryFile = File("${PATH_TO_FOLDER}directory.txt").readLines()
    return SearchData(findFile, leftToFind, directoryFile)
}

private fun performLinearSearch(
    searchData: SearchData
) {
    println("Start searching (linear search)...")
    val startTime = System.currentTimeMillis()
    val result = mutableListOf<String>()
    for (line in searchData.directoryFile) {
        for (needle in searchData.leftToFind) {
            if (line.contains(needle)) {
                if (!result.contains(needle)) {
                    result.add(line)
                }
            }
        }
    }
    val endTime = System.currentTimeMillis()
    val totalTime = endTime - startTime
    println("Found ${searchData.findFile.size} / ${searchData.findFile.size} entries. ${getTime(Time(totalTime).time)}")
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
