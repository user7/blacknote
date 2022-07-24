package com.gb.blacknote.db.protobuf

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
@Serializable
class SKey(
    @ProtoNumber(1)
    val passSalt: ByteArray,

    @ProtoNumber(2)
    val keyId: ByteArray,

    @ProtoNumber(3)
    val keyBytes: ByteArray,
)