/*
 * Copyright (C) 2009-2014 Typesafe Inc. <http://www.typesafe.com>
 */

package akka.http.scaladsl.server.util

/**
 * Provides a way to convert a value into an Tuple.
 * If the value is already a Tuple then it is returned unchanged, otherwise it's wrapped in a Tuple1 instance.
 */
trait Tupler[T] {
  type Out
  def OutIsTuple: Tuple[Out]
  def apply(value: T): Out
}

object Tupler extends LowerPriorityTupler {
  implicit def forTuple[T: Tuple] =
    new Tupler[T] {
      type Out = T
      def OutIsTuple = implicitly[Tuple[Out]]
      def apply(value: T) = value
    }
}

private[server] abstract class LowerPriorityTupler {
  implicit def forAnyRef[T] =
    new Tupler[T] {
      type Out = Tuple1[T]
      def OutIsTuple = implicitly[Tuple[Out]]
      def apply(value: T) = Tuple1(value)
    }
}