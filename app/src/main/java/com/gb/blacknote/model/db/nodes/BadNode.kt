package com.gb.blacknote.model.db.nodes

import java.util.*

class BadNode(
    nodeId: UUID,
    val error: String
) : Node(nodeId)