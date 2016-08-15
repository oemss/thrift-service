

package my.MyService

/**
  * Created by evgeniy on 13.08.16.
  */
object Functions {
  def find(id: String,list: Seq[Rt]): Option[Rt] = {
    list.find(wa => wa._1 == id)
  }

}
