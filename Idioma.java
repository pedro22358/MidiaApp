import java.util.Random;

public enum Idioma {
	Portugues ("Português"),
	Ingles ("Inglês"),
	Espanhol ("Espanhol");
	
	String descricao;
	
	Idioma(String descricao) {
		this.descricao = descricao;
	}
	
	private static Random irng =  new Random();
	
	public static Idioma randomIdioma() {
		Idioma[] idioma = values();
		return idioma[irng.nextInt(idioma.length)];
	}
	
	public String getDescricao() {
		return this.descricao;
	}
}
