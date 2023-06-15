import java.util.Random;

public enum Genero {
	Acao ("Ação"),
	Anime ("Anime"),
	Aventura ("Aventura"),
	Comedia ("Comedia"),
	Documentario ("Documentario"),
	Drama ("Drama"),
	Policial ("Policial"),
	Romance ("Romance"),
	Suspense ("Suspense");

	String descricao;
	
	private static Random grng =  new Random();
	
	Genero(String descricao) {
		this.descricao = descricao;
	}
	
	public static Genero randomGenero() {
		Genero[] genero = values();
		return genero[grng.nextInt(genero.length)];
	}
	
	public String getDescricao() {
		return this.descricao;
	}
}
