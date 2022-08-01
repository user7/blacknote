@file:OptIn(ExperimentalSerializationApi::class)

package com.gb.blacknote

import kotlinx.serialization.*
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoNumber
import kotlinx.serialization.protobuf.schema.ProtoBufSchemaGenerator
import org.junit.Test

import org.junit.Assert.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import kotlin.test.assertContentEquals

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

class TestNested {
    @Serializable
    data class TestSI(
        @ProtoNumber(1)
        val s: String = "qq",

        @ProtoNumber(2)
        val i: Int = 11,
    )
}

@Serializable
class TestUUID(
    @ProtoNumber(1)
    var bytes: ByteArray,
)

@Serializable
class TestNullableByteArray(
    @ProtoNumber(1)
    var bytes: ByteArray?,
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

    private inline fun <reified T> roundtrip(data: T) {
        val r = ProtoBuf.encodeToByteArray(data)
        printBytes(r)
        val expected = ProtoBuf.decodeFromByteArray<T>(r)
        assertEquals(expected, data)
    }

    private inline fun <reified T, reified U> roundtrip(input: T, expected: U) {
        val r = ProtoBuf.encodeToByteArray(input)
        printBytes(r)
        val got = ProtoBuf.decodeFromByteArray<U>(r)
        assertEquals(expected, got)
    }

    @Test
    fun testOverlap() {
        // broken schema works, need to detect compile time
        roundtrip(TestOverlap(1, 2), TestOverlap(null, 2))

        assertThrows(
            "generateSchemaText throws on bad schema",
            IllegalArgumentException::class.java
        ) {
            val descriptors = listOf(TestOverlap.serializer().descriptor)
            val schemas = ProtoBufSchemaGenerator.generateSchemaText(descriptors)
            println("schema:\n$schemas")
        }
    }

    @Test
    fun testNestedRoundtrip() {
        roundtrip(TestNested.TestSI("qwe", 12))
    }

    @Test
    fun testByteArrayComparison() {
        assertNotEquals(bin("11"), bin("11"))
    }

    @Test
    fun testByteArrayContentsComparison() {
        assertContentEquals(bin("11"), bin("11"))
    }

    @Test
    fun testIntArrayComparison() {
        assertEquals(arrayListOf(9), arrayListOf(9))
    }

    @Test
    fun testUUIDByteArray() {
        val uuid = TestUUID(bin("11 22 33 44 55 66 77 88 99"))
        val bytes = ProtoBuf.encodeToByteArray(uuid)
        val uuid2 = ProtoBuf.decodeFromByteArray<TestUUID>(bytes)
        assertTrue(uuid2.bytes.contentEquals(uuid.bytes))
    }

    @Test
    fun testNullableByteArray() {
        assertThrows(
            "protobuf should refuse ByteArray? field",
            SerializationException::class.java
        )
        { roundtrip(TestNullableByteArray(null)) }
    }

// === helpers

    private fun bin(hex: String): ByteArray =
        hex.replace(" ", "").chunked(2)
            .map { it.toInt(16).toByte() }.toByteArray()

    private inline fun <reified T> readHexTest(hex: String, expected: T) {
        val bytes = bin(hex)
        try {
            val got = ProtoBuf.decodeFromByteArray<T>(bytes)
            assertEquals(
                "reading ${prettyHex(bytes)} failed, expected '$expected', got '$got'",
                expected,
                got
            )
        } catch (e: Exception) {
            fail("exception reading ${prettyHex(bytes)}: $e")
        }
    }

    private fun prettyHex(bytes: ByteArray): String {
        val sb = StringBuilder("[")
        val regex = "\\p{Punct}|\\p{Alnum}| ".toRegex()
        for (b in bytes) {
            val char = b.toUInt().toInt().toChar()
            sb.append(if (sb.length == 1) "" else ", ")
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

    private fun printBytes(bytes: ByteArray) {
        println(prettyHex(bytes))
    }
}