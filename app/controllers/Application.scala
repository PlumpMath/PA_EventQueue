package controllers

import models._
import play.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import play.libs.Akka

object Application extends Controller {
  val rmq = new RabbitMQHandler

  def index = Action {
    rmq.publishMessage("", "Hello World!")
    Ok(views.html. index("Your new application is ready."))
  }

  def handleCheckInEvent = Action { implicit request =>
    val checkInEvent = checkinForm.bindFromRequest.get
    Logger.debug(s"${checkInEvent}")
    Ok("Got it!")
  }

  def handleCheckOutEvent = Action { implicit request =>
    val checkOutEvent = checkoutForm.bindFromRequest.get
    Ok("Got it!")
  }

  def handleLocationEvent = Action { implicit request =>
    val locationEvent = locationForm.bindFromRequest.get
    Ok("Got it!")
  }

  def handleRatingEvent = Action { implicit request =>
    val ratingEvent = ratingForm.bindFromRequest.get
    Ok("Got it!")
  }

  def handleQrScanEvent = Action { implicit request =>
    val qrscanEvent = qrScanForm.bindFromRequest.get
    Ok("Got it!")
  }

  def handleTestRequest = Action {
    Ok("Received test message")
  }

  var checkinForm = Form(
    mapping(
      "timestamp" -> longNumber,
      "clubId"    -> text
    )(CheckInEvent.apply)(CheckInEvent.unapply)
  )

  var checkoutForm = Form(
    mapping(
      "timestamp" -> longNumber,
      "clubId"    -> nonEmptyText
    )(CheckOutEvent.apply)(CheckOutEvent.unapply)
  )

  var locationForm = Form(
    mapping(
      "timestamp" -> longNumber,
      "latitude"  -> nonEmptyText,
      "longitude" -> nonEmptyText
    )(LocationEvent.apply)(LocationEvent.unapply)
  )

  var qrScanForm = Form(
    mapping(
      "timestamp" ->  longNumber,
      "clubId"    ->  nonEmptyText
    )(QrScanEvent.apply)(QrScanEvent.unapply)
  )

  var ratingForm = Form(
    mapping(
      "timestamp" -> longNumber,
      "clubId"    -> nonEmptyText,
      "rating"    -> nonEmptyText
    )(RatingEvent.apply)(RatingEvent.unapply)
  )
}