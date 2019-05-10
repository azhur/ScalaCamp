/**
  * 1. Error in 'Return type' that might confuse: String instead of case class. (3 methods).
  * 2. PR hygiene: description, commit messages, relevant code.
  * 3. Code organization: packages, files (not all code in one file).
  * 4. Async*Spec instead of await: http://www.scalatest.org/user_guide/async_testing.
  * 5. Not thread safe code (id based on storage length or just simple var)
  */


import java.util.concurrent.atomic.AtomicLong

import cats.{Id, Monad}

import scala.concurrent.Await
// NOTE: This import bring into the scope implicits that allow you to call .map and .flatMap on the type F[_]
// and also bring you typeclasses that know how to flatmap (Monad) and map (Functor) over your higher-kinded type.
import cats.implicits._

import scala.concurrent.{ExecutionContext, Future}

case class User(id: Long, username: String)

case class IotDevice(id: Long, userId: Long, sn: String)

trait UserRepository[F[_]] {
  def registerUser(username: String): F[User]

  def getById(id: Long): F[Option[User]]

  def getByUsername(username: String): F[Option[User]]
}

class InMemoryUserRepositoryId extends UserRepository[Id] {
  private val storage = scala.collection.mutable.Map.empty[Long, User]
  private val idGen = new AtomicLong(0L)

  override def registerUser(username: String): User = {
    val id = idGen.incrementAndGet()
    val user = User(id, username)
    storage += id -> user
    user
  }

  override def getById(id: Long): Option[User] = {
    storage.get(id)
  }

  override def getByUsername(username: String): Option[User] = {
    storage.values.find(_.username == username)
  }
}

class InMemoryUserRepositoryFuture(implicit val ec: ExecutionContext) extends UserRepository[Future] {
  private val storage = scala.collection.mutable.Map.empty[Long, User]
  private val idGen = new AtomicLong(0L)

  override def registerUser(username: String): Future[User] = Future {
    val id = idGen.incrementAndGet()
    val user = User(id, username)
    storage += id -> user
    user
  }

  override def getById(id: Long): Future[Option[User]] = Future {
    storage.get(id)
  }

  override def getByUsername(username: String): Future[Option[User]] = Future {
    println()
    storage.values.find(_.username == username)
  }
}

trait IotDeviceRepository[F[_]] {
  def registerDevice(userId: Long, serialNumber: String): F[IotDevice]

  def getById(id: Long): F[Option[IotDevice]]

  def getBySn(sn: String): F[Option[IotDevice]]

  def getByUser(userId: Long): F[Seq[IotDevice]]
}

class InMemoryIotDeviceRepositoryId extends IotDeviceRepository[Id] {
  private val storage = scala.collection.mutable.Map.empty[Long, IotDevice]
  private val idGen = new AtomicLong(0L)

  override def registerDevice(userId: Long, serialNumber: String): IotDevice = {
    val id = idGen.incrementAndGet()
    val device = IotDevice(id, userId, serialNumber)
    storage += id -> device
    device
  }

  override def getById(id: Long): Option[IotDevice] = storage.get(id)

  override def getBySn(sn: String): Option[IotDevice] = storage.values.find(_.sn == sn)

  override def getByUser(userId: Long): Seq[IotDevice] = storage.values.filter(_.userId == userId).toList
}

class InMemoryIotDeviceRepositoryFuture(implicit val ec: ExecutionContext) extends IotDeviceRepository[Future] {
  private val storage = scala.collection.mutable.Map.empty[Long, IotDevice]
  private val idGen = new AtomicLong(0L)

  override def registerDevice(userId: Long, serialNumber: String): Future[IotDevice] = Future {
    val id = idGen.incrementAndGet()
    val device = IotDevice(id, userId, serialNumber)
    storage += id -> device
    device
  }

  override def getById(id: Long): Future[Option[IotDevice]] = Future(storage.get(id))

  override def getBySn(sn: String): Future[Option[IotDevice]] = Future(storage.values.find(_.sn == sn))

  override def getByUser(userId: Long): Future[Seq[IotDevice]] = Future(storage.values.filter(_.userId == userId).toList)
}


class UserService[F[_]](repository: UserRepository[F])
                       (implicit monad: Monad[F]) {

  def registerUser(username: String): F[Either[String, User]] = {
    // .flatMap syntax works because of import cats.implicits._
    // so flatMap function is added to F[_] through implicit conversions
    // The implicit monad param knows how to flatmap and map over your F.
    repository.getByUsername(username).flatMap({
      case Some(user) =>
        monad.pure(Left(s"User $user already exists"))
      case None =>
        // .map syntax works because of import cats.implicits._
        // so map function is added to F[_] through implicit conversions
        repository.registerUser(username).map(Right(_))
    })
  }

  def getByUsername(username: String): F[Option[User]] = repository.getByUsername(username)

  def getById(id: Long): F[Option[User]] = repository.getById(id)
}

class UserServiceId(repository: UserRepository[Id]) extends UserService[Id](repository)
class UserServiceFuture(repository: UserRepository[Future])
                       (implicit ec: ExecutionContext) extends UserService[Future](repository)


class IotDeviceService[F[_]](repository: IotDeviceRepository[F],
                             userRepository: UserRepository[F])
                            (implicit monad: Monad[F]) {

  // the register should fail with Left if the user doesn't exist or the sn already exists.
  def registerDevice(userId: Long, sn: String): F[Either[String, IotDevice]] = {
    repository.getBySn(sn).flatMap({
      case Some(_) => Monad[F].pure(Left(s"Serial number $sn already exists"))
      case _ => userRepository.getById(userId).flatMap({
        case Some(user) =>
          repository.registerDevice(userId, sn).map(Right(_))
        case None =>
          Monad[F].pure(Left(s"User $userId not found"))
      })
    })
  }
}

class IotDeviceServiceId(repository: IotDeviceRepository[Id],
                         userRepository: UserRepository[Id]) extends IotDeviceService[Id](repository, userRepository)
class IotDeviceServiceFuture(repository: IotDeviceRepository[Future],
                             userRepository: UserRepository[Future])
                            (implicit ec: ExecutionContext) extends IotDeviceService[Future](repository, userRepository)


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

val userRepoId = new InMemoryUserRepositoryId
val userRepoFuture = new InMemoryUserRepositoryFuture

val userServiceId = new UserServiceId(userRepoId)
val userServiceFuture = new UserServiceFuture(userRepoFuture)

Await.result(userServiceFuture.registerUser("test"), 5.seconds)
Await.result(userServiceFuture.getByUsername("test"), 5.seconds)
Await.result(userServiceFuture.getByUsername("test"), 5.seconds)
Await.result(userServiceFuture.registerUser("test"), 5.seconds)

val iotDeviceRepositoryId = new InMemoryIotDeviceRepositoryId
val iotDeviceRepositoryFuture = new InMemoryIotDeviceRepositoryFuture

val iotDeviceServiceId = new IotDeviceServiceId(iotDeviceRepositoryId, userRepoId)
val iotDeviceServiceFuture = new IotDeviceServiceFuture(iotDeviceRepositoryFuture, userRepoFuture)

Await.result(iotDeviceServiceFuture.registerDevice(1, "sn1"), 5.seconds)
Await.result(iotDeviceServiceFuture.registerDevice(0, "sn2"), 5.seconds)
Await.result(iotDeviceServiceFuture.registerDevice(1, "sn1"), 5.seconds)
Await.result(iotDeviceServiceFuture.registerDevice(1, "sn2"), 5.seconds)