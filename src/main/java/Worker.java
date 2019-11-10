import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Worker {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {
        System.out.println("Worker");

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection conexao = connectionFactory.newConnection();

        Channel channel = conexao.createChannel();
        channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String mensagem = new String (delivery.getBody (), "UTF-8");

            System.out.println ("[x] Recebido '" + mensagem + "'");
            try {
                doWork(mensagem);
            } finally {
                System.out.println ("[x] Feito");
            }
        };
        boolean autoAck = true;
        channel.basicConsume (TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});
    }

    private static void doWork (String task) {
        for (char ch: task.toCharArray ()) {
            if (ch == '.') {
                try {
                    Thread.sleep (1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
