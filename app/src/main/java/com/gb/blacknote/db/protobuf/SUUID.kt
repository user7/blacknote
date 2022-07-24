package com.gb.blacknote.db.protobuf

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoIntegerType
import kotlinx.serialization.protobuf.ProtoNumber
import kotlinx.serialization.protobuf.ProtoType

@ExperimentalSerializationApi
@Serializable
class SUUID(
    @ProtoType(ProtoIntegerType.FIXED)
    @ProtoNumber(1)
    val uuidLow: Long,

    @ProtoType(ProtoIntegerType.FIXED)
    @ProtoNumber(2)
    val uuidHigh: Long,
)