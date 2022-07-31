package com.gb.blacknote.db.protobuf.proto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class NoteNodeProto(
    @ProtoNumber(1)
    val title: String? = null,

    @ProtoNumber(2)
    val comment: String? = null,
)