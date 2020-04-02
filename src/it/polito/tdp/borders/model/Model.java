package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
    private Map <Integer,Country> mappaPaesi;
    private List <Country> listaPaesi;
    private Map <Integer,Border> mappaBorders;
    private List <Border> listaBorders;
	private SimpleGraph<Country, DefaultEdge> grafo;
	private BordersDAO dao;
	public Model() {
    dao = new BordersDAO ();
	
	mappaPaesi = new HashMap <Integer,Country> ();
	mappaPaesi = dao.loadAllCountries(mappaPaesi);
	listaPaesi = new LinkedList <Country> (mappaPaesi.values());
	}
	public List<Country> getListaPaesi() {
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
	

}
