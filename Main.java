
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {

	public static void logado(ServicoStreaming servico, Scanner ent) {
		int opcao = 1;
		while (opcao != 0) {
			System.out.println("1 - Buscar Midia");
			System.out.println("2 - Avaliar Midia Assistida");
			System.out.println("3 - Comentar em uma Midia Assistida");
			System.out.println("4 - Adicionar Midia a lista");
			System.out.println("5 - Relátorios do sistema");
			System.out.println("6 - Deslogar");
			System.out.println("7 - Remover Cliente");

			opcao = ent.nextInt();

			switch (opcao) {

			case 1:
				ent.nextLine();
				System.out.println("Qual midia deseja buscar?");
				String midia = ent.nextLine();

				System.out.println("Qual lista deseja pesquisar?");
				System.out.println("geral - Lista de midias do serviço");
				System.out.println("assistir - Lista de midias que o usuário deseja ver");
				System.out.println("assistidas - Lista de midias que foram assistidas pelo usuários");
				String opc = ent.nextLine();

				opc.toLowerCase();

				List<Midia> midias = servico.buscarGeral(midia, opc);
				if (midias != null)
					for (Midia m : midias) {
						System.out.println("Nome: " + m.getNome());
						System.out.println("Data de Lancamento: " + m.getDataLancamento());
						System.out.println("Idiomas: " + m.getIdioma().toString());
						System.out.println("Generos: " + m.getGenero().toString());
						System.out.println("Avaliacao: " + m.getAvaliacao());
						System.out.println("Comentarios: ");
						m.getListaComentarios().forEach((key, value) -> System.out.println(key + " " + value));
					}
				else
					System.out.println("Não foi encontrado nenhuma midia.");
				break;

			case 2:
				ent.nextLine();
				List<Midia> midias1 = servico.buscarGeral(null, "assistidas");
				if (!midias1.isEmpty()) {
					midias1.stream()
							.map(m -> " " + m.getNome() + "\n Avaliação:" + String.valueOf(m.getAvaliacao())
									+ "\n Data de Lançamento:" + m.getDataLancamento() + "\n Idiomas: " + m.getIdioma()
									+ "\n Generos:" + m.getGenero() + "\n")
							.forEach(System.out::println);

					System.out.println("Qual midia assistida deseja avaliar?");
					String busca = ent.nextLine();
					System.out.println("Qual nota deseja dar?");
					double nota = ent.nextDouble();

					try {
						servico.avaliar(nota, busca);
						System.out.println("Midia avaliada");
					} catch (MidiaNaoEncontradaException e2) {
						System.out.println(e2.getMessage());
					} catch (IllegalArgumentException e3) {
						System.out.println(e3.getMessage());
					}
				} else {
					System.out.println("Não foi encontrada nenhuma midia na sua lista de Midias Assistidas!");
				}
				break;

			case 3:
				ent.nextLine();
				List<Midia> midias2 = servico.buscarGeral(null, "assistidas");
				if (!midias2.isEmpty()) {
					midias2.stream()
							.map(m -> " " + m.getNome() + "\n Avaliação:" + String.valueOf(m.getAvaliacao())
									+ "\n Data de Lançamento:" + m.getDataLancamento() + "\n Idiomas: "
									+ m.getIdioma().toString() + "\n Generos:" + m.getGenero().toString() + "\n")
							.forEach(System.out::println);

					System.out.println("Qual midia assistida deseja Comentar?");
					String busca = ent.nextLine();
					System.out.println("Qual comentário deseja fazer?");
					String comentario = ent.nextLine();
					servico.comentar(comentario, busca);
				}
				break;

			case 4:
				ent.nextLine();
				System.out.println("Qual midia deseja adicionar?");
				String mi = ent.nextLine();
				System.out.println("Em qual lista deseja colocar?");
				System.out.println("Para assistir depois: (F)");
				System.out.println("Já assistidas: (A)");
				String opca = ent.nextLine();

				try {
					servico.adicionar(opca, mi);
				} catch (MidiaNaoEncontradaException e1) {
					System.out.println(e1.getMessage());
				}

				break;

			case 5:
				relatoriosSistema(servico, ent);
				break;

			case 6:
				servico.deslogar();
				System.out.println("Cliente deslogado!");
				opcao = 0;
				break;

			case 7:
				System.out.println("Tem certeza que deseja remover a conta? S/N");
				char op = ent.next().charAt(0);
				Character.toUpperCase(op);
				if (op == 'S') {
					System.out.println("Digite sua senha:");
					try {
						servico.removerCliente(ent.nextLine());
					} catch (UsuarioSenhaErradosException e) {
						System.out.println(e.getMessage());
					}
					System.out.println("Conta removida");
					opcao = 0;
				} else {
					System.out.println("Conta não removida");
				}
				break;
			}
		}

	}

	public static void relatoriosSistema(ServicoStreaming servico, Scanner ent) {
		int quant;
		int maiorquant = 0;
		Cliente maior = null;
		List<Midia> midia;
		Map<Object, List<Midia>> listaMidia;
		int opcao = 1;
		while (opcao != 0) {
			System.out.println("Qual relátorio deseja ver?");
			System.out.println("1 - Qual cliente assistiu mais midias ");
			System.out.println("2 - Qual cliente tem mais avaliações");
			System.out.println("3 - Porcentagem de clientes com pelo menos 15 avaliações");
			System.out.println("4 - Melhores 10 midias com maior visualizações");
			System.out.println("5 - Melhores 10 midias com maior media de avaliação com mais de 100 visualizações");
			System.out.println("6 - Melhores 10 mídias com mais visualizações separadas por gênero");
			System.out.println("7 - Melhores 10 midias com maior media de avaliação com mais de 100 visualizações separadas por gênero");
			System.out.println("0 - Sair");
			opcao = ent.nextInt();
			switch (opcao) {

			case 1:
				maiorquant = 0;
				quant = 0;
				maior = null;
				for (Cliente c : servico.getListaCliente()) {
					quant = c.getListaAssistidos().size();
					if (quant > maiorquant) {
						maiorquant = quant;
						maior = c;
					}
				}
				System.out.println("Cliente com maior quantidade de midias assistidas é: " + maior.getNome()
						+ "! \nAssistiu um total de: " + maior.getListaAssistidos().size() + " Midias!");
				break;

			case 2:
				maiorquant = 0;
				quant = 0;
				maior = null;
				for (Cliente c : servico.getListaCliente()) {
					quant = c.getListaAvaliados().size();
					if (quant > maiorquant) {
						maiorquant = quant;
						maior = c;
					}
				}
				System.out.println("Cliente com maior quantidade de midias assistidas é: " + maior.getNome()
						+ "! \nAvaliou um total de: " + maior.getListaAvaliados().size() + " Midias!");
				break;

			case 3:
				maiorquant = 0;
				quant = 0;
				maior = null;
				int quantCliente = 0;
				for (Cliente c : servico.getListaCliente()) {
					quant = c.getListaAvaliados().size();
					quantCliente++;
					if (quant > 15) {
						maiorquant++;
					}
				}
				int porc = (maiorquant * 100) / quantCliente;
				System.out.println("A porcentagem de clientes com pelo menos 15 avaliações é de: " + porc + "%");
				break;

			case 4:
				midia = new ArrayList<>();
				midia = servico.getListaMidia().stream()
						.sorted(Comparator.comparing(Midia::getQuantidadeDeViwers).reversed()).limit(10).toList();
				System.out.println("O top 10 filmes mais assistidos são:");
				for (Midia m : midia) {
					System.out.println(m.getNome());
				}
				break;

			case 5:
				midia = new ArrayList<>();
				midia = servico.getListaMidia().stream().filter(m -> m.getQuantidadeDeViwers() >= 100)
						.sorted(Comparator.comparing(Midia::getAvaliacao).reversed()).limit(10).toList();
				if (!midia.isEmpty()) {
					System.out.println("O top 10 filmes com melhor avaliação são:");
					for (Midia m : midia) {
						System.out.println(m.getNome());
					}
				} else {
					System.out.println("Não há midias com mais de 100 visualizações.");
				}
				break;

			case 6:
				listaMidia = servico.getListaMidia().stream()
						.sorted(Comparator.comparing(Midia::getQuantidadeDeViwers).reversed())
						.collect(Collectors.groupingBy(Midia::getGenero));

				Map<String, List<Midia>> listaMidiaPorGenero = new TreeMap<>();
				listaMidia.forEach((genero, mid) -> {
					List<Midia> dezMidiasDoGenero = mid.stream().distinct().limit(10).collect(Collectors.toList());
					listaMidiaPorGenero.put((String) genero, dezMidiasDoGenero);
				});

				listaMidiaPorGenero.forEach((genero, mi) -> {
					System.out.println("Gênero: " + genero);
					System.out.println("Mídias:");
					mi.forEach(m -> System.out.println(m.getNome()));
					System.out.println();
				});
				break;

			case 7:
				listaMidia = servico.getListaMidia().stream().filter(Midia -> Midia.getQuantidadeDeViwers() >= 100)
						.sorted(Comparator.comparing(Midia::getAvaliacao).reversed())
						.collect(Collectors.groupingBy(Midia::getGenero));

				Map<String, List<Midia>> listaMidiaPorGenero2 = new TreeMap<>();
				listaMidia.forEach((genero, mid) -> {
					List<Midia> dezMidiasDoGenero = mid.stream().distinct().limit(10).collect(Collectors.toList());
					listaMidiaPorGenero2.put((String) genero, dezMidiasDoGenero);
				});

				if (!listaMidiaPorGenero2.isEmpty()) {
					listaMidiaPorGenero2.forEach((genero, mi) -> {
						System.out.println("Gênero: " + genero);
						System.out.println("Mídias:");
						mi.forEach(m -> System.out.println(m.getNome()));
						System.out.println();
					});
				} else {
					System.out.println("Não há midias com mais de 100 visualizações.");
				}
				break;
			}
		}
	}

	public static void main(String[] args) {
		Scanner ent = new Scanner(System.in);

		String usuario;
		String senha;
		int opcao = 1;

		ServicoStreaming servico = new ServicoStreaming();
		servico.lerArquivo();

		while (opcao != 0) {
			System.out.println("Menu:");
			System.out.println("1 - Logar");
			System.out.println("2 - Cadastrar");
			System.out.println("0 - Sair");

			opcao = ent.nextInt();

			switch (opcao) {
			case 1:
				ent.nextLine();
				System.out.println("Digite o usuário:");
				usuario = ent.nextLine();
				System.out.println("Digite a senha:");
				senha = ent.nextLine();
				try {
					servico.logar(usuario, senha);
					System.out.println("Usuário logado com sucesso!");
					logado(servico, ent);
				} catch (UsuarioSenhaErradosException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 2:
				ent.nextLine();
				System.out.println("Digite seu nome:");
				String nome = ent.nextLine();
				System.out.println("Digite o usuário:");
				usuario = ent.nextLine();
				System.out.println("Digite a senha:");
				senha = ent.nextLine();

				servico.cadastrar(nome, usuario, senha);
				System.out.println("Usuário cadastrado com sucesso!");
				break;

			}
		}

	}
}