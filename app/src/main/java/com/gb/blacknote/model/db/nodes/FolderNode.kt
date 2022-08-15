package com.gb.blacknote.model.db.nodes

import com.gb.blacknote.model.db.ChunkRef
import java.util.*
import kotlin.collections.ArrayList

class FolderNode(
    nodeId: UUID,
    val name: String,
    val items: ArrayList<ChunkRef> = arrayListOf(),
) : Node(nodeId)