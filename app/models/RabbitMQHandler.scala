package models

import com.rabbitmq.client._
import play.Play

/**
 * Created by Wojtek on 17/05/15.
 */

class RabbitMQHandler(exchangeName: String = "") {
  def publishMessage(routingKey: String, message: String) = {
    val sendingChannel  = RabbitMQHandler.connection.createChannel()
    sendingChannel.basicPublish(exchangeName, routingKey, null, message.getBytes)
    sendingChannel.close
  }
}

object RabbitMQHandler {
  lazy val connection: Connection = {
    val factory = new ConnectionFactory
    val rabbitUri = Play.application().configuration().getString("ampq.uri")
    factory.setUri(rabbitUri)
    factory.newConnection
  }

  def cleanup() = {
    connection match {
      case null => {
      }
      case _ => {
        connection.close()
      }
    }
  }
}

