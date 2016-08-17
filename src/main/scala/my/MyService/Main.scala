package my.MyService

import com.twitter.finagle.Thrift
import com.twitter.util.Await


object Main extends App {

  val service = Thrift.server.serveIface("127.0.0.1:6666", new MyServer)
  //val client = MyClient.startclient()
  Await.result(service)

}