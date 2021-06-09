package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	ImdbDAO dao;
	Map<Integer, Actor> idMapActor;
	Graph<Actor, DefaultWeightedEdge> grafo;

	public List<String> listAllGenres() {
		dao = new ImdbDAO();
		return this.dao.listAllGenres();
	}

	public List<Integer> getActorPerGenre(String genere) {
		return this.dao.getActorPerGenre(genere);
	}

	public List<Actor> listAllActors() {
		return this.dao.listAllActors();
	}

	public List<Actor> creaGrafo(String genere) {
		this.grafo= new SimpleWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.dao=new ImdbDAO();
		this.idMapActor = new HashMap<Integer, Actor>();
		for (Actor a : this.dao.listAllActors()) {
			this.idMapActor.put(a.getId(), a);
			
			}
		List<ArcoGrafo> archi = dao.getArchi(genere);
		
	
		List<Actor> actorPerGenre = new ArrayList<>();
		for(Integer i : dao.getActorPerGenre(genere)) {
			
				actorPerGenre.add(idMapActor.get(i));
		}
		
		Graphs.addAllVertices(grafo, actorPerGenre);
		for(ArcoGrafo a : archi) {
			if(idMapActor.containsKey(a.getId1()) && idMapActor.containsKey(a.getId2())) {
				Graphs.addEdge(grafo, idMapActor.get(a.getId1()), idMapActor.get(a.getId2()), a.getPeso());
			}
		}
		
		 List<Actor> attori = new ArrayList<Actor>();
			if(this.grafo.vertexSet().size()==0)
				System.out.println("il grafo è vuoto");
			for(Actor a : this.grafo.vertexSet() ) {
				attori.add(a);
			}
			Collections.sort(attori);
			return attori;
		 
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	public List<Actor> attoriSimili(Actor attore){
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector<>(grafo);
		List<Actor> attoriConnessi = new ArrayList<>(ci.connectedSetOf(attore));
		attoriConnessi.remove(attore);
		Collections.sort(attoriConnessi);
		return attoriConnessi;
		
		
		
	}

	

}
