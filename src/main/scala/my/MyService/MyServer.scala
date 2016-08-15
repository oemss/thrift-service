package my.MyService

import com.twitter.util.{Await, Future}
import my.MyService.{MyServ, Rt}
import java.util.concurrent.ConcurrentHashMap
import scala.collection.mutable.{Map,
SynchronizedMap, HashMap}
import scala.collection.JavaConversions._
import scala.collection.mutable
import collection.JavaConverters._
import scala.collection._
import scala.collection.convert.decorateAsScala._
import scala.collection.mutable.{HashMap, Map, SynchronizedMap}

/**
  * Created by evgeniy on 13.08.16.
  */
class MyServer extends MyServ[Future] {
  var db = new mutable.HashMap[Rt,Seq[Rt]]
  var tag: Seq[Rt] = Seq()
  var rec: Seq[Rt] = Seq()

  override def add(idR: String, idT: String): Future[Unit] = {
    //println(rec)
    //println(tag)
    db.synchronized {
      val fst = Functions.find(idR, rec)
      val snd = Functions.find(idT, tag)
      (fst, snd) match {
        case (Some(f), Some(s)) => {
          val dbValue = db.get(f)
          dbValue match {
            case Some(v: Seq[Rt]) => {
              v.find(x => x == s ) match {
                case Some(value) =>
                case None => db.put(f, v ++ Seq(s))
              }
            }
            case None => db.put(f,Seq(s))
          }
        }
        case _ => throw new Exception("Add: Not found in records or/and tags")
      }
    }
    Future.Unit
  }

  override def delete(idR: String, idT: String): Future[Unit] = {

    db.synchronized {
      println("SDA")
      val found = Functions.find(idR, rec)
      found match {
        case Some(value) => {
          val localFound = db.get(value)
          localFound match {
            case null => throw new Exception("Delete: Not found in database")
            case Some(v: Seq[Rt]) => {
              db.remove(value)
              db.put(value, v.filter(x => x._1 != idT))
            }
          }
        }
        case None =>
      }
    }
    println("Delete " + db)
    Future.Unit
  }

  /**
    * Возвращает список имен записей по списку тэгов
    *
    * @param lstT
    * @return
    */
  override def listR(lstT: Seq[String]): Future[Seq[Rt]] = {
    //println(db)
    val keySeq: Seq[Rt] = db.filter(w => w._2.map(x => x._1).canEqual(lstT)).keySet.toSeq
    //println(keySeq)
    keySeq match {
      case Seq()=> throw new Exception("ListR: Not found in database")
      case vl: Seq[Rt] => Future(keySeq)
    }
  }

  /**
    * Возвращает список тэгов по id записи
    *
    * @param idR
    * @return
    */
  override def listT(idR: String): Future[Seq[Rt]] = {
    println("listT " + db)
    db.synchronized {
      val found = Functions.find(idR, rec)
      found match {
        case Some(value: Rt) =>
          val hlpval = db.get(value)
          hlpval match {
            case Some(v: Seq[Rt]) => Future(v)
            case _ => throw new Exception("ListT: Not found in database")
          }
        case _ => throw new Exception("ListT: Not found " + idR + "in records")
      }
    }
  }

  /**
    * Функция для тестирования
    * @param where
    * @param wt
    * @return
    */
  override def put(where: Short, wt:Seq[Rt]): Future[Unit] = {
    println("recc = " + wt)
    println("idrr = " + where)
    where match {
        case 0 => rec ++= wt
        case 1 => tag ++= wt
        case _ => throw new Exception("What?")
      }
    Future.Unit
  }


}
