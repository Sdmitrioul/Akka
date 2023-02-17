package ru.itmo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.itmo.actors.MasterActor;
import ru.itmo.search.SearchProvider;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MasterActorTests {
    private ActorSystem system;
    
    @BeforeEach
    public void before() {
        system = ActorSystem.create("MasterTest");
    }
    
    @AfterEach
    public void after() {
        system.terminate();
    }
    
    @Test
    public void testMasterActor() {
        ActorRef masterActor = system.actorOf(Props.create(MasterActor.class, 10L));
        
        MasterActor.MasterResult response = (MasterActor.MasterResult) ask(masterActor, "query",
                Timeout.apply(10, TimeUnit.SECONDS)).toCompletableFuture()
                .join();
        
        assertEquals(response.value()
                .keySet(), EnumSet.allOf(SearchProvider.class));
        assertTrue(response.value()
                .values()
                .stream()
                .noneMatch(List::isEmpty));
    }
    
    @Test
    public void testMasterActorTimeout() {
        ActorRef masterActor = system.actorOf(Props.create(MasterActor.class, 1L));
        
        MasterActor.MasterResult response = (MasterActor.MasterResult) ask(masterActor, "query",
                Timeout.apply(10, TimeUnit.SECONDS)).toCompletableFuture()
                .join();
        
        assertTrue(response.value()
                .keySet()
                .isEmpty());
    }
}
