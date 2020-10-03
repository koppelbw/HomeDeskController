import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  
import java.time.Duration;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetUp {
    public static void main(String[] args) {
        
        System.out.println("<--GetUp--> Initializing ...");

	final int sessionTime = 60;
	
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #21 as an output pin and turn on
        final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, "MyLED", PinState.HIGH);

		// provision gpio pin #22 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_22, PinPullResistance.PULL_UP);
        
        final GpioPinDigitalInput button_WhiteLight = gpio.provisionDigitalInputPin(RaspiPin.GPIO_23, PinPullResistance.PULL_UP);

        // set shutdown state for this pin
        //pin.setShutdownOptions(true, PinState.LOW);
        led.setShutdownOptions(true);
        
        led.pulse(100, true);
        try{Thread.sleep(110);}catch(InterruptedException e){}
        led.pulse(100, true);
        try{Thread.sleep(110);}catch(InterruptedException e){}
        led.pulse(100, true);
        try{Thread.sleep(110);}catch(InterruptedException e){}
        
        //System.out.println("--> GPIO state should be: ON");	   

		// create and register gpio pin listener
        myButton.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // toggle LED off if light is on
                if(event.getState().toString().equals("LOW"))
                {
					led.toggle();
				}
                
                //System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());                
            }
        });		
        
        button_WhiteLight.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // toggle LED off if light is on
                if(event.getState().toString().equals("LOW"))
                {
					Process p;
					try {
						List<String> cmdList = new ArrayList<String>();
						cmdList.add("/home/pi/Documents/Projects/DesktopController/googleassistant/lights_white.sh");
						
						ProcessBuilder pb = new ProcessBuilder(cmdList);
						p = pb.start();            
						p.waitFor(); 
						
						BufferedReader reader=new BufferedReader(new InputStreamReader(
						 p.getInputStream())); 
						 
						String line; 
						while((line = reader.readLine()) != null) { 
							System.out.println(line);
						} 
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}                
                //System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());                
            }
        });		

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
	LocalDateTime currentTime = LocalDateTime.now();
	LocalDateTime endTime = currentTime.plusMinutes(sessionTime);
	System.out.println("Session start time: " + dtf.format(currentTime) + "\n Ending time: " + dtf.format(endTime) + "\n Duration minutes: " + Duration.between(currentTime, endTime).toMinutes());
	while(true)
	{	
		currentTime = LocalDateTime.now();
		
		// reset session
		if(led.getState().toString().equals("HIGH"))
		{
			System.out.println("\n**********************************************************************\n");
			System.out.println("Session has been reset.\n");
			System.out.println("\n**********************************************************************\n");
			try{Thread.sleep(1000);}catch(InterruptedException e){}
			led.pulse(100, true);
			try{Thread.sleep(110);}catch(InterruptedException e){}
			led.pulse(100, true);
			try{Thread.sleep(110);}catch(InterruptedException e){}
			led.pulse(100, true);
			try{Thread.sleep(110);}catch(InterruptedException e){}
			
			led.low();
			currentTime = LocalDateTime.now();
			endTime = currentTime.plusMinutes(sessionTime);
			System.out.println("Session start time: " + dtf.format(currentTime) + "\n Ending time: " + dtf.format(endTime) + "\n Duration minutes: " + Duration.between(currentTime, endTime).toMinutes());
		}
		
		// session timeout
		if(currentTime.compareTo(endTime) > 0)
		{
			if(led.getState().toString().equals("LOW"))
			{
				System.out.println("\n**********************************************************************\n");
				System.out.println("Session has expired. Please press the button to start a new session.");
				System.out.println("\n**********************************************************************\n");
				led.high();
			}
			
			while(led.getState().toString().equals("HIGH"))
			{
				try{Thread.sleep(500);}catch(InterruptedException e){}
			}
			
			currentTime = LocalDateTime.now();
			endTime = currentTime.plusMinutes(sessionTime);
			System.out.println("Session start time: " + dtf.format(currentTime) + "\n Ending time: " + dtf.format(endTime) + "\n Duration minutes: " + Duration.between(currentTime, endTime).toMinutes());
		}
		
		try{Thread.sleep(500);}catch(InterruptedException e){}
	}

        // turn off gpio pin #01
       // pin.low();
      //  System.out.println("--> GPIO state should be: OFF");

       // try{Thread.sleep(5000);}catch(InterruptedException e){}

        // toggle the current state of gpio pin #01 (should turn on)
      //  pin.toggle();
      //  System.out.println("--> GPIO state should be: ON");

       // try{Thread.sleep(5000);}catch(InterruptedException e){}

        // toggle the current state of gpio pin #01  (should turn off)
      //  pin.toggle();
       // System.out.println("--> GPIO state should be: OFF");

      //  try{Thread.sleep(5000);}catch(InterruptedException e){}

        // turn on gpio pin #01 for 1 second and then off
      //  System.out.println("--> GPIO state should be: ON for only 1 second");
       // pin.pulse(1000, true); // set second argument to 'true' use a blocking call

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
       // gpio.shutdown();

        //System.out.println("Exiting ControlGpioExample");
    }
}
