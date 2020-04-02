package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
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
	mappaBorders = new HashMap<>();
	listaBorders = new LinkedList<>();
	mappaPaesi = new HashMap <Integer,Country> ();
	mappaPaesi = dao.loadAllCountries(mappaPaesi);
	listaPaesi = new LinkedList <Country> (mappaPaesi.values());
	}
	public List<Country> getListaPaesi() {
		return listaPaesi;
	}
	public void creaGrafo (int anno) {
		// 1. crea l oggetto grafo
		this.grafo =new SimpleGraph<>(DefaultEdge.class);
	    // 2. aggiungere i vertici
		Graphs.addAllVertices(this.grafo, this.listaPaesi);
		// 3.A creo la mappa dei confini
		mappaBorders = dao.getCountryPairs(anno, mappaBorders, mappaPaesi);
		listaBorders = new LinkedList <Border> (mappaBorders.values());
		// 3.B aggiungo i vertici
		for (Border b : listaBorders) {
			Country c1 = b.getStatoA();
			Country c2 = b.getStatoB();
			grafo.addEdge(c1, c2);
		}
		System.out.println(grafo.vertexSet()+"\n"+grafo.edgeSet());
	}
	public int getNumberOfConnectedComponents() {
		
		return grafo.edgeSet().size();
	}
	

}
