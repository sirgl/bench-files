import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Path

class FileWrapper(path: Path) : AutoCloseable {
    private val ch = FileChannel.open(path)
    private val buffer: ByteBuffer = ByteBuffer.allocateDirect(4096)


    fun readInt(): Int {
        ensureSize(4)
        return buffer.int
    }

    fun hasInt() {
        // TODO
    }

    private fun ensureSize(size: Int) {
        while (buffer.limit() - buffer.position() < size) {
            buffer.compact()
            if (ch.read(buffer) <= 0) throw FileEof()
        }
    }

    override fun close() {
        ch.close()
    }

}

class FileEof : Exception()