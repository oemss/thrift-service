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

  describe("first") {

    client.add(Rt(id = "1", name = "One"), Rt(id = "1", name = "Hello"))
    client.add(Rt(id = "1", name = "One"), Rt(id = "2", name = "Evgeniy"))
    client.add(Rt(id = "1", name = "One"), Rt(id = "4", name = "Magic"))
    client.add(Rt(id = "5", name = "Five"), Rt(id = "8", name = "Service"))
    client.add(Rt(id = "5", name = "Five"), Rt(id = "7", name = "Thrift"))
    client.add(Rt(id = "6", name = "Six"), Rt(id = "8", name = "Service"))
    client.add(Rt(id = "6", name = "Six"), Rt(id = "7", name = "Thrift"))
    Thread.sleep(500)
    it("Вернуть по записи список тегов") {
      Thread.sleep(50)
      val lst1 = Await.result(client.listT(Rt(id = "1", name = "One")))
      lst1 should contain only (Rt(id = "1", name = "Hello"), Rt(id = "2", name = "Evgeniy"), Rt(id = "4", name = "Magic"))
      client.add(Rt(id = "2", name = "Two"), Rt(id = "1", name = "Hello"))
      client.add(Rt(id = "2", name = "Two"), Rt(id = "4", name = "Magic"))
      Thread.sleep(50)
      val lst2 = Await.result(client.listT(Rt(id = "2", name = "Two")))
      lst2 should contain only (Rt(id = "1", name = "Hello"), Rt(id = "4", name = "Magic"))

    }

    it("Не добавляет ли копии записей?") {
      Thread.sleep(65)

      client.add(Rt(id = "1", name = "One"), Rt(id = "1", name = "Hello"))
      client.add(Rt(id = "1", name = "One"), Rt(id = "2", name = "Evgeniy"))
      client.add(Rt(id = "1", name = "One"), Rt(id = "4", name = "Magic"))

      Thread.sleep(50)
      val lst = Await.result(client.listT(Rt(id = "1", name = "One")))
      lst should contain only (Rt(id = "1", name = "Hello"), Rt(id = "2", name = "Evgeniy"), Rt(id = "4", name = "Magic"))
    }

    it("Работа вывода по списку тэгов")
    {
      Thread.sleep(70)
      val lst = Await.result(client.listR(Seq(Rt(id = "1", name = "Hello"),Rt(id = "2", name = "Evgeniy"),Rt(id = "4", name = "Magic"))))
      lst should contain only (Rt(id = "1", name = "One"))
      Thread.sleep(50)
      val lst2 = Await.result(client.listR(Seq(Rt(id = "8", name = "Service"),Rt(id = "7", name = "Thrift"))))
      lst2 should contain only (Rt(id = "5", name = "Five"),Rt(id = "6", name = "Six"))
    }
    it("delete") {
      Thread.sleep(80)
      client.delete(Rt(id = "1", name = "One"),Rt(id = "4", name = "Magic"))
      Thread.sleep(85)
      val lst = Await.result(client.listT(Rt(id = "1", name = "One")))
      lst should contain only (Rt(id = "1", name = "Hello"), Rt(id = "2", name = "Evgeniy"))
      Thread.sleep(50)

    }
    describe("Ловим ошибки") {
      Thread.sleep(1000)
      it("Пытаемся вывести элемент которого нет") {
        Thread.sleep(50)
        (try {
          Await.result(client.listT(Rt(id = "3", name = "Three")))
          true
        } catch {
          case e: Throwable => false
        }) should be(false)
      }
      it("Пытаемся вывести элемент который удалили") {
        Thread.sleep(50)
        client.delete(Rt(id = "1", name = "One"), Rt(id = "2", name = "Evgeniy"))
        client.delete(Rt(id = "1", name = "One"), Rt(id = "1", name = "Hello"))
        Thread.sleep(50)
        (try {
          Await.result(client.listT(Rt(id = "1", name = "One")))
          true
        } catch {
          case e: Throwable => false
        }) should be(false)
      }
      it("Пытаемя удалить несуществующий элемент"){
        Thread.sleep(50)
        (try {
          Await.result(client.delete(Rt(id = "13", name = "213"), Rt(id = "13", name = "213")))
          true
        } catch {
          case e: Throwable => false
        }) should be(false)

        Thread.sleep(500)

      }
    }
  }
}

