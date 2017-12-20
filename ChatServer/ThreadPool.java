import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * Created by angjelinmalaj on 11/26/17.
 */

public class ThreadPool extends HashMap {

    // Get Client
    public ServerClient getClient(String client){
        ServerClient clientThread = (ServerClient) this.get(client);
        if(clientThread != null) return clientThread;
        else return null;
    }

    // Send to all minus the client that initiated the request
    public void sendToAllMinusSender(String client, Object[] data){
        Map<String, ServerClient> map = this;

        Iterator<Entry<String, ServerClient>> entries = map.entrySet().iterator();
        while(entries.hasNext()) {
            Map.Entry<String, ServerClient> entry = entries.next();
            ServerClient nextClient = entry.getValue();
            if(!nextClient.name.equals(client))
                nextClient.sendData(data);
        }
    }

    // Get connected clients sends a message to the new client to add all other clients
    public void getConnectedClients(ServerClient client){
        Map<String, ServerClient> map = this;
        Iterator<Map.Entry<String, ServerClient>> entries = map.entrySet().iterator();
        while(entries.hasNext()){
            Map.Entry<String, ServerClient> entry = entries.next();
            ServerClient nextClient = entry.getValue();
            if(!nextClient.name.equals(client.name)){
                Object[] data = { ServerInterface.Client.ADD, nextClient.name, nextClient.ip, nextClient.port };
                client.sendData(data);
            }
        }
    }
}