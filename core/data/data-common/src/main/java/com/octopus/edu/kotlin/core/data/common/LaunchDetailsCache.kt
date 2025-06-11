package com.octopus.edu.kotlin.core.data.common

import com.octopus.edu.kotlin.core.data.common.dataStructure.DoublyLinkedList
import com.octopus.edu.kotlin.core.domain.models.launch.LaunchDetails
import javax.inject.Inject
import javax.inject.Named

class LaunchDetailsCache
    @Inject
    constructor(
        @Named("launch_details_cache_size") private val capacity: Int
    ) : DataCache<Int, LaunchDetails>() {
        private val cacheHistory: DoublyLinkedList<LaunchDetails> = DoublyLinkedList()
        private val cache: HashMap<Int, LaunchDetails?> = hashMapOf()

        override fun get(key: Int): LaunchDetails? =
            cache[key]?.let { value ->
                cacheHistory.remove(value)
                cacheHistory.addFirst(value)
                return@let value
            }

        override fun set(
            key: Int,
            value: LaunchDetails
        ) {
            cacheHistory.addFirst(value)

            cache[key]?.let { value -> cacheHistory.remove(value) }

            if (cache.size == capacity) {
                cacheHistory.removeLast().also { value ->
                    cache.remove(value?.flightNumber)
                }
            }

            cache[key] = value
        }
    }
