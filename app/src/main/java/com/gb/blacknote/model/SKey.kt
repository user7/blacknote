package com.gb.blacknote.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoIntegerType
import kotlinx.serialization.protobuf.ProtoNumber
import kotlinx.serialization.protobuf.ProtoType

// when user gives pass:
// key1 = PBKDF2(pass, .passHardeningSalt, .passHardeningIterations)
// key2 = xor(key1, .keyBytes)
// if (hash(key2) == .keyHash)
//     then key2 is actual aes128key

@ExperimentalSerializationApi
@Suppress("ArrayInDataClass")
@Serializable
data class SKey constructor(
    @ProtoNumber(1)
    @ProtoType(ProtoIntegerType.FIXED)
    val passHardeningSalt: Long,

    @ProtoNumber(2)
    val passHardeningIterations: Int,

    @ProtoNumber(3)
    @ProtoType(ProtoIntegerType.FIXED)
    val keyHash: Long,

    @ProtoNumber(4)
    val keyBytes: ByteArray,
)