package my.MyService

import com.twitter.util.{Await, Future}
import my.MyService._

import scala.collection.mutable.{HashMap, Map, SynchronizedMap}
import scala.collection._
import scala.collection.convert.decorateAsScala._
import scala.collection.JavaConversions._
import scala.collection.mutable
import collection.JavaConverters._
import scala.collection._
import scala.collection.convert.decorateAsScala._
import scala.collection.mutable.{HashMap, Map, SynchronizedMap}
import java.util.concurrent.ConcurrentHashMap
/**
  * Created by evgeniy on 13.08.16.
  */
class MyServer extends MyServ[Future] {
  var db: concurrent.Map[Rt,Seq[Rt]] = new ConcurrentHashMap[Rt, Seq[Rt]]

  override def add(idR: Rt, idT: Rt): Future[Unit] = {
    db.putIfAbsent(idR,Seq(idT)) match {
      case Some(res) => db.put(idR, res ++ Seq(idT))
    }
    Future.Unit
  }

  override def delete(idR: Rt, idT: Rt): Future[Unit] = {
    db.get(idR) match {
      case Some(result) =>
        result.contains(idT) match {
          case true => result.filter(x => x != idT) match {
            case Seq() => db.remove(idR)
            case value => db.put(idR, value)
          }
          case false => throw new Exception("Delete: Not found tag in database")
        }
      case None => throw new Exception("Delete: Not found record in database")
    }
    Future.Unit
  }

  /**
    * Возвращает список имен записей по списку тэгов
    *
    * @param lstT
    * @return
    */
  override def listR(lstT: Seq[Rt]): Future[Seq[Rt]] = {
    println(db)
    db.filter(w => Functions.myEqual(w._2,lstT)).keySet.toSeq match {
      case vl: Seq[Rt] => Future(vl)
      case Seq()=> throw new Exception("ListR: Not found in database")
    }
  }

  /**
    * Возвращает список тэгов по записи
    *
    * @param idR
    * @return
    */
  override def listT(idR: Rt): Future[Seq[Rt]] = {
    db.get(idR) match {
      case Some(result) => Future(result)
      case _ => throw new Exception("ListT: Not found in database")
    }
  }
}
