import java.io.DataOutput
import java.io.DataOutputStream
import java.io.File
import kotlin.random.Random

fun main() {
    DataOutputStream(File("out").outputStream()).use {
        val r = Random(0)
        generateRecords(80 * 1024 * 1024, it, r)
    }
}

fun generateRecords(lowerSizeLimit: Int, out: DataOutput, r: Random) {
    var size = 0
    while (size < lowerSizeLimit) {
        size += genRecord(out, r)
        println("Gen $size")
    }
}

// TODO actually order also should be written
fun genRecord(out: DataOutput, r: Random) : Int {
    val size = r.nextInt(0, 10000)
    out.writeInt(size)
    val arr = IntArray(size) { r.nextInt() }
    arr.sort()
    for (i in arr) {
        out.writeInt(i)
    }
    return size
}