package com.gb.blacknote.save

import java.util.UUID

data class BKey(

)

data class NodeRef(
    val objId: UUID,
    val keyId: Int,
    val key: Key,
)

