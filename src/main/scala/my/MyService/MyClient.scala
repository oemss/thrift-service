package my.MyService

import com.twitter.finagle.Thrift
import com.twitter.util.{Await, Future}

/**
  * Created by evgeniy on 13.08.16.
  */
object MyClient {
  val client = Thrift.client.newIface[MyServ[Future]]("127.0.0.1:6666")

  def startclient(): Future[Unit] = {


    var rty = Rt(
      id = "1", name = "Hello"
    )
    //client.put(1,rty)
    rty = Rt(
      id = "2", name = "World"
    )
    //client.put(1,rty)
    rty = Rt(
      id = "1", name = "Evgeny"
    )
    //client.put(0,rty)
    rty = Rt(
      id = "2", name = "Magic"
    )
    //client.put(0,rty)
    Thread.sleep(2000)

    client.add("1", "1")
    //Await.ready(client.add("2","2"))
    client.add("1", "2")
    //Thread.sleep(2000)

    //client.listT("1")
    //client.delete("1","2")
    //client.listT("1")
    println(Await.result(client.listT("1")))

    //      .foreach(sas => println(sas))
    //client.listT("3")
    Future.Unit
  }
}
