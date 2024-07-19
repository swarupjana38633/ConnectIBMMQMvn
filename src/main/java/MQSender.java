import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

/*
Below are the details as requested. LA Non Prod server IP – 10.80.3.162
Queue Manager Name  :-             GROUP_INSPIRE
Listener Name       :-             GAINSPIRELSNR
Channel Name        :-             GAINSPIRECHNL
Port                :-             7786
*/
public class MQSender {

    public static void main(String[] args) {
        // Connection parameters
        String host = "localhost";
        int port = 1414; // default port
        //String host = "10.80.3.162";
        //int port = 7786; // default port
        String channel = "DEV.ADMIN.SVRCONN";
        String queueManagerName = "QM1";
        String queueName = "DEV.QUEUE.1";
        String user = "admin";
        String password = "passw0rd";

        try {
            // Create a connection factory
            MQQueueConnectionFactory connectionFactory = new MQQueueConnectionFactory();
            connectionFactory.setHostName(host);
            connectionFactory.setPort(port);
            connectionFactory.setChannel(channel);
            connectionFactory.setQueueManager(queueManagerName);
            connectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            
            // Optional: Set user credentials if required
            connectionFactory.setStringProperty(WMQConstants.USERID, user);
            connectionFactory.setStringProperty(WMQConstants.PASSWORD, password);
            
            // Create a connection
            QueueConnection connection = connectionFactory.createQueueConnection(user, password);
            connection.start();
            
            // Create a session
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            
            // Create the queue
            Queue queue = session.createQueue(queueName);
            
            // Create a message producer
            MessageProducer producer = session.createProducer(queue);
            
            // Create a message
            TextMessage message = session.createTextMessage("Hello, World!");
            
            // Send the message
            producer.send(message);
            
            System.out.println("Message sent successfully!");
            
            // Clean up
            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
