package my.MyService

import com.twitter.util.{Await, Future}
import my.MyService._
import scala.collection.mutable.{Map,
SynchronizedMap, HashMap}
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
  var db: concurrent.Map[Rt,Seq[Rt]] = new ConcurrentHashMap[Rt,Seq[Rt]]

  var tag: Seq[Rt] = Seq()
  var rec: Seq[Rt] = Seq()

  override def add(idR: String, idT: String): Future[Unit] = {
    val fst = Functions.find(idR, rec)
      val snd = Functions.find(idT, tag)

      (fst, snd) match {
        case (Some(f), Some(s)) => {
          println(s"fst =  $fst")
          println(s"snd =  $snd")
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
        case (_,_) => throw new Exception("Add: Not found in records or/and tags")
      }
    Future.Unit
  }

  override def delete(idR: String, idT: String): Future[Unit] = {
      println("SDA")
      val found = Functions.find(idR, rec)
      found match {
        case Some(value) => {
          val localFound = db.get(value)
          localFound match {
            case None => throw new Exception("Delete: Not found in database")
            case Some(v: Seq[Rt]) => {
              db.remove(value)
              val chk = v.filter(x => x._1 != idT)
              chk match {
                case Seq() =>
                case _ => db.put(value, chk)
              }
            }
          }
        }
        case None =>
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
    val keySeq: Seq[Rt] = db.filter(w => w._2.map(x => x._1).canEqual(lstT)).keySet.toSeq
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
    println(s"listT $idR" + db)
      val found = Functions.find(idR, rec)
      println(s"found = $found")
      found match {
        case Some(value: Rt) =>
          println(s"basa = $db")
          val hlpval = db.get(value)
          hlpval match {
            case Some(v: Seq[Rt]) =>
              println(s"v = $v")
              Future(v)
            case _ => throw new Exception("ListT: Not found in database")
          }
        case _ => throw new Exception("ListT: Not found " + idR + "in records")
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
