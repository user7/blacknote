package com.gb.blacknote.db.binary_format.proto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

// a readable node, apart from uuid one and only one field should be present, if no field is present
// that means it's a newer type no supported by current app version

@ExperimentalSerializationApi
@Serializable
class ChunkProto(
    @ProtoNumber(1)
    val nodeId: ByteArray,

    @ProtoNumber(3)
    val folder: FolderNodeProto? = null,

    @ProtoNumber(4)
    val note: NoteNodeProto? = null,

    @ProtoNumber(2)
    val file: FileNodeProto? = null,
)
