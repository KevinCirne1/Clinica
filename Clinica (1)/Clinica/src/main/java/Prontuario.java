import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Prontuario {

    private String nomePaciente;
    private Internacao internacao;
    private List<Procedimento> procedimentos = new ArrayList<>();

    // Método para carregar o prontuário de um arquivo
    public Prontuario carregueProntuario(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Aqui, fazemos o parsing da linha conforme o formato do seu arquivo CSV
                String[] dados = line.split(",");
                
                if (dados.length == 1) {
                    // Se houver apenas o nome do paciente, armazena o nome
                    nomePaciente = dados[0];
                } else if (dados.length == 2) {
                    // Se houver o tipo de leito e a quantidade de dias de internação
                    TipoLeito tipoLeito = TipoLeito.valueOf(dados[0].trim());
                    int dias = Integer.parseInt(dados[1].trim());
                    internacao = new Internacao(tipoLeito, dias);
                } else if (dados.length == 3) {
                    // Caso haja dados de procedimento, adicione os procedimentos
                    TipoProcedimento tipoProcedimento = TipoProcedimento.valueOf(dados[0].trim());
                    procedimentos.add(new Procedimento(tipoProcedimento));
                }
            }
        }
        return this;
    }

    // Outros métodos da classe...
}
