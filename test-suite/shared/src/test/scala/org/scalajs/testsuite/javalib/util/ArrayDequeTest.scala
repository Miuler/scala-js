/*
 * Scala.js (https://www.scala-js.org/)
 *
 * Copyright EPFL.
 *
 * Licensed under Apache License 2.0
 * (https://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package org.scalajs.testsuite.javalib.util

import java.util

import org.junit.Test
import org.junit.Assert._

import java.{util => ju}

import scala.reflect.ClassTag

class ArrayDequeTest extends AbstractCollectionTest with DequeTest {

  override def factory: ArrayDequeFactory = new ArrayDequeFactory

  @Test def addRemovePeekFirstAndLastInt(): Unit = {
    val ad = factory.empty[Int]

    ad.addLast(1)
    ad.removeFirst()
    ad.addLast(2)
    assertEquals(ad.peekFirst(), 2)

    ad.clear()

    ad.addFirst(1)
    ad.removeLast()
    ad.addFirst(2)
    assertEquals(ad.peekLast(), 2)
  }

  @Test def fromCollectionInt(): Unit = {
    val l = TrivialImmutableCollection(1, 5, 2, 3, 4)
    val ad = factory.from[Int](l)

    assertEquals(ad.size(), 5)

    for (i <- 0 until l.size())
      assertEquals(ad.poll(), l(i))

    assertTrue(ad.isEmpty)
  }

  @Test def addAllCollectionAndAddInt(): Unit = {
    val ad = factory.empty[Int]

    assertEquals(ad.size(), 0)
    ad.addAll(TrivialImmutableCollection(1, 5, 2, 3, 4))
    assertEquals(ad.size(), 5)
    ad.add(6)
    assertEquals(ad.size(), 6)
  }

  @Test def addAndPollLastString(): Unit = {
    val adInt = factory.empty[Int]

    assertTrue(adInt.add(1000))
    assertTrue(adInt.add(10))
    assertEquals(adInt.pollLast(), 10)

    val adString = factory.empty[String]

    assertTrue(adString.add("pluto"))
    assertTrue(adString.add("pippo"))
    assertEquals(adString.pollLast(), "pippo")

    val adDouble = factory.empty[Double]

    assertTrue(adDouble.add(+10000.987))
    assertTrue(adDouble.add(-0.987))
    assertEquals(adDouble.pollLast(), -0.987, 0.0)
  }

  @Test def pushAndPopString(): Unit = {
    val adInt = factory.empty[Int]

    adInt.push(1000)
    adInt.push(10)
    assertEquals(adInt.pop(), 10)
    assertEquals(adInt.pop(), 1000)
    assertTrue(adInt.isEmpty())

    val adString = factory.empty[String]

    adString.push("pluto")
    adString.push("pippo")
    assertEquals(adString.pop(), "pippo")
    assertEquals(adString.pop(), "pluto")
    assertTrue(adString.isEmpty())

    val adDouble = factory.empty[Double]

    adDouble.push(+10000.987)
    adDouble.push(-0.987)
    assertEquals(adDouble.pop(), -0.987, 0.0)
    assertEquals(adDouble.pop(), +10000.987, 0.0)
    assertTrue(adString.isEmpty())
  }

  @Test def peekAndPollFirstAndLastString(): Unit = {
    val pq = factory.empty[String]

    assertTrue(pq.add("one"))
    assertTrue(pq.add("two"))
    assertTrue(pq.add("three"))

    assertTrue(pq.peek.equals("one"))
    assertTrue(pq.poll.equals("one"))

    assertTrue(pq.peekFirst.equals("two"))
    assertTrue(pq.pollFirst.equals("two"))

    assertTrue(pq.peekLast.equals("three"))
    assertTrue(pq.pollLast.equals("three"))

    assertNull(pq.peekFirst)
    assertNull(pq.pollFirst)

    assertNull(pq.peekLast)
    assertNull(pq.pollLast)
  }

  @Test def removeFirstAndLastOccurrenceString(): Unit = {
    val ad = factory.from[String](
        TrivialImmutableCollection("one", "two", "three", "two", "one"))

    assertTrue(ad.removeFirstOccurrence("one"))
    assertTrue(ad.removeLastOccurrence("two"))
    assertTrue(ad.removeFirstOccurrence("one"))
    assertTrue(ad.removeLastOccurrence("two"))
    assertTrue(ad.removeFirstOccurrence("three"))
    assertFalse(ad.removeLastOccurrence("three"))
    assertTrue(ad.isEmpty)
  }

  @Test def iteratorDescendingIterator(): Unit = {
    val l = TrivialImmutableCollection("one", "two", "three")
    val ad = factory.from[String](l)

    val iter = ad.iterator()
    for (i <- 0 until l.size()) {
      assertTrue(iter.hasNext())
      assertEquals(iter.next(), l(i))
    }
    assertFalse(iter.hasNext())

    val diter = ad.descendingIterator()
    for (i <- (0 until l.size()).reverse) {
      assertTrue(diter.hasNext())
      assertEquals(diter.next(), l(i))
    }
    assertFalse(diter.hasNext())
  }
}

class ArrayDequeFactory extends AbstractCollectionFactory with DequeFactory {
  override def implementationName: String =
    "java.util.ArrayDeque"

  override def empty[E: ClassTag]: ju.ArrayDeque[E] =
    new ju.ArrayDeque[E]

  def from[E](coll: ju.Collection[E]): ju.ArrayDeque[E] =
    new ju.ArrayDeque[E](coll)

  override def allowsNullElement: Boolean = false
}
