package com.gb.blacknote.db.protobuf

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class SFolder(
    @ProtoNumber(1)
    val name: String = "",

    @ProtoNumber(2)
    val items: List<SChunkRef> = listOf(),
)