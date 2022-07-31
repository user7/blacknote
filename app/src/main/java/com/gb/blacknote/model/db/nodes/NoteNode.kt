package com.gb.blacknote.model.db.nodes

import java.util.*

class NoteNode(
    nodeId: UUID,
    val title: String,
    val comment: String,
) : Node(nodeId)