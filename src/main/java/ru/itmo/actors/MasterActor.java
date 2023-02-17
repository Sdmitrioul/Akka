package ru.itmo.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;
import ru.itmo.search.SearchProvider;
import ru.itmo.search.SearchRequest;
import ru.itmo.search.parser.ResultEntity;
import ru.itmo.search.result.SearchResult;
import scala.concurrent.duration.Duration;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class MasterActor extends UntypedActor {
    private static final Set<SearchProvider> SEARCH_PROVIDERS = EnumSet.allOf(SearchProvider.class);
    
    private final MasterResult result = new MasterResult(new EnumMap<>(SearchProvider.class));
    private final AtomicInteger size = new AtomicInteger(0);
    private final long timeout;
    private ActorRef sender;
    
    public MasterActor() {
        this(1);
    }
    
    public MasterActor(final long timeout) {
        this.timeout = timeout;
    }
    
    @Override
    public void onReceive(final Object message) {
        if (message instanceof String query) {
            sender = getSender();
            
            for (var provider : SEARCH_PROVIDERS) {
                ActorRef child = getContext().actorOf(Props.create(ChildActor.class, provider));
                child.tell(new SearchRequest(query), self());
            }
            
            getContext().setReceiveTimeout(Duration.create(timeout, TimeUnit.SECONDS));
        } else if (message instanceof SearchResult searchResult) {
            result.value().put(searchResult.provider(), searchResult.result());
            
            var currentSize = size.incrementAndGet();
            
            if (currentSize == SEARCH_PROVIDERS.size()) {
                tellResult();
            }
        } else if (message instanceof ReceiveTimeout) {
            tellResult();
        }
    }
    
    private void tellResult() {
       /* sender.tell(result.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList()), self());*/
        sender.tell(result, self());
        getContext().stop(self());
    }
    
    public static record MasterResult(Map<SearchProvider, List<ResultEntity>>  value){}
}
