package com.gb.blacknote.storage.protobuf

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class SDBHeader(
    @ProtoNumber(1)
    val keys: List<SKey> = listOf(),

    @ProtoNumber(2)
    val rootNode: SRef,
)