
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.time.LocalDate;

public class Midia implements Comparable<Midia> {
	protected final String idMidia;
	protected final String nome;
	protected ArrayList<String> idioma = new ArrayList<>();
	protected String genero;
	protected final LocalDate dataLancamento;
	protected int quantidadeDeViwers;
	protected double avaliacao;
	protected ArrayList<Double> notas = new ArrayList<>();
	protected SortedMap<String,String> listaComentarios = new TreeMap<>();
	
	public void init() {
		for(int i=0; i<2; i++) {
			if(!idioma.contains(Idioma.randomIdioma().getDescricao()))
			this.idioma.add(Idioma.randomIdioma().getDescricao());
		}
		this.genero = (Genero.randomGenero().getDescricao());
	}

	public Midia(String idMidia, String nome, LocalDate dataLancamento) {
		this.idMidia = idMidia;
		this.nome = nome;
		this.dataLancamento = dataLancamento;
		this.quantidadeDeViwers = 0;
		init();
	}

	public void adicionarIdioma(String idioma) {
		this.idioma.add(idioma);
	}

	public void removerIdioma(String idioma) {
		this.idioma.remove(idioma);
	}

	public void assistiu() {
		quantidadeDeViwers++;
	}

	public void availiar(double nota) {
		notas.add(nota);
		double x = 0;
		for (double not : notas) {
			x += not;
		}
		this.avaliacao = x / notas.size();
	}
	
	protected void comentar(String usuario, String comentario) {
		listaComentarios.put(usuario, comentario);
	}
	
	@Override
	public int compareTo(Midia m) {
		return this.nome.compareTo(m.nome);
	}

	// Getters and Setters

	public ArrayList<String> getIdioma() {
		return idioma;
	}

	public String getGenero() {
		return genero;
	}

	public String getNome() {
		return nome;
	}

	public LocalDate getDataLancamento() {
		return dataLancamento;
	}

	public String getIdMidia() {
		return idMidia;
	}

	public double getAvaliacao() {
		return avaliacao;
	}

	protected SortedMap<String, String> getListaComentarios() {
		return listaComentarios;
	}
	
	public int getQuantidadeDeViwers() {
		return quantidadeDeViwers;
	}

}
