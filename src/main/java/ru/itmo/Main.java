package ru.itmo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.actors.MasterActor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("search");
        
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                final String value = reader.readLine();
                
                ActorRef master = system.actorOf(Props.create(MasterActor.class, 10L));
    
                Object response = ask(master, value, Timeout.apply(10, TimeUnit.SECONDS))
                        .toCompletableFuture()
                        .join();
    
                System.out.println(response);
            }
        } catch (IOException e) {
            LOG.warn("Can't read from console!", e);
        }
        
        system.terminate();
    }
}
