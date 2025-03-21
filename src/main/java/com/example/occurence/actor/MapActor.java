package com.example.occurence.actor;

import com.example.occurence.dto.Message;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class MapActor extends UntypedActor{
	
	private ActorRef[] reducers;

	 // Constructeur avec le tableau de reducers
    public MapActor(ActorRef[] reducers) {
        this.reducers = reducers;
    }

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String line) {
            System.out.println(getSelf().path().name() + "reçoit la ligne: " + line);
            
            String[] words = line.split("\\s+");
            for (String word : words) {
            	String cleanWord = word.toLowerCase().replaceAll("[^\\p{L}]", "");
            	ActorRef targetReducer = reducers[Math.abs(word.hashCode()) % reducers.length];
            	targetReducer.tell(new Message(cleanWord), getSelf());
            }
        } else {
            unhandled(message);
        }
		
	}

	public static Props props(ActorRef[] reducers) {
        return Props.create(MapActor.class, () -> new MapActor(reducers));
    }


}
