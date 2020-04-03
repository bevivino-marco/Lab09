package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;



public class Model {
    private Map <Integer,Country> mappaPaesi;
    private List <Country> listaPaesi;
    private Map <Integer,Border> mappaBorders;
    private List <Border> listaBorders;
	private SimpleGraph<Country, DefaultEdge> grafo;
	private BordersDAO dao;
	private Map <Country, Country >back;
	public Model() {
    dao = new BordersDAO ();
	
	mappaPaesi = new HashMap <Integer,Country> ();
	mappaPaesi = dao.loadAllCountries(mappaPaesi);
	listaPaesi = new LinkedList <Country> (mappaPaesi.values());
	}
	private class EdgeTraversedGraphListener implements TraversalListener<Country, DefaultEdge> {

		@Override
		public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> ev) {
			Country sourceVertex  = grafo.getEdgeSource(ev.getEdge());
			Country targetVertex = grafo.getEdgeTarget(ev.getEdge());
			/**
			 * se il grafo è orientato allora il source sara il parente e il target sara il child.
			 * se non è orientato, potrebbe anche essere il contrario.
			 */
			if (!back.containsKey(targetVertex) && back.containsKey(sourceVertex)) {
				back.put(targetVertex, sourceVertex);
			}else if (!back.containsKey(sourceVertex) && back.containsKey(targetVertex)) {
				back.put(sourceVertex, targetVertex);
			}
			
		}

		@Override
		public void vertexFinished(VertexTraversalEvent<Country> arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void vertexTraversed(VertexTraversalEvent<Country> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	public List<Country> getListaPaesi() {
		class Comparatore implements Comparator <Country>{

			@Override
			public int compare(Country c1, Country c2) {
				
				return c1.getNome().compareTo(c2.getNome());
			}
			
		

		
		}
		 listaPaesi.sort(new Comparatore());
		 return listaPaesi;
	}
	public void creaGrafo (int anno) {
		mappaBorders = new HashMap<>();
		listaBorders = new LinkedList<>();
		// 1. crea l oggetto grafo
		this.grafo =new SimpleGraph<>(DefaultEdge.class);
	    // 2. aggiungere i vertici
		//Graphs.addAllVertices(this.grafo, this.listaPaesi); COSI FACENDO
		// NON TENGO CONTO DEGLI STATI CHE NON SONO CONFINANTI	
		// 3.A creo la mappa dei confini
		mappaBorders = dao.getCountryPairs(anno, mappaBorders, mappaPaesi);
		listaBorders = new LinkedList <Border> (mappaBorders.values());
		// 2. aggiungere i vertici
		// 3.B aggiungo i vertici
		if (listaBorders.isEmpty()) {
		
				throw new RuntimeException("No country pairs for specified year");
			
		}
		for (Border b : listaBorders) {
			Country c1 = b.getStatoA();
			Country c2 = b.getStatoB();
			grafo.addVertex(c1);
			grafo.addVertex(c2);
			grafo.addEdge(c1, c2);
		}
		System.out.println(grafo.vertexSet()+"\n"+grafo.edgeSet());
		System.out.println("i vertici sono : "+grafo.vertexSet().size()+"\nGli archi sono : "+grafo.edgeSet().size());
	}
	public int getNumberOfConnectedComponents() {
		ConnectivityInspector<Country, DefaultEdge> cI = new ConnectivityInspector <Country, DefaultEdge>(grafo);
		
		return cI.connectedSets().size();
	}
	public Map<Country, Integer> getCountryCounts() {
		Map<Country, Integer> mappa = new HashMap <Country, Integer>();
		for (Country c : grafo.vertexSet()) {
			mappa.put(c, grafo.degreeOf(c));
		}
		
		return mappa;
	}
	public List <Country > getViciniIterator(Country source) {
		List <Country> result = new ArrayList<Country>();
		back = new HashMap<>();
		//GraphIterator <Country, DefaultEdge> it = new DepthFirstIterator<>(this.grafo, source);
		GraphIterator <Country, DefaultEdge> it = new BreadthFirstIterator<>(this.grafo, source);
		back.put(source, null); // la radice non ha un padre
		it.addTraversalListener(new Model.EdgeTraversedGraphListener());
		while (it.hasNext()) {
			result.add(it.next());
		}
		//System.out.println(back);
		return result;
		
	}
	public List <Country> getViciniRicorsione(Country source){
		List <Country> parziale = new ArrayList <Country>();
		List <Country> lista = new LinkedList <Country>(listaPaesi);
		int livello = 0;
		cerca(parziale,source, livello, lista);
		return parziale;
	}
	private void cerca(List<Country> parziale, Country source, int livello,List<Country> lista) {
		if (livello ==0 ) {
			parziale.add(source);
		}
		if (livello == lista.size()-1) {
			System.out.println(parziale.toString());
			return;
		}
		for (int i =0; i< lista.size(); i++) {
			if (!parziale.contains(lista.get(i)) && areVicini(parziale.get(parziale.size()-1), lista.get(i))){
				parziale.add(lista.get(i));
				//lista.remove(i);
				cerca (parziale, lista.get(i), livello+1,lista);
				parziale.remove(lista.get(i));
			}
		}
		
	}
	private boolean areVicini(Country country, Country country2) {
		List <Country> vicini = new LinkedList <Country>(Graphs.neighborListOf(grafo, country));
		for (Country c : vicini) {
			if (c.equals(country2)) {
				return true;
			}
		}
		return false;
	}

}
