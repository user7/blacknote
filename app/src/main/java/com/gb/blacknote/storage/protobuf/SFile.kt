package com.gb.blacknote.storage.protobuf

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class SFile(
    @ProtoNumber(1)
    val name: String = "",

    @ProtoNumber(2)
    val body: ByteArray,
)