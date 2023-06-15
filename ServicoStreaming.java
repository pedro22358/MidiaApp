
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ServicoStreaming {

	private SortedSet<Midia> listaMidia = new TreeSet<>();
	private SortedSet<Cliente> listaCliente = new TreeSet<Cliente>();
	private Cliente clienteLogado;

	public ServicoStreaming() {
	}

	/**
	 * Metodo para realizar o login do usuario no servico. Ele permanecerá logado
	 * até que deslogue. Caso tenha algum erro, será lançado uma Exceção na tela.
	 * 
	 * @param usuario Recebe uma string contendo o nome de usuario
	 * @param senha   Recebe uma string contendo a senha do usuario
	 * @throws UsuarioSenhaErradosException
	 */
	public void logar(String usuario, String senha) throws UsuarioSenhaErradosException {
		Cliente cliente = listaCliente.stream().filter(c -> c.getUsuario().equals(usuario)).findFirst().orElse(null);
		if (cliente == null)
			throw new UsuarioSenhaErradosException("Usuário não encontrado.");

		else if (cliente.logar(senha))
			clienteLogado = (Cliente) cliente;

		else
			throw new UsuarioSenhaErradosException("Senha inválida.");
	}

	/**
	 * Metodo usado para cadastrar um novo cliente no sistema e depois salva-lo na
	 * lista de clientes
	 * 
	 * @param nome,    string que carrega o nome do cliente
	 * @param usuario, string que carrega o usuario escolhido pelo cliente
	 * @param senha,   string que carrega a senha escolhida pelo cliente
	 */
	public void cadastrar(String nome, String usuario, String senha) {
		listaCliente.add(new Cliente(nome, usuario, senha));
	}

	/**
	 * Metodo usado para remover um cliente do sistema, apos informar a senha, será
	 * realizado a verificacao se a senha informada coincide. Se for verdadeiro,
	 * removera o cliente da lista, se não, não removerá da lista e lançará uma
	 * exceção
	 * 
	 * @throws UsuarioSenhaErradosException
	 * 
	 */
	public void removerCliente(String senha) throws UsuarioSenhaErradosException {
		if (clienteLogado.logar(senha))
			listaCliente.remove(clienteLogado);

		else
			throw new UsuarioSenhaErradosException("Senha inválida.");
	}

	/**
	 * O metodo buscarGeral serve para buscar a midia desejada nas listas de midia
	 * tanto do sistema, quanto do usuario, dependerá da escolha do cliente em qual
	 * lista ele deseja buscar a midia. Caso o cliente tenha digitado uma midia que
	 * não existe, a lista irá retornar nula.
	 * 
	 * @param busca, recebe uma string contendo o que o usuario deseja buscar
	 * @param opcao, recebe a opcao da lista que deseja buscar ("geral" para a lista
	 *               de midias do sistema "assistir" para a lista de midias para
	 *               assistir futuramente do cliente "assistidos" para a lista de
	 *               midias assistidas do cliente)
	 * @return ArrayList<Midia>, retorna uma ArrayList de midias que contem no nome
	 *         a string buscada pelo usuário. Retornara nulo caso não seja
	 *         encontrado nenhuma midia
	 */
	public List<Midia> buscarGeral(String busca, String opcao) {
		List<Midia> resultados = new ArrayList<>();
		resultados = null;
		opcao = opcao.toLowerCase();
		switch (opcao) {
		case "geral":
			resultados = listaMidia.stream().filter(m -> m.nome.contains(busca)).collect(Collectors.toList());
			return resultados;

		case "assistir":
			resultados = clienteLogado.buscarLista(busca, opcao);
			return resultados;

		case "assistidas":
			resultados = clienteLogado.buscarLista(busca, opcao);
			return resultados;
		}
		return null;
	}

	/**
	 * O metodo serve para adicionar uma midia a lista de midias do sistema
	 * 
	 * @param midia, recebe um objeto midia
	 */
	private void adicionarMidia(Midia midia) {
		listaMidia.add(midia);
	}

	/**
	 * O metodo realiza a leitura de quatro arquivos contendo a audiencia,
	 * espectadores e os filmes e series; Durante a leitura, é realizado o
	 * gravamento das midias e clientes em suas respectivas listas.
	 * 
	 */
	public void lerArquivo() {
		try {
			Random gerador = new Random();
			FileReader arq = new FileReader("D:\\Eclipse\\Programs\\TrabalhoPraticoPOO\\src\\POO_Espectadores.csv");
			FileReader arq2 = new FileReader("D:\\Eclipse\\Programs\\TrabalhoPraticoPOO\\src\\POO_Series.csv");
			FileReader arq3 = new FileReader("D:\\Eclipse\\Programs\\TrabalhoPraticoPOO\\src\\POO_Audiencia.csv");
			FileReader arq4 = new FileReader("D:\\Eclipse\\Programs\\TrabalhoPraticoPOO\\src\\POO_Filmes.csv");
			BufferedReader lerarq = new BufferedReader(arq);
			BufferedReader lerarq2 = new BufferedReader(arq2);
			BufferedReader lerarq3 = new BufferedReader(arq3);
			BufferedReader lerarq4 = new BufferedReader(arq4);

			String linha = lerarq.readLine();
			while (linha != null) {
				String[] linhaPartida = linha.split(";");
				cadastrar(linhaPartida[0], linhaPartida[1], linhaPartida[2]);
				linha = lerarq.readLine();
			}

			String linha2 = lerarq2.readLine();
			while (linha2 != null) {
				String[] linhaPartida2 = linha2.split(";");

				DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate dataLancamento = LocalDate.parse(linhaPartida2[2], formato);

				Midia serie = new Serie(linhaPartida2[0], linhaPartida2[1], dataLancamento, gerador.nextInt(12));
				adicionarMidia(serie);
				linha2 = lerarq2.readLine();
			}

			/*String linha3 = lerarq3.readLine();
			while (linha3 != null) {
				String[] linhaPartida3 = linha3.split(";");

				Midia midia = listaMidia.stream().filter(m -> m.getIdMidia().equals(linhaPartida3[2])).findFirst().orElse(null);
				Cliente cliente = listaCliente.stream().filter(c -> c.getUsuario().equals(linhaPartida3[0])).findFirst().orElse(null);
				
				if(cliente != null && midia != null)
					try {
						cliente.adicionar(linhaPartida3[1], midia);
					} catch (MidiaJaAdicionadaException e) {
						System.out.println(e.getMessage());
					}

				linha3 = lerarq3.readLine();
			}*/

			String linha4 = lerarq4.readLine();
			while (linha != null) {
				String[] linhaPartida4 = linha4.split(";");

				DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate dataLancamento = LocalDate.parse(linhaPartida4[2], formato);

				int duracao = Integer.parseInt(linhaPartida4[3]);

				Midia filme = new Filme(linhaPartida4[0], linhaPartida4[1], dataLancamento, duracao);
				adicionarMidia(filme);
				linha4 = lerarq4.readLine();
			}

			arq.close();
			arq2.close();
			arq3.close();
			arq4.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * O metodo deslogar é utilizado para deslogar o usuário do sistema.
	 */
	public void deslogar() {
		clienteLogado = null;
	}

	/**
	 * O metodo avaliar é utilizado para dar uma nota para uma midia. Caso o cliente
	 * já tenha avaliado essa midia antes, não será feito a nova avaliacao.
	 * 
	 * @param nota,  double contendo a nota que deseja ser dada à midia
	 * @param busca, String contendo a midia que deseja ser avaliada
	 * @throws MidiaNaoEncontradaException 
	 */
	public void avaliar(double nota, String busca) throws MidiaNaoEncontradaException {
		try {
			Midia midiaAvaliada = buscarGeral(busca, "geral").get(0);
			Collection<Midia> verificacao = clienteLogado.getListaAvaliados().values();
			if(nota <= 5 && nota >= 1) {
				if (!verificacao.contains(midiaAvaliada)) {
					midiaAvaliada.availiar(nota);
					clienteLogado.setListaAvaliados(midiaAvaliada);
				}
				else {
					throw new MidiaNaoEncontradaException("Midia já avaliada! Favor inserir outra midia!");
				}
				
			} else {
				throw new IllegalArgumentException("Nota inválida! Por favor escolha uma entre 1 e 5!");
			}
		} catch(IndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		}
		
		
	}

	public void adicionar(String opcao, String mi) throws MidiaNaoEncontradaException {
		Midia midia = listaMidia.stream().filter(m -> m.getNome().toLowerCase().equals(mi.toLowerCase())).findFirst().orElse(null);
		if(midia != null)
			try {
				clienteLogado.adicionar(opcao, midia);
			} catch (MidiaJaAdicionadaException e) {
				System.out.println(e.getMessage());
			}
		else throw new MidiaNaoEncontradaException("Não foi encontrado nenhum filme ou série com esse nome.");
	}
	
	public void comentar(String msg, String busca) {
		Midia midiaAvaliada = buscarGeral(busca, "geral").get(0);
		clienteLogado.comentar(msg, midiaAvaliada);
	}
	
	public SortedSet<Cliente> getListaCliente(){
		return listaCliente;
	}
	
	public SortedSet<Midia> getListaMidia(){
		return listaMidia;
	}
}
