package com.octopus.edu.kotlin.core.data.common.dataStructure

class DoublyLinkedList<T> {
    private inner class Node<T>(
        val value: T,
        var next: Node<T>? = null,
        var prev: Node<T>? = null
    )

    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    var size: Int = 0
        private set

    fun isEmpty(): Boolean = size == 0

    fun addFirst(value: T) {
        val newNode = Node(value, prev = null, next = head)
        if (isEmpty()) {
            tail = newNode
        } else {
            head?.prev = newNode
        }
        head = newNode
        size++
    }

    fun addLast(value: T) {
        val newNode = Node(value, prev = tail, next = null)
        if (isEmpty()) {
            head = newNode
        } else {
            tail?.next = newNode
        }
        tail = newNode
        size++
    }

    fun remove(value: T): Boolean {
        var current = head
        while (current != null) {
            if (current.value == value) {
                if (current.prev != null) {
                    current.prev!!.next = current.next
                } else {
                    head = current.next
                }

                if (current.next != null) {
                    current.next!!.prev = current.prev
                } else {
                    tail = current.prev
                }

                size--
                return true
            }
            current = current.next
        }
        return false
    }

    fun removeFirst(): T? {
        val removedData = head?.value ?: return null
        head = head?.next
        if (head == null) {
            tail = null
        } else {
            head?.prev = null
        }
        size--
        return removedData
    }

    fun removeLast(): T? {
        val removedData = tail?.value ?: return null
        tail = tail?.prev
        if (tail == null) {
            head = null
        } else {
            tail?.next = null
        }
        size--
        return removedData
    }

    fun peekFirst(): T? = head?.value

    fun peekLast(): T? = tail?.value

    fun toList(): List<T> {
        val result = mutableListOf<T>()
        var current = head
        while (current != null) {
            result.add(current.value)
            current = current.next
        }
        return result
    }

    override fun toString(): String = toList().toString()
}
