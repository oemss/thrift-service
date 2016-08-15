package my.MyService

import com.twitter.finagle.Thrift
import org.scalatest._
import com.twitter.util.{Await, Future}
import org.scalatest.{FunSpec, Matchers}
import Matchers._

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
//    it("add") {
//      for {
//        _ <- client.add("1", "1")
//        _ <- client.add("1", "2")
//        _ <- client.add("1", "4")
//      } yield ()
//    }
//    it("Вернуть по записи список тегов") {
//      //val expectedSeq = (Rt(id = "1", name = "Hello"), Rt(id = "2", name = "Evgeniy"), Rt(id = "4", name = "Magic"))
//      val lst = Await.result(client.listT("1"))
//      lst should contain only (Rt(id = "1", name = "Hello"), Rt(id = "2", name = "Evgeniy"), Rt(id = "4", name = "Magic"))
////      lst match {
////        case Some(seq: Future[Seq[Rt]]) =>
////          throw new Exception
////        case Some(er: Future[Exception]) =>
////      }
//    }
//    it("addCopy") {
//      for {
//        _ <- client.add("1", "1")
//        _ <- client.add("1", "2")
//        _ <- client.add("1", "4")
//      } yield ()
//    }
//    it("Не добавляет ли копии записей?") {
//      //val expectedSeq = Seq(Rt(id = "1", name = "Hello"), Rt(id = "2", name = "Evgeniy"), Rt(id = "4", name = "Magic"))
//      val lst = Await.result(client.listT("1"))
//      lst should contain only (Rt(id = "1", name = "Hello"), Rt(id = "2", name = "Evgeniy"), Rt(id = "4", name = "Magic"))
//    }
    it("delete") {
      for {
        _ <- client.add("1", "1")
        _ <- client.add("1", "2")
        _ <- client.add("1", "4")
        _ <- client.delete("1","4")
      } yield ()
      val lst = Await.result(client.listT("1")).sortBy(x => x._1)
      lst should contain only (Rt(id = "1", name = "Hello"), Rt(id = "2", name = "Evgeniy"))
    }
    it("Проверка удаления") {

    }
//    it("Работа вывода по списку тэгов")
//    {
//      val expectedSeq = Seq(Rt(id = "1", name = "One"))
//      val lst = Await.result(client.listR(Seq("1","2")))
//      lst should contain only expectedSeq.toBuffer
//    }
//    it("all close") = {
//
//    }
  }
}

