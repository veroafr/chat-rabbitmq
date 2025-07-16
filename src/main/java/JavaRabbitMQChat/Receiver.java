package JavaRabbitMQChat;

import com.rabbitmq.client.*;

public class Receiver {
    public static void receive(String queueName, MessageHandler handler) throws Exception {
        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            handler.handle(message);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    public interface MessageHandler {
        void handle(String message);
    }
}