package com.gb.blacknote.db.protobuf

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class SChunkHeader(
    @ProtoNumber(1)
    val salt: ByteArray,

    @ProtoNumber(2)
    val dataLength: Int,

    @ProtoNumber(3)
    val dataHash: ByteArray,
)