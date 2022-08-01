package com.gb.blacknote.db.binary_format.proto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class FolderNodeProto(
    @ProtoNumber(1)
    val name: String? = null,

    @ProtoNumber(2)
    val items: List<ChunkRefProto> = listOf(),
)