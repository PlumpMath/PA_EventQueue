package controllers

import models._
import play.Logger
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._
import services.{RabbitMQHandler, UserAuthenticationService, BasicAuthenticationService}

object Application extends Controller {

  import Message._
  import RabbitMQHandler.RoutingKey._

  val rmq = new RabbitMQHandler
  val tokenAuthentication: UserAuthenticationService = new UserAuthenticationService

  def index = Action {
    rmq.publishMessage("", "Hello World!")
    Ok(views.html. index("Your new application is ready."))
  }

  def handleCheckInEvent = Action(parse.json) { implicit request =>
    val requestToken = request.headers.get("Authorization").get

    val jwtToken = requestToken.split(" ")(1)
    if (tokenAuthentication.verifyToken(jwtToken)) {
        request.body.validate[CheckInEvent].fold(
          invalidBody => {
            Logger.error("CheckInForm with error.")
            BadRequest("Invalid Json body.")
          },
          validBody => {
            var extendedBody = Json.toJson(validBody).as[JsObject]
            val userId = tokenAuthentication.getUserId(jwtToken)
            extendedBody += ("userId", JsString(userId))
            rmq.publishMessage(CHECKIN_EVENT, extendedBody.toString)
            Logger.debug(s"${extendedBody.toString}")
            Ok("Event enqueued.")
          }
        )
       } else {
      BadRequest("Invalid token.")
    }
  }

  def handleCheckOutEvent = Action(parse.json) { implicit request =>
    Json.fromJson[CheckOutEvent](request.body).fold(
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

  def handleLocationEvent = Action(parse.json) { implicit request =>
    Json.fromJson[LocationEvent](request.body).fold(
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

  def handleRatingEvent = Action(parse.json) { implicit request =>
    Json.fromJson[RatingEvent](request.body).fold(
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

  def handleQrScanEvent = Action(parse.json) { implicit request =>
    Json.fromJson[QrScanEvent](request.body).fold(
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

  /*
    CASE CLASS TO JSON CONVERTERS
   */
  // CheckOutEvent
  implicit val checkoutEventReader: Reads[CheckOutEvent] = (
      (JsPath \ "timestamp").read[Long] and
      (JsPath \ "clubId").read[String]
    )(CheckOutEvent.apply _)

  implicit val checkoutEventWriter: Writes[CheckOutEvent] = (
      (JsPath \ "timestamp").write[Long] and
      (JsPath \ "clubId").write[String]
    )(unlift(CheckOutEvent.unapply))

  //CheckInEvent
  implicit val checkinEventReader: Reads[CheckInEvent] = (
      (JsPath \ "timestamp").read[Long] and
      (JsPath \ "clubId").read[String]
    )(CheckInEvent.apply _)

  implicit val checkinEventWriter: Writes[CheckInEvent] = (
      (JsPath \ "timestamp").write[Long] and
      (JsPath \ "clubId").write[String]
    )(unlift(CheckInEvent.unapply))

  //LocationEvent
  implicit val locationEventReader: Reads[LocationEvent] = (
      (JsPath \ "timestamp").read[Long] and
      (JsPath \ "latitude").read[String] and
      (JsPath \ "longitude").read[String]
    )(LocationEvent.apply _)

  implicit val locationEventWriter: Writes[LocationEvent] = (
      (JsPath \ "timestamp").write[Long] and
      (JsPath \ "latitude").write[String] and
      (JsPath \ "longitude").write[String]
    )(unlift(LocationEvent.unapply))

  //QrScanEvent
  implicit val qrscanEventReader: Reads[QrScanEvent] = (
      (JsPath \ "timestamp").read[Long] and
      (JsPath \ "clubId").read[String] and
      (JsPath \ "payload").read[String]
    )(QrScanEvent.apply _)

  implicit val qrscanEventWriter: Writes[QrScanEvent] = (
      (JsPath \ "timestamp").write[Long] and
      (JsPath \ "clubId").write[String] and
      (JsPath \ "payload").write[String]
    )(unlift(QrScanEvent.unapply))

  //RatingEvent
  implicit val ratingEventReader: Reads[RatingEvent] = (
      (JsPath \ "timestamp").read[Long] and
      (JsPath \ "clubId").read[String] and
      (JsPath \ "rating").read[String]
    )(RatingEvent.apply _)

  implicit val ratingEventWriter: Writes[RatingEvent] = (
      (JsPath \ "timestamp").write[Long] and
      (JsPath \ "clubId").write[String] and
      (JsPath \ "rating").write[String]
    )(unlift(RatingEvent.unapply))
}