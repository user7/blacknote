package com.gb.blacknote.db.protobuf

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

// a readable node, apart from uuid one and only one field should be present, if no field is present
// that means it's a newer type no supported by current app version

@ExperimentalSerializationApi
@Serializable
class SVariant(
    @ProtoNumber(1)
    val uuid: SUUID,

    @ProtoNumber(2)
    val folder: SFolder?,

    @ProtoNumber(3)
    val note: SNote?,
)
