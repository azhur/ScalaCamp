val jvmLanguages = List("Java", "Scala")
val nonJvmLanguages = List("Go", "JavaScript", "Haskell", "Python")
val languages = jvmLanguages ++ nonJvmLanguages
val salaries = Map (
  "Java" -> 1000,
  "Scala" -> 2000,
  "Go" -> 1500,
  "Haskell" -> 500
)

// NOTE: almost all the methods below are available for Map too

val indices = languages.indices

// head|headOption|last|lastOption|init|tail

val firstLang = languages.head

val firstHuman = List().headOption

val lastLang = languages.last

val lastHuman = List().lastOption

val allLangExceptLast = languages.init

val allLangExceptFirst = languages.tail

// take|takeRight|takeWhile
val first2Langs = languages.take(2)

val last2Langs = languages.takeRight(2)

val firstLangsWithA = languages.takeWhile(_.contains("a"))

val allLangsExceptFirst2 = languages.drop(2)

val allLangsExceptLast2 = languages.dropRight(2)

val allExceptFirstContainingA = languages.dropWhile(_.contains("a"))

// exists|contains|forall
val langExistsThatStartsWithJ = languages.exists(_.startsWith("J"))

val containsJava = languages.contains("Java")

val allLangsStartWithJ = languages.forall(_.startsWith("J"))

// partition|span|sliding
val (allStartWithJ, theRest) = languages.partition(_.startsWith("J"))
// takes first N elems that satisfy predicate
val (firstContainingA, others) = languages.span(_.contains("a"))

val slidedLangs = languages.sliding(size = 2, step = 1).toList

// fold|reduce
val folded = languages.fold("")((e1, e2) => e1 + e2)
val foldedLeft = languages.foldLeft(0l)((e1, e2) => e1 + e2.length)
val foldedRight = languages.foldRight(0L)((e1, e2) => e1.length + e2)

val reduced = languages.reduce(_ + _)
val reducedOption = languages.reduceOption(_ + _)
val reducedLeft = languages.reduceLeft(_ + _)
val reducedLeftOption = languages.reduceLeftOption(_ + _)
val reducedRight = languages.reduceRight(_ + _)
val reducedRightOption = languages.reduceRightOption(_ + _)

// zip|unzip|zipWithIndex
val withIndexes = languages.zipWithIndex
val zipped = jvmLanguages.zip(nonJvmLanguages)
val unzipped = zipped.unzip

// intersect|union|distinct|diff
val intersection = jvmLanguages.intersect(nonJvmLanguages)
val union = jvmLanguages.union(nonJvmLanguages)
val distinct = languages.distinct
val diff = jvmLanguages.diff(nonJvmLanguages)

// maxBy|max|sum
val maxBy = languages.maxBy(_.length)
val max = languages.max
val sum = languages.map(_.length).sum