import java.nio.channels.FileChannel
import java.nio.file.Paths
import kotlin.system.measureNanoTime

fun main() {
    // Task: given array of ints, need to find all records which contains any of these values
    val path = Paths.get("out")
    for (i in 0..10) {
        val time = measureNanoTime {
            FileWrapper(path).use {
                readRecords(FileWrapper(path))
            }
        }
        val ms = time / 1_000_000
        val sizeMb = FileChannel.open(path).size().toFloat() / (1024 * 1024)
        val sec = ms / 1000.toFloat()
        val speed = sizeMb / sec
        println("ms: $ms, speed: $speed Mb/sec, size: $sizeMb Mb")
    }
}

private fun readRecords(file: FileWrapper) {
    var recordIdx = 0
    val records = mutableListOf<Int>()
    val needle = intArrayOf(12, 70, 120)
    try {
        loop@
        while (true) {
            val recordSize = file.readInt()
            if (recordContains(needle, file, recordSize)) {
                records.add(recordIdx)
            }
            recordIdx++
        }
    } catch (e: FileEof) {
//        println(needle.contentToString())
    }
}

fun recordContains(arr: IntArray, file: FileWrapper, recordSize: Int): Boolean {
    if (recordSize == 0) return false
    var arrIdx = 0
    var recordIdx = 0
    var currRecordValue = file.readInt()
    val arrSize = arr.size
    var contains = false
    while (arrIdx < arrSize && recordIdx < recordSize) {
        val currArrValue = arr[arrIdx]
        if (currArrValue < currRecordValue) {
            arrIdx++
        } else if (currArrValue > currRecordValue) {
            recordIdx++
            currRecordValue = file.readInt()
        } else {
            contains = true
            break
        }
    }
    while (recordIdx < recordSize) {
        file.readInt()
        recordIdx++
    }
    return contains
}