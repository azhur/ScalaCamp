import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

/*
 * Transform Java CompletableFuture to Scala Future.
 */

def javaCall(): CompletableFuture[String] = CompletableFuture.completedFuture("completed")

implicit class RichCompletedFuture[T](value: CompletableFuture[T]) {
  def asScalaFuture: Future[T] = {
    val promise = Promise[T]()

    value.whenComplete(new BiConsumer[T, Throwable] {
      override def accept(t: T, u: Throwable) = {
        if (u != null) promise.tryFailure(u)
        else promise.success(t)
      }
    })

    promise.future
  }
}

javaCall().asScalaFuture.map(println)