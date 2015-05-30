package services

import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jose.{Payload, JWSVerifier, JWSObject}
import net.minidev.json
import play.api.Play
import play.api.libs.json.{JsNull, JsValue}
import play.api.Play.current
import play.mvc.{Http, Security}

import scala.util.parsing.json.JSONObject

/**
 * Created by Wojtek on 29/05/15.
 */

class UserAuthenticationService {

  def verifyToken(jwtToken: String): Boolean = {
    val jwsObject: JWSObject = JWSObject.parse(jwtToken)
    val verifier: JWSVerifier = new MACVerifier(UserAuthenticationService.secret.getBytes)
    jwsObject.verify(verifier)
  }

  def getUserId(jwtToken: String): String = {
    val jwsObject: JWSObject = JWSObject.parse(jwtToken)
    val jsonObject: json.JSONObject = jwsObject.getPayload.toJSONObject
    println(jsonObject.get("userId").asInstanceOf[String])
    jsonObject.get("userId").asInstanceOf[String]
  }

  def getTokenPayload(jwtToken: String): JsValue ={
    JsNull
  }
}

object UserAuthenticationService {
  val secret: String = Play.application.configuration.getString("jwt.secret").get
}


