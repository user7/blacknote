package com.gb.blacknote.db.protobuf.proto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class DBHeaderProto(
    @ProtoNumber(1)
    val keys: List<StoredKeyProto> = listOf(),

    @ProtoNumber(2)
    val rootNode: ChunkRefProto,
)