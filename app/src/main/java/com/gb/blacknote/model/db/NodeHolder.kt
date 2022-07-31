package com.gb.blacknote.model.db

import com.gb.blacknote.model.db.nodes.Node

class NodeHolder(
    val chunkRef: ChunkRef,
    var node: Node? = null,
)