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
    val requestToken = request.headers.get("Authorization").get

    val jwtToken = requestToken.split(" ")(1)
    if (tokenAuthentication.verifyToken(jwtToken)) {
      request.body.validate[CheckOutEvent].fold(
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

  def handleLocationEvent = Action(parse.json) { implicit request =>
    val requestToken = request.headers.get("Authorization").get

    val jwtToken = requestToken.split(" ")(1)
    if (tokenAuthentication.verifyToken(jwtToken)) {
      request.body.validate[LocationEvent].fold(
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

  def handleRatingEvent = Action(parse.json) { implicit request =>
    val requestToken = request.headers.get("Authorization").get

    val jwtToken = requestToken.split(" ")(1)
    if (tokenAuthentication.verifyToken(jwtToken)) {
      request.body.validate[RatingEvent].fold(
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

  def handleQrScanEvent = Action(parse.json) { implicit request =>
    val requestToken = request.headers.get("Authorization").get

    val jwtToken = requestToken.split(" ")(1)
    if (tokenAuthentication.verifyToken(jwtToken)) {
      request.body.validate[QrScanEvent].fold(
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
      (JsPath \ "latitude").read[Double] and
      (JsPath \ "longitude").read[Double]
    )(LocationEvent.apply _)

  implicit val locationEventWriter: Writes[LocationEvent] = (
      (JsPath \ "timestamp").write[Long] and
      (JsPath \ "latitude").write[Double] and
      (JsPath \ "longitude").write[Double]
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
      (JsPath \ "rating").read[Int]
    )(RatingEvent.apply _)

  implicit val ratingEventWriter: Writes[RatingEvent] = (
      (JsPath \ "timestamp").write[Long] and
      (JsPath \ "clubId").write[String] and
      (JsPath \ "rating").write[Int]
    )(unlift(RatingEvent.unapply))
}