import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class DesktopController 
{ 
    public static void main(String[] args) 
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
} 
