
import java.time.LocalDate;

public class Filme extends Midia{
	
	private int duracao;

	public Filme(String idMidia, String nome, LocalDate dataLancamento, int duracao) {
		super(idMidia, nome, dataLancamento);
		this.duracao = duracao;
	}

	
	//Getters and Setters 
	
	protected int getDuracao() {
		return duracao;
	}

	protected void setDuracao(int duracao) {
		this.duracao = duracao;
	}

}
