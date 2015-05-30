import com.google.inject.{Guice, Injector}
import play.api._

/**
 * Created by Wojtek on 29/05/15.
 */
object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
  }
}
