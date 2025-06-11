package com.octopus.edu.kotlin.core.data.common

import com.octopus.edu.kotlin.core.data.common.dataStructure.DoublyLinkedList
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class DoublyLinkedListTest {
    private lateinit var list: DoublyLinkedList<String>

    @Before
    fun setUp() {
        list = DoublyLinkedList()
    }

    @Test
    fun `list is empty initially`() {
        assertTrue(list.isEmpty())
        assertEquals(0, list.size)
    }

    @Test
    fun `addFirst adds element to front`() {
        list.addFirst("A")
        list.addFirst("B")

        assertEquals(2, list.size)
        assertEquals("B", list.peekFirst())
        assertEquals("A", list.peekLast())
    }

    @Test
    fun `addLast adds element to end`() {
        list.addLast("A")
        list.addLast("B")

        assertEquals(2, list.size)
        assertEquals("A", list.peekFirst())
        assertEquals("B", list.peekLast())
    }

    @Test
    fun `removeFirst removes element from front`() {
        list.addLast("A")
        list.addLast("B")

        val removed = list.removeFirst()

        assertEquals("A", removed)
        assertEquals(1, list.size)
        assertEquals("B", list.peekFirst())
    }

    @Test
    fun `removeLast removes element from end`() {
        list.addLast("A")
        list.addLast("B")

        val removed = list.removeLast()

        assertEquals("B", removed)
        assertEquals(1, list.size)
        assertEquals("A", list.peekLast())
    }

    @Test
    fun `remove returns true when element is removed`() {
        list.addLast("A")
        list.addLast("B")

        val result = list.remove("A")

        assertTrue(result)
        assertEquals(1, list.size)
        assertEquals("B", list.peekFirst())
    }

    @Test
    fun `remove returns false when element is not found`() {
        list.addLast("A")

        val result = list.remove("B")

        assertFalse(result)
        assertEquals(1, list.size)
    }

    @Test
    fun `removeFirst returns null when list is empty`() {
        val result = list.removeFirst()
        assertNull(result)
    }

    @Test
    fun `removeLast returns null when list is empty`() {
        val result = list.removeLast()
        assertNull(result)
    }

    @Test
    fun `peekFirst and peekLast return correct values`() {
        list.addLast("A")
        list.addLast("B")
        list.addLast("C")

        assertEquals("A", list.peekFirst())
        assertEquals("C", list.peekLast())
    }

    @Test
    fun `toList returns list in correct order`() {
        list.addLast("A")
        list.addLast("B")
        list.addLast("C")

        val result = list.toList()
        assertEquals(listOf("A", "B", "C"), result)
    }

    @Test
    fun `toString reflects list contents`() {
        list.addFirst("X")
        list.addLast("Y")
        assertEquals("[X, Y]", list.toString())
    }

    @Test
    fun `remove head and tail to empty the list`() {
        list.addLast("A")
        list.addLast("B")
        list.remove("A")
        list.remove("B")

        assertTrue(list.isEmpty())
        assertNull(list.peekFirst())
        assertNull(list.peekLast())
    }
}
