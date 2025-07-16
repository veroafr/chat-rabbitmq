package JavaRabbitMQChat;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Sender {
    public static void send(String queueName, String message) throws Exception {
        try (Connection conn = RabbitMQConnection.getConnection();
             Channel channel = conn.createChannel()) {
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicPublish("", queueName, null, message.getBytes("UTF-8"));
        }
    }
}