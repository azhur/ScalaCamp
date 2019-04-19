import scala.annotation.tailrec
import scala.concurrent.duration.FiniteDuration

@tailrec
def retry[A](block: () => A,
             acceptResult: A => Boolean,
             retries: List[FiniteDuration]): A = {
  val result = block()
  if (acceptResult(result) || retries.isEmpty) {
    result
  } else {
    Thread.sleep(retries.head.toMillis)
    retry(block, acceptResult, retries.tail)
  }
}


/**
  * 1. waiting before very first attempt, failing on empty retries List.
  * 2. exceptions (neither throwing nor catching)
  * 3. null checking and throwing.
  * 4. non effective unit tests: number of retries, interval between retries.
  * 5. over-engineering: several functions, inheritance, identical retry function inside another retry function
  */