import java.time.LocalDate;
import java.util.SortedMap;
import java.util.TreeMap;

public interface IComentar{
	
	public SortedMap<LocalDate,Midia> listaMidiasAvaliadas = new TreeMap<>();

	private static void podeComentar(SortedMap<LocalDate,Midia> listaMidiasAvaliadas) throws AvaliacaoInsuficienteException {
		LocalDate data = LocalDate.now().minusMonths(1);
		long count = listaMidiasAvaliadas.entrySet().stream().filter(m -> m.getKey().getMonth().compareTo(data.getMonth()) == 0).count();
		if(count < 5) throw new AvaliacaoInsuficienteException("Número de Avaliações menores do que 5");
	}
	
	public static void comentar(String msg,Midia midia,String usuario) {
		try {
			podeComentar(listaMidiasAvaliadas);
			midia.comentar(usuario,msg);
		} catch (AvaliacaoInsuficienteException e) {
			System.out.println(e.getMessage());
		}
	}
	
}
