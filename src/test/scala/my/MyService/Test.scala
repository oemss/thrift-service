package my.MyService

import java.io.IOException

import com.twitter.finagle.Thrift
import org.scalatest._
import com.twitter.util.{Await, Future}
import org.scalatest.{FunSpec, Matchers}
import Matchers._
import org.scalacheck.Prop.Exception

/**
  * Created by evgeniy on 15.08.16.
  */
class Test extends FunSpec with Matchers {
  val client = Thrift.client.newIface[MyServ[Future]]("127.0.0.1:6666")
  client.put(0, Seq(Rt(id = "1", name = "One"),
    Rt(id = "2", name = "Two"),
    Rt(id = "3", name = "Three"),
    Rt(id = "4", name = "Four"),
    Rt(id = "5", name = "Five"),
    Rt(id = "6", name = "Six"),
    Rt(id = "7", name = "Seven"),
    Rt(id = "8", name = "Eight"),
    Rt(id = "9", name = "Nine"),
    Rt(id = "10", name = "Ten")
  ))
  client.put(1, Seq(Rt(id = "1", name = "Hello"),
    Rt(id = "2", name = "Evgeniy"),
    Rt(id = "3", name = "World"),
    Rt(id = "4", name = "Magic"),
    Rt(id = "5", name = "Scala"),
    Rt(id = "6", name = "Java"),
    Rt(id = "7", name = "Thrift"),
    Rt(id = "8", name = "Service"),
    Rt(id = "9", name = "Client")
  ))
  Thread.sleep(2000)
  describe("first") {

    it("add") {
      for {
        _ <- client.add("1", "1")
        _ <- client.add("1", "2")
        _ <- client.add("1", "4")
      } yield ()
    }

    it("Вернуть по записи список тегов") {
      Thread.sleep(50)
      val lst1 = Await.result(client.listT("1"))
      lst1 should contain only (Rt(id = "1", name = "Hello"), Rt(id = "2", name = "Evgeniy"), Rt(id = "4", name = "Magic"))
      for {
        _ <- client.add("2", "1")
        _ <- client.add("2", "4")
      } yield ()
      Thread.sleep(50)
      val lst2 = Await.result(client.listT("2"))
      lst2 should contain only (Rt(id = "1", name = "Hello"), Rt(id = "4", name = "Magic"))

    }

    it("Не добавляет ли копии записей?") {
      Thread.sleep(65)
      for {
        _ <- client.add("1", "1")
        _ <- client.add("1", "2")
        _ <- client.add("1", "4")
      } yield ()
      Thread.sleep(50)
      val lst = Await.result(client.listT("1"))
      lst should contain only (Rt(id = "1", name = "Hello"), Rt(id = "2", name = "Evgeniy"), Rt(id = "4", name = "Magic"))
    }

    it("Работа вывода по списку тэгов")
    {
      Thread.sleep(75)
      val lst = Await.result(client.listR(Seq("1","2")))
      lst should contain only (Rt(id = "1", name = "One"),Rt(id = "2", name = "Two"))
    }
    it("delete") {
      Thread.sleep(80)
      for {
        _ <- client.delete("1","4")
      } yield ()
      Thread.sleep(85)
      val lst = Await.result(client.listT("1"))
      lst should contain only (Rt(id = "1", name = "Hello"), Rt(id = "2", name = "Evgeniy"))
      Thread.sleep(50)

    }
    describe("Ловим ошибки") {
      Thread.sleep(1000)
      it("Пытаемся вывести элемент которого нет") {
        Thread.sleep(50)
        (try {
          Await.result(client.listT("3"))
          true
        } catch {
          case e: Throwable => false
        }) should be(false)
      }
      it("Пытаемся вывести элемент который удалили") {
        Thread.sleep(50)
        for {
          _ <- client.delete("1", "2")
          _ <- client.delete("1", "1")
        } yield ()
        Thread.sleep(50)
        (try {
          Await.result(client.listT("1"))
          true
        } catch {
          case e: Throwable => false
        }) should be(false)
      }
      it("Пытаемся выполнить add с элементами которых нет в начальных списках"){
        Thread.sleep(50)
        (try {
          Await.result(client.add("13", "1"))
          true
        } catch {
          case e: Throwable => false
        }) should be(false)
      }
    }
  }
}

