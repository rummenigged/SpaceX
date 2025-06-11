package com.octopus.edu.kotlin.core.data.common

abstract class DataCache<Key, Value> {
    abstract fun get(key: Key): Value?

    abstract fun set(
        key: Key,
        value: Value
    )
}
