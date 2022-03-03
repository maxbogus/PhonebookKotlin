package phonebook

import java.io.File
import java.sql.Time
import kotlin.math.floor
import kotlin.math.sqrt

const val PATH_TO_FOLDER = "/"

data class SearchData(
    val findFile: List<String>,
    val leftToFind: List<String>,
    var directoryFile: List<String>,
    var stopped: Boolean = false
) {
    fun updateDirectorySearch(newDirSearch: List<String>) {
        directoryFile = newDirSearch
    }
}

enum class SortType {
    BubbleSortJumpSearch,
    QuickSortBinarySearch
}

fun main() {
    val searchData = parseInput()

    val limit = performLinearSearch(searchData)

    performSortAndSearch(searchData, limit, SortType.BubbleSortJumpSearch)
    performSortAndSearch(searchData, limit, SortType.QuickSortBinarySearch)
}

private fun performSortAndSearch(searchData: SearchData, limit: Long, type: SortType) {
    println(
        "Start searching ${
            when (type) {
                SortType.BubbleSortJumpSearch -> "(bubble sort + jump search)"
                SortType.QuickSortBinarySearch -> "(quick sort + binary search)"
            }
        }..."
    )
    val sortStartTime = System.currentTimeMillis()
    val sortedSearchData = when (type) {
        SortType.BubbleSortJumpSearch -> performBubbleSort(searchData, limit)
        SortType.QuickSortBinarySearch -> performQuickSort(searchData, limit)
    }
    val sortEndTime = System.currentTimeMillis()
    val sortTotalTime = sortEndTime - sortStartTime
    val jumpSearchStartTime = System.currentTimeMillis()
    when (type) {
        SortType.BubbleSortJumpSearch -> performJumpSearch(sortedSearchData)
        SortType.QuickSortBinarySearch -> performBinarySearch(sortedSearchData)
    }
    val jumpSearchEndTime = System.currentTimeMillis()
    val jumpSearchTotalTime = jumpSearchEndTime - jumpSearchStartTime
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
    println("Sorting time: ${getTime(Time(sortTotalTime).time)}.${checkStopTime(sortedSearchData)}")
    println("Searching time: ${getTime(Time(jumpSearchTotalTime).time)}")
}

fun performQuickSort(searchData: SearchData, limit: Long): SearchData {
    TODO("Not yet implemented")
}

fun performBinarySearch(sortedSearchData: SearchData) {
    TODO("Not yet implemented")
}

fun checkStopTime(sortedSearchData: SearchData): String {
    return if (sortedSearchData.stopped) " - STOPPED, moved to linear search" else ""
}

fun performBubbleSort(searchData: SearchData, limit: Long): SearchData {
    val startTimer = System.currentTimeMillis()
    val updateDirResults = searchData.directoryFile.toMutableList()
    var allItemsSorted = false
    do {
        var check = false
        for (index in 0..searchData.directoryFile.size - 2) {
            val reachedLimit = System.currentTimeMillis() - startTimer > limit * 10
            if (reachedLimit) {
                allItemsSorted = true
                searchData.stopped = true
                break
            }
            val firstItem = updateDirResults[index].split(" ", limit = 2)
            val secondItem = updateDirResults[index + 1].split(" ", limit = 2)

            if (firstItem[1] > secondItem[1]) {
                val temp = updateDirResults[index + 1]
                updateDirResults[index + 1] = updateDirResults[index]
                updateDirResults[index] = temp
                check = true
            }
        }
        if (!check) {
            allItemsSorted = true
        }
    } while (!allItemsSorted)

    searchData.updateDirectorySearch(updateDirResults)
    return searchData
}

fun performJumpSearch(sortedSearchData: SearchData): List<String> {
    val resultList = mutableListOf<String>()
    for (item in sortedSearchData.leftToFind) {
        val index = jumpSearch(sortedSearchData.directoryFile, item)
        if (index != -1) {
            resultList.add(sortedSearchData.directoryFile[index])
        }
    }
    return resultList
}

fun jumpSearch(list: List<String>, value: String): Int {
    val step = floor(sqrt(list.size.toFloat()))
    var curr = 1
    do {
        val item = list[curr].split(" ", limit = 2)
        if (list[curr].contains(value)) {
            return curr
        } else if (item[1] > value) {
            var ind = curr - 1
            do {
                if (list[curr].contains(value)) {
                    return ind
                }
                ind -= 1
            } while (ind > curr - step && ind >= 1)

            return -1
        }
        curr += 1
    } while (curr <= list.size - 1)

    var ind = list.size - 1
    do {
        if (list[ind].contains(value)) {
            return ind
        }
        ind -= 1
    } while (ind > curr - step)

    return -1
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
): Long {
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
    return totalTime
}

fun getTime(time: Long, timeTake: Boolean = false): String {
    var result = if (timeTake) "Time taken: " else ""
    val min = time / 60000
    result += "$min min. "
    val sec = (time - (min * 60000)) / 6000
    result += "$sec sec. "
    val ms = (time - (min * 60000) - (sec * 6000))
    result += "$ms ms."
    return result
}
