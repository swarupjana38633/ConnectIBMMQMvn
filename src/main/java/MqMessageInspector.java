import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class MqMessageInspector {

    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        try {
            // Create a connection factory
            MQConnectionFactory mqConnectionFactory = new MQConnectionFactory();
            mqConnectionFactory.setHostName("localhost");
            mqConnectionFactory.setPort(1414);
            mqConnectionFactory.setQueueManager("QM1");
            mqConnectionFactory.setChannel("DEV.ADMIN.SVRCONN");
            mqConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);

            // Create a connection
            connection = mqConnectionFactory.createConnection("admin", "passw0rd");
            connection.start();

            // Create a session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create a destination (queue)
            Destination destination = session.createQueue("DEV.QUEUE.1");

            // Create a message consumer
            MessageConsumer consumer = session.createConsumer(destination);

            // Receive a message
            Message message = consumer.receive(1000);

            // Check if a message was received
            if (message != null) {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println("Received message: " + textMessage.getText());
                }

                // Print message properties
                String jmsType = message.getJMSType();
                String jmsFormat = message.getStringProperty("JMS_IBM_Format");
                System.out.println("JMSType: " + jmsType);
                System.out.println("JMS_IBM_Format: " + jmsFormat);
            } else {
                System.out.println("No message received");
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            // Clean up resources
            try {
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
