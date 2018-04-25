package com.cooksys.handler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Destination;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.model.Flight;
import com.cooksys.wrapper.FlightWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * This class gets messages via JMS.  If a message is received, it is
 * stored until called upon to release all messages.
 * 
 * @author Jason Byrd
 */

@Component
@Scope("singleton")
public class FlightJMS
{
	private Gson gson = new Gson();
	private Session session;
	private static FlightJMS instance;
	private List<FlightWrapper> flights = new ArrayList<FlightWrapper>();

	/**
	 * Maintains a single instance of FlightJMS.
	 * 
	 * @return FlightJMS instance
	 */
	public static FlightJMS getInstance()
	{
		if(instance == null)
			instance = new FlightJMS();
		System.out.println("JMS Started");
		return instance;
	}
	
	/**
	 * Private constructor used to receive a JMS message, convert it from
	 * TexMessage to Json string, and send it to the web sockets OnMessage method.
	 */
	private FlightJMS() {
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			Connection connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destionation = session.createTopic("FlightUpdate");
			MessageConsumer consumer = session.createConsumer(destionation);
			
			// Listens for a new message to be sent
			consumer.setMessageListener(new MessageListener() 
			{
				// Handles message when a new message is received
				@Override
				public void onMessage(Message message)
				{
					try
					{
						if (message instanceof TextMessage)
						{
							TextMessage textMessage = (TextMessage) message;
							
							// Gets string values from TextMessage
							String flightsText = textMessage.getText();
							String flightStatus = textMessage.getStringProperty("FlightStatus");
							
							if (flightStatus.equals("Flight Arrived"))
							{
								// A single flight is converted from Json text and put in 
								// Flightwrapper and marked as arrived and adds to stored flights
								Flight flight = gson.fromJson(flightsText, Flight.class);
								flights.add(new FlightWrapper(flight, false));
							}
							else
							{
								// Gets type for List<Flight> and converts Json text to object
								Type listType = new TypeToken<ArrayList<Flight>>(){}.getType();
								List<Flight> delayedFlights = gson.fromJson(flightsText, listType);
								
								// For each flight, marks as delayed and adds to flights
								for (Flight flight : delayedFlights)
								{
									flights.add(new FlightWrapper(flight, true));
								}
							}
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e){ }
	}
	
	/**
	 * Returns all the saved flightWrappers and clears the stored list.
	 * 
	 * @return FlightWrapper list with flights marked as delayed or arrived
	 */
	public List<FlightWrapper> getAlerts()
	{
		List<FlightWrapper> flightsProxy = new ArrayList<FlightWrapper>(flights);
		flights.clear();
		return flightsProxy;
	}
}