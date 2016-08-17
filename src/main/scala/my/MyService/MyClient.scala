package my.MyService

import com.twitter.finagle.Thrift
import com.twitter.util.{Await, Future}

/**
  * Created by evgeniy on 13.08.16.
  */
object MyClient {
  val client = Thrift.client.newIface[MyServ[Future]]("127.0.0.1:6666")

  def startclient(): Future[Unit] = {


    Future.Unit
  }
}
