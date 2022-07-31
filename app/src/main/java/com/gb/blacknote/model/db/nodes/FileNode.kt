package com.gb.blacknote.model.db.nodes

import java.util.*

class FileNode(
    nodeId: UUID,
    val name: String,
    val contents: ByteArray,
) : Node(nodeId)