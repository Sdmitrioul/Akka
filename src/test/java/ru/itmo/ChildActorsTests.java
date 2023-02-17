package ru.itmo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.itmo.actors.ChildActor;
import ru.itmo.search.SearchProvider;
import ru.itmo.search.SearchRequest;
import ru.itmo.search.result.SearchResult;

import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ChildActorsTests {
    private ActorSystem system;
    
    @BeforeEach
    public void before() {
        system = ActorSystem.create("ChildTest");
    }
    
    @AfterEach
    public void after() {
        system.terminate();
    }
    
    @Test
    void testChild() {
        ActorRef childActor = system.actorOf(Props.create(ChildActor.class, SearchProvider.GOOGLE));
        
        SearchResult result = (SearchResult) ask(childActor, new SearchRequest("res"),
                Timeout.apply(10, TimeUnit.SECONDS)).toCompletableFuture()
                .join();
        
        assertSame(result.provider(), SearchProvider.GOOGLE);
        assertFalse(result.result()
                .isEmpty());
    }
}
