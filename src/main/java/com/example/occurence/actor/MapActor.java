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
		if (message instanceof Message m) {
            String word = m.word();
            System.out.println("Mapper: Traitement du mot: " + word);
            
            // Envoi du message au reducer
            reducers[0].tell(word, getSelf());
        } else {
            unhandled(message);
        }
		
	}

	public static Props props(ActorRef[] reducers) {
        return Props.create(MapActor.class, () -> new MapActor(reducers));
    }


}
