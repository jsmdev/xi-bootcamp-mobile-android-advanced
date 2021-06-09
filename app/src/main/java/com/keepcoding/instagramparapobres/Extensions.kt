package com.keepcoding.instagramparapobres

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn

fun Boolean.alsoIfTrue(cb: () -> Unit) {
    if(this) cb()
}
