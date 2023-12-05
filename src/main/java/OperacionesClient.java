import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class OperacionesClient {

    // Método para crear un client
    public static void crearClient(Client client) {
        Logger logger = LoggerFactory.getLogger("org.mongodb.driver");

        // String uri = "mongodb://usuario:password@host:puerto";
        String uri = "mongodb://alvrub:qwerty1234@ec2-54-146-188-92.compute-1.amazonaws.com:27017";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
            CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

            MongoDatabase database = mongoClient.getDatabase("alvrub").withCodecRegistry(pojoCodecRegistry);
            MongoCollection<Client> collection = database.getCollection("clients", Client.class);

            collection.insertOne(client);
        }catch (Exception e){
            System.out.println("Error al crear el client");
        }

    }

    // Método para borrar un client por su id, cuando se borra un client se borran todas sus accounts

    public static void borrarClient(int clientid) {
        // Borrar el cliente de la base de datos MongoDB
        Logger logger = LoggerFactory.getLogger("org.mongodb.driver");

        // String uri = "mongodb://usuario:password@host:puerto";
        String uri = "mongodb://alvrub:qwerty1234@ec2-54-146-188-92.compute-1.amazonaws.com:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
            CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

            MongoDatabase database = mongoClient.getDatabase("alvrub").withCodecRegistry(pojoCodecRegistry);
            MongoCollection<Client> collection = database.getCollection("clients", Client.class);

            collection.deleteMany(eq("clientid", clientid));
        }catch (Exception e){
            System.out.println("Error al borrar el client");
        }

        // Obtener todas las cuentas del cliente
        ArrayList<Account> accounts = OperacionesAccount.leerAccountsPorCliente(clientid);

        // Borrar todas las cuentas del cliente en la base de datos PostgreSQL
        for (Account account : accounts) {
            OperacionesAccount.borrarAccount(account.getAccountid());
        }
    }

    // Método para actualizar un client
    public static void actualizarClient(Client client) {

        Logger logger = LoggerFactory.getLogger("org.mongodb.driver");

        // String uri = "mongodb://usuario:password@host:puerto";
        String uri = "mongodb://alvrub:qwerty1234@ec2-54-146-188-92.compute-1.amazonaws.com:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
            CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

            MongoDatabase database = mongoClient.getDatabase("alvrub").withCodecRegistry(pojoCodecRegistry);
            MongoCollection<Client> collection = database.getCollection("clients", Client.class);

            collection.replaceOne(eq("clientid", client.getClientid()), client);
        }catch (Exception e){
            System.out.println("Error al borrar el client");
        }

    }

    //Método que devuelve un ArrayList con todos los clients
    public static ArrayList<Client> leerClients() {

        ArrayList<Client> clients = new ArrayList<>();
        Logger logger = LoggerFactory.getLogger("org.mongodb.driver");

        // String uri = "mongodb://usuario:password@host:puerto";
        String uri = "mongodb://alvrub:qwerty1234@ec2-54-146-188-92.compute-1.amazonaws.com:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
            CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

            MongoDatabase database = mongoClient.getDatabase("alvrub").withCodecRegistry(pojoCodecRegistry);
            MongoCollection<Client> collection = database.getCollection("clients", Client.class);

           clients = collection.find().into(new ArrayList<>());
        }catch (Exception e){
            System.out.println("Error al leer los clients");
        }

        return clients;
    }




}
