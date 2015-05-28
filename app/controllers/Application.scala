package controllers

import models._
import play.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import play.libs.Akka

object Application extends Controller {

  import RabbitMQHandler.RoutingKey._

  val rmq = new RabbitMQHandler

  def index = Action {
    rmq.publishMessage("", "Hello World!")
    Ok(views.html. index("Your new application is ready."))
  }

  def handleCheckInEvent = Action { implicit request =>
    checkinForm.bindFromRequest.fold(
      formWithError => {
        Logger.error(s"CheckInForm with error.")
        BadRequest("Invalid Json body.")
      },
      eventBody => {
        rmq.publishMessage(CHECKIN_EVENT, eventBody.toString)
        Logger.debug(s"${eventBody}")
        Ok
      }
    )
  }

  def handleCheckOutEvent = Action { implicit request =>
    checkoutForm.bindFromRequest.fold(
      formWithError => {
        Logger.error(s"CheckOutForm error.")
        BadRequest("Invalid Json body.")
      },
      eventBody => {
      rmq.publishMessage(CHECKOUT_EVENT, eventBody.toString)
      Logger.debug(s"${eventBody}")
      Ok
      }
    )
  }

  def handleLocationEvent = Action { implicit request =>
    locationForm.bindFromRequest.fold(
      formWithError => {
        Logger.error(s"LocationForm error.")
        BadRequest("Invalid Json body.")
      },
      eventBody => {
        rmq.publishMessage(LOCATION_EVENT, eventBody.toString)
        Logger.debug(s"${eventBody}")
        Ok
      }
    )
  }

  def handleRatingEvent = Action { implicit request =>
    ratingForm.bindFromRequest.fold(
      formWithError => {
        Logger.error(s"RatingForm error.")
        BadRequest("Invalid Json body.")
      },
      eventBody => {
        rmq.publishMessage(RATING_EVENT, eventBody.toString)
        Logger.debug(s"${eventBody}")
        Ok
      }
    )
  }

  def handleQrScanEvent = Action { implicit request =>
    qrScanForm.bindFromRequest.fold(
      formWithError => {
        Logger.error(s"QRScanForm error.")
        BadRequest("Invalid Json body.")
      },
      eventBody => {
        rmq.publishMessage(QRSCAN_EVENT, eventBody.toString)
        Logger.debug(s"${eventBody}")
        Ok
      }
    )
  }

  def handleTestRequest = Action {
    Ok("Received test message")
  }

  var checkinForm = Form(
    mapping(
      "userId"    -> optional(text),
      "timestamp" -> longNumber,
      "clubId"    -> text
    )(CheckInEvent.apply)(CheckInEvent.unapply)
  )

  var checkoutForm = Form(
    mapping(
      "userId"    -> optional(text),
      "timestamp" -> longNumber,
      "clubId"    -> nonEmptyText
    )(CheckOutEvent.apply)(CheckOutEvent.unapply)
  )

  var locationForm = Form(
    mapping(
      "userId"    -> optional(text),
      "timestamp" -> longNumber,
      "latitude"  -> nonEmptyText,
      "longitude" -> nonEmptyText
    )(LocationEvent.apply)(LocationEvent.unapply)
  )

  var qrScanForm = Form(
    mapping(
      "userId"    -> optional(text),
      "timestamp" ->  longNumber,
      "clubId"    ->  nonEmptyText,
      "rating"    ->  nonEmptyText
    )(QrScanEvent.apply)(QrScanEvent.unapply)
  )

  var ratingForm = Form(
    mapping(
      "userId"    -> optional(text),
      "timestamp" -> longNumber,
      "clubId"    -> nonEmptyText,
      "rating"    -> nonEmptyText
    )(RatingEvent.apply)(RatingEvent.unapply)
  )
}