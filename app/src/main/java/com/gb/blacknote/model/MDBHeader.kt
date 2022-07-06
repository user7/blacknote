package com.gb.blacknote.model

import com.gb.blacknote.storage.protobuf.SDBHeader
import javax.crypto.SecretKey

class MDBHeader(val sdbHeader: SDBHeader) {
    val keys: Map<Long, SecretKey> = mapOf()
    val root: MNode = MNodePending(sdbHeader.rootNode)
}
