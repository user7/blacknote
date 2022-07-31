package com.gb.blacknote.model.db.nodes

import com.gb.blacknote.model.db.NodeHolder
import java.util.*
import kotlin.collections.ArrayList

class FolderNode(
    nodeId: UUID,
    val name: String,
    val items: ArrayList<NodeHolder> = arrayListOf(),
) : Node(nodeId)