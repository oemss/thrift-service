

package my.MyService

/**
  * Created by evgeniy on 13.08.16.
  */
object Functions {
  def myEqual(fst: Seq[Rt],snd: Seq[Rt]): Boolean = {
    snd.map(x => fst.find(v => v == x) match {
    case None => false
    case Some(vl) => true
  }).forall(x => x)
  }
}
