import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class NewTask {
    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception{

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);

        try (
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()){

            channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
            String mensagem = String.join("", argv);
            channel.basicPublish("", "olá", null, mensagem.getBytes());
            System.out.println("[x] Enviado '" + mensagem + "'");
        }
    }
}
