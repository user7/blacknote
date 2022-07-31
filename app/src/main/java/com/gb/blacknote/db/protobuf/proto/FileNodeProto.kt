package com.gb.blacknote.db.protobuf.proto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class FileNodeProto(
    @ProtoNumber(1)
    val name: String?,

    @ProtoNumber(2)
    val contents: ByteArray?,
)