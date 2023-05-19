package ch.hearc.topazlion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.hearc.topazlion.model.HttpResponse;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;

@SpringBootApplication
public class TopazLionApplication {

	static private HttpResponse lastMediaResponse = null;

	@Autowired
	private ObjectMapper mapper;

	public static void main(String[] args) {
		SpringApplication.run(TopazLionApplication.class, args);
		System.out.println("Hello World!");
	}

	/**
	 * JMS listener for media response JSON messages
	 * @param jsonMessage
	 * @throws JMSException
	 */
	@JmsListener(destination = "${spring.activemq.media-response-json-queue}")
    public void readInprogressJsonMessage(final Message jsonMessage) throws JMSException {

        System.out.println("Received message " + jsonMessage);
        
        if(!(jsonMessage instanceof TextMessage)) {
			return;
		}

		TextMessage textMessage = (TextMessage)jsonMessage;
		String jsonString = textMessage.getText();

		System.out.println("--------------------");
		System.out.println("Received: " + jsonString);
		System.out.println("--------------------");

		try {
			HttpResponse httpResponse = mapper.readValue(jsonString, HttpResponse.class);
			
			System.out.println("HTTP response from microservice:");
			System.out.println("- id: " + httpResponse.getId());
			System.out.println("- status: " + httpResponse.getStatus());
			System.out.println("- message: " + httpResponse.getMessage());

			// Save the last media response
			lastMediaResponse = httpResponse;
			
		} catch (Exception e) {
			System.err.println("Error while parsing JSON message to HttpResponse object");
		}
	}

	public static HttpResponse getLastMediaResponse() {
		return lastMediaResponse;
	}
}
