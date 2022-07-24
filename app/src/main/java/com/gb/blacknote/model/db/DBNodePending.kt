package com.gb.blacknote.model.db

class DBNodePending(
    ref: DBChunkRef
) : DBNode(ref) {
    var loading: Boolean = false
}