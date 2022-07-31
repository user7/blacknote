package com.gb.blacknote.db.protobuf.proto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class ChunkRefProto(
    @ProtoNumber(1)
    val chunkId: ByteArray, // UUID

    @ProtoNumber(2)
    val keyId: ByteArray, // UUID

    @ProtoNumber(3)
    val iv: ByteArray,
)