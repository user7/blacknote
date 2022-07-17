package com.gb.blacknote.storage.protobuf

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class SRef(
    @ProtoNumber(1)
    val chunkId: SUUID,

    @ProtoNumber(2)
    val keyId: Long,

    @ProtoNumber(3)
    val head: ByteArray,
)