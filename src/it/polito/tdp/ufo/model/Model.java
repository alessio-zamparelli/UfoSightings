package it.polito.tdp.ufo.model;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {

	// Per la ricorsione
	// 1. Struttura dati finale
	private List<String> ottima;
	// c'è lo stato di partenza, ed un insieme di altri stati non ripetuti

	// 2. Struttura dati parziale
	// definita nel handler del metodo ricorsivo

	// 3. Condizione di terminazione
	// Dopo un determinato nodo non ci sono piu successori che non ho considerato

	// 4. Genero una nuova soluzione a partire da una soluzione parziale
	// Dato l'ultimo nodi inserito in parziale, considero tutti i successori di quel
	// nodo che non ho ancora considerato

	// 5. FILTRO
	// Alla fine ritornero una sola soluzione -> quella che cui la MAX(size())

	// 6. LIVELLO DI RICORSIONE
	// Lunghezza del percorso parziale

	// 7. Il caso iniziale
	// Parziale contiene il mio stato di partenza

	private SightingsDAO dao;
	private List<String> stati;
	private Graph<String, DefaultEdge> grafo;

	public Model() {
		this.dao = new SightingsDAO();
	}

	public List<AnnoCount> getAnni() {

		return this.dao.getAnni();

	}

	public void creaGrafo(Year anno) {

		this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
		this.stati = this.dao.getStati(anno);
		Graphs.addAllVertices(this.grafo, this.stati);

		// Soluzione "Semplice" -> doppio ciclo, controllo esistenza arco
		for (String s1 : this.grafo.vertexSet()) {
			for (String s2 : this.grafo.vertexSet()) {
				if (!s1.equals(s2)) {
					if (this.dao.esisteArco(s1, s2, anno)) {
						this.grafo.addEdge(s1, s2);
					}
				}
			}
		}

		System.out.println("GRAFO CREATO");
		System.out.println("#VERTICI: " + this.grafo.vertexSet().size());
		System.out.println("#ARCHI: " + this.grafo.edgeSet().size());
	}

	public int getNvertici() {
		return this.grafo.vertexSet().size();
	}

	public int getNarchi() {
		return this.grafo.edgeSet().size();
	}

	public List<String> getStati() {
		return this.stati;
	}

	public List<String> getSuccessori(String stato) {
		return Graphs.successorListOf(this.grafo, stato);
	}

	public List<String> getPredecessori(String stato) {
		return Graphs.predecessorListOf(this.grafo, stato);
	}

	public List<String> getRaggiungibili(String stato) {
		List<String> raggiungibili = new ArrayList<String>();
		DepthFirstIterator<String, DefaultEdge> dp = new DepthFirstIterator<>(this.grafo, stato);

		dp.next();

		while (dp.hasNext()) {
			raggiungibili.add(dp.next());
		}

		return raggiungibili;
	}

	public List<String> getPercorsoMassimo(String partenza) {
		this.ottima = new LinkedList<>();
		List<String> parziale = new LinkedList<>();
		parziale.add(partenza);

		// La magicFunction
		cercaPercorso(parziale);

		return this.ottima;
	}

	private void cercaPercorso(List<String> parziale) {
		// verifica di caso migliore
		if (parziale.size() > ottima.size()) {
			this.ottima = new LinkedList<>(parziale);
		}

		// Ottengo tutti i candidati
		List<String> candidati = getSuccessori(parziale.get(parziale.size() - 1));
		for (String candidato : candidati) {
			if (!parziale.contains(candidato)) {
				// E' un candidato vero, che non ho ancora considerato
				parziale.add(candidato);
				this.cercaPercorso(parziale);
				parziale.remove(parziale.size() - 1);
			}
		}

	}

}
