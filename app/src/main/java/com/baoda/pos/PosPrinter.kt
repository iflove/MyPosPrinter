package com.baoda.pos

import android.support.annotation.IntDef
import android.support.annotation.IntRange
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.TimeUnit

class PosPrinter {

    var encoding: String = "GBK"

    private lateinit var client: Socket
    private lateinit var writer: PrintWriter

    @Throws(IOException::class, UnsupportedEncodingException::class)
    fun connect(ip: String, port: Int = 9100, timeout: Long = 1, timeUnit: TimeUnit = TimeUnit.SECONDS) {
        client = Socket()
        client.connect(InetSocketAddress(ip, port), TimeUnit.MILLISECONDS.convert(timeout, timeUnit).toInt())
        writer = PrintWriter(BufferedWriter(OutputStreamWriter(client.getOutputStream(), encoding)))
    }

    @Throws(IOException::class)
    fun disconnect() {
        writer.close()
        client.close()
    }

    private fun write(hexCommands: HexCommands, vararg moreHexValues: Int) {
        hexCommands.hexValues.forEach {
            writer.write(it)
        }
        moreHexValues.forEach {
            writer.write(it)
        }
        writer.flush()
    }

    fun initPosPrinter(): PosPrinter {
        write(HexCommands.INIT_PRINTER)
        return this
    }

    fun selectStandardMode(): PosPrinter {
        write(HexCommands.STANDARD_MODE)
        return this
    }

    fun printText(text: String): PosPrinter {
        writer.write(text)
        writer.flush()
        return this
    }

    fun printLine(line: Int = 1): PosPrinter {
        for (i in 0 until line) {
            writer.write("\n")
            writer.flush()
        }
        return this
    }

    fun printTextLine(text: String, line: Int = 1): PosPrinter {
        printLine(line)
        printText(text)
        return this
    }

    fun innerPrint(function: PosPrinter.() -> PosPrinter): PosPrinter {
        function(this)
        return this
    }

    /**
     * 打印空白（size个汉字的位置）
     */
    fun printWordSpace(size: Int): PosPrinter {
        for (i in 0 until size) {
            writer.write("  ")
        }
        writer.flush()
        return this
    }

    /**
     * 选择字符对齐模式
     */
    fun setTextBold(isBold: Boolean = false): PosPrinter {
        write(HexCommands.TEXT_BOLD, if (isBold) 1 else 0)
        return this
    }

    /**
     * 选择字符对齐模式
     */
    fun setTextAlignment(@TextAlignment textAlignment: Int): PosPrinter {
        write(HexCommands.TEXT_ALIGNMENT, textAlignment)
        return this
    }

    /**
     * 设置绝对打印位置
     */
    fun setTextAbsoluteLocation(@IntRange(from = 0, to = 255) offsetX: Int,
                                @IntRange(from = 0, to = 255) offsetY: Int = 1): PosPrinter {
        write(HexCommands.PRINT_ABSOLUTE_LOCATION, offsetX, offsetY)
        return this
    }


    fun feedAndCut(length: Int = 100): PosPrinter {
        write(HexCommands.FEED_AND_CUT, length)
        return this
    }

    companion object {
        /**
         * Align to the start of the paragraph, e.g. ALIGN_LEFT.
         *
         * Use with [.setTextAlignment]
         */
        const val TEXT_ALIGNMENT_TEXT_START = 0

        /**
         * Center the paragraph, e.g. ALIGN_CENTER.
         *
         * Use with [.setTextAlignment]
         */
        const val TEXT_ALIGNMENT_CENTER = 1

        /**
         * Align to the end of the paragraph, e.g. ALIGN_RIGHT.
         *
         * Use with [.setTextAlignment]
         */
        const val TEXT_ALIGNMENT_TEXT_END = 2

        @IntDef(TEXT_ALIGNMENT_TEXT_START, TEXT_ALIGNMENT_CENTER, TEXT_ALIGNMENT_TEXT_END)
        @Retention(AnnotationRetention.SOURCE)
        annotation class TextAlignment
    }
}