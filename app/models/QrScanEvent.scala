package models

/**
 * Created by Wojtek on 16/05/15.
 */
case class QrScanEvent(userId: Option[String], timestamp: Long, clubId: String, payload: String) {

}
