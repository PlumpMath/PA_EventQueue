package services

import play.api.libs.json.JsValue

/**
 * Created by Wojtek on 29/05/15.
 */
trait BasicAuthenticationService {
  def verifyToken(jwtToken: String): Boolean
  def getTokenPayload(jwtToken: String): JsValue
  def getUserId(jwtToken: String): String
}
