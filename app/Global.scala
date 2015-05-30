import play.api._
import play.api.mvc._
import play.api.http.HeaderNames._
import play.api.libs.concurrent.Execution.Implicits._


/**
 * Created by Wojtek on 29/05/15.
 */
object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
  }

  //     _______ _________          _______  _______
  //    (  ____ \\__   __/|\     /|(       )(  ____ \
  //    | (    \/   ) (   ( \   / )| () () || (    \/
  //    | (__       | |    \ (_) / | || || || (__
  //    |  __)      | |     ) _ (  | |(_)| ||  __)
  //    | (         | |    / ( ) \ | |   | || (
  //    | )      ___) (___( /   \ )| )   ( || (____/\
  //    |/       \_______/|/     \||/     \|(_______/
  //FIXME make it safer
  override def doFilter(action: EssentialAction): EssentialAction = EssentialAction { request =>
    action.apply(request).map(_.withHeaders(
      ACCESS_CONTROL_ALLOW_ORIGIN -> "*"
    ))
  }
}
