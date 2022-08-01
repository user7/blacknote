package com.gb.blacknote.db.binary_format.proto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class DatabaseHeaderProto(
    @ProtoNumber(1)
    val keys: List<StoredKeyProto> = listOf(),

    @ProtoNumber(2)
    val rootNode: ChunkRefProto,
)