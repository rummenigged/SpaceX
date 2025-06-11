package com.octopus.edu.kotlin.core.data.common

import com.octopus.edu.kotlin.core.domain.models.launch.LaunchDetails
import com.octopus.edu.kotlin.core.domain.models.launch.mock
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Before
import org.junit.Test

class LaunchDetailsCacheTest {
    private lateinit var cache: LaunchDetailsCache

    private val launch1 = LaunchDetails.mock(1)
    private val launch2 = LaunchDetails.mock(2)
    private val launch3 = LaunchDetails.mock(3)
    private val launch4 = LaunchDetails.mock(4)

    @Before
    fun setup() {
        cache = LaunchDetailsCache(3)
    }

    @Test
    fun `put and get should return the same launch`() {
        cache.set(launch1.flightNumber, launch1)
        val result = cache.get(launch1.flightNumber)
        assertEquals(launch1, result)
    }

    @Test
    fun `get should move item to front of usage history`() {
        cache.set(launch1.flightNumber, launch1)
        cache.set(launch2.flightNumber, launch2)
        cache.set(launch3.flightNumber, launch3)

        cache.get(launch1.flightNumber)

        cache.set(launch4.flightNumber, launch4)

        assertNull(cache.get(launch2.flightNumber))
        assertNotNull(cache.get(launch1.flightNumber))
        assertNotNull(cache.get(launch3.flightNumber))
        assertNotNull(cache.get(launch4.flightNumber))
    }

    @Test
    fun `set should evict least recently used item when capacity is exceeded`() {
        cache.set(launch1.flightNumber, launch1)
        cache.set(launch2.flightNumber, launch2)
        cache.set(launch3.flightNumber, launch3)

        cache.set(launch4.flightNumber, launch4)

        assertNull(cache.get(launch1.flightNumber))
        assertNotNull(cache.get(launch2.flightNumber))
        assertNotNull(cache.get(launch3.flightNumber))
        assertNotNull(cache.get(launch4.flightNumber))
    }

    @Test
    fun `set should update existing entry and move it to front`() {
        val updatedLaunch2 = LaunchDetails.mock(2)

        cache.set(launch1.flightNumber, launch1)
        cache.set(launch2.flightNumber, launch2)
        cache.set(launch3.flightNumber, launch3)

        cache.set(launch2.flightNumber, updatedLaunch2)

        assertEquals(updatedLaunch2, cache.get(launch2.flightNumber))
    }

    @Test
    fun `get should return null for non-existent key`() {
        assertNull(cache.get(999))
    }
}
