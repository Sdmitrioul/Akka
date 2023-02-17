# AKKA
Repository was created for Software Design course in ITMO (CT) university by
Skroba Dmitriy.

### Purpose:

Gain practical experience experience using actors.

### What`s has been done:

1. For each request, a master-actor is created, which will collect the results from
search engines

2. The master-actor for each search engine creates a child-actor to which it sends the original
"search query"

3. The master-actor sets itself a receive timeout, how long it will wait
answers from child-actors

4. The child-actor makes a request to the appropriate search service and sends it
result in master-actor

5. Master-actor, upon receiving each answer, saves them if he received all 3
response or the timeout has passed, then sends the collected aggregated
result for further processing

6. Master-actor must die after returning an aggregated result