package ru.itmo.actors;

import akka.actor.UntypedActor;
import ru.itmo.search.SearchProvider;
import ru.itmo.search.SearchRequest;
import ru.itmo.search.exceptions.ClientException;

public final class ChildActor extends UntypedActor {
    private final SearchProvider provider;
    
    public ChildActor(final SearchProvider provider) {
        this.provider = provider;
    }
    
    @Override
    public void onReceive(final Object message) {
        if (message instanceof SearchRequest request) {
            try {
                getSender().tell(provider.ask(request), self());
            } catch (ClientException e) {
                // Ignore
            }
            getContext().stop(self());
        }
    }
}
