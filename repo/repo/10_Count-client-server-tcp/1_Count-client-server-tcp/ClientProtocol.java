import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ClientProtocol {
    
    BufferedReader user = new BufferedReader(new InputStreamReader(System.in));

    public String prepareRequest() throws IOException {
        System.out.println("steile akeraio arithmo gia na ipologisw p: ");
        String input = user.readLine();




        return input;
    }

    public void processReply(String reply) throws IOException{
        System.out.println("processReply message received from server: " + reply);
    }
}
