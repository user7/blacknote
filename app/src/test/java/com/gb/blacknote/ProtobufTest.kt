package com.gb.blacknote

import com.gb.blacknote.model.SKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoNumber
import kotlinx.serialization.protobuf.schema.ProtoBufSchemaGenerator
import org.junit.Test

import org.junit.Assert.*
import java.lang.Exception

@Serializable
data class TestSI(
    @ProtoNumber(1)
    val s: String = "qq",

    @ProtoNumber(2)
    val i: Int = 11,
)

@Serializable
data class TestSqIq(
    @ProtoNumber(1)
    val s: String?,

    @ProtoNumber(2)
    val i: Int?
)

@Serializable
data class TestOverlap(
    @ProtoNumber(1)
    val i: Int?,

    @ProtoNumber(1)
    val j: Int?,
)

@Serializable
data class TestComposite(
    @ProtoNumber(1)
    val a: TestSI,

    @ProtoNumber(2)
    val b: TestSI,
)

class ProtobufTest {
    @Test
    fun testRequired() {
        roundtrip(TestSI("hi!", 65))
    }

    @Test
    fun testComposite() {
        roundtrip(TestComposite(TestSI("aaaa", 99), TestSI("bbbb", 98)))
    }

    @Test
    fun testRequiredMissing() {
        readHexTest("0A 03 71 77 65 10 64", TestSI("qwe", 100))
        readHexTest("0A 03 71 77 65", TestSI("qwe", 11))
        readHexTest("", TestSI("qq", 11))
    }

    @Test
    fun testOptional() {
        roundtrip(TestSqIq(null, null))
        roundtrip(TestSqIq("qwe", 99))
    }

    @Test
    fun testTrailingBytes() {
        readHexTest("", TestSqIq(null, null))
        readHexTest("0A 03 71 77 65 10 63", TestSqIq("qwe", 99))
        readHexTest("0A 03 71 77 65 10 61 0A 03 71 77 65 10 63", TestSqIq("qwe", 99))
    }

    inline fun <reified T> roundtrip(data: T) {
        val r = ProtoBuf.encodeToByteArray(data)
        printBytes(r)
        val expected = ProtoBuf.decodeFromByteArray<T>(r)
        assertEquals(expected, data)
    }

    inline fun <reified T, reified U> roundtrip(input: T, expected: U) {
        val r = ProtoBuf.encodeToByteArray(input)
        printBytes(r)
        val got = ProtoBuf.decodeFromByteArray<U>(r)
        assertEquals(expected, got)
    }

    @Test
    fun testOverlap() {
//        val descriptors = listOf(TestOverlap.serializer().descriptor)
//        val schemas = ProtoBufSchemaGenerator.generateSchemaText(descriptors)
//        println("schema:\n$schemas")

        // broken schema works, need to detect compile time
        roundtrip(TestOverlap(1, 2), TestOverlap(null, 2))
    }

    @Test
    fun roundrtipSKey() {
        roundtrip(SKey(123, 3232, 12321, byteArrayOf(1, 0x23, 3, 4, 5)))
    }

    inline fun <reified T> readHexTest(hex: String, expected: T) {
        val bytes = hex.replace(" ", "").chunked(2)
            .map { it.toInt(16).toByte() }.toByteArray()
        try {
            val got = ProtoBuf.decodeFromByteArray<T>(bytes)
            assertEquals(
                "reading ${hexBytes(bytes)} failed, expected '$expected', got '$got'",
                expected,
                got
            )
        } catch (e: Exception) {
            fail("exception reading ${hexBytes(bytes)}: $e")
        }
    }

    fun hexBytes(bytes: ByteArray): String {
        val sb = StringBuilder()
        val regex = "\\p{Punct}|\\p{Alnum}| ".toRegex()
        for (b in bytes) {
            val char = b.toUInt().toInt().toChar()
            sb.append(if (sb.isEmpty()) "[" else ", ")
            sb.append(
                "%02X%s".format(
                    b.toUByte().toInt(),
                    if (regex.matches(char.toString())) " $char" else ""
                )
            )
        }
        sb.append("]")
        return sb.toString()
    }

    fun printBytes(bytes: ByteArray) {
        println(hexBytes(bytes))
    }
}