import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ProntuarioTest {

    @After
    public void cleanUp() {
        DirectoryStream<Path> stream = null;
        try {
            stream = Files.newDirectoryStream(Paths.get("."), "*.csv");
            for (Path path : stream) {
                Files.delete(path);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testSomenteProcedimentos() {
        Prontuario prontuario = new Prontuario("Paul McCartney");

        prontuario.addProcedimento(new Procedimento(TipoProcedimento.BASICO));
        prontuario.addProcedimento(new Procedimento(TipoProcedimento.AVANCADO));

        final String respostaEsperada = "----------------------------------------------------------------------------------------------" +
                "\nA conta do(a) paciente Paul McCartney tem valor total de __ R$ 550,00 __" +
                "\n" +
                "\nConforme os detalhes abaixo:" +
                "\n" +
                "\nValor Total Procedimentos:		R$ 550,00" +
                "\n					1 procedimento básico" +
                "\n					1 procedimento avançado" +
                "\n" +
                "\nVolte sempre, a casa é sua!" +
                "\n----------------------------------------------------------------------------------------------";

        assertEquals(respostaEsperada, prontuario.imprimaConta());
    }

    @Test
    public void testInternacaoComProcedimentos() {
        Prontuario prontuario = new Prontuario("Nando Reis");
        prontuario.setInternacao(new Internacao(TipoLeito.APARTAMENTO, 4));

        prontuario.addProcedimento(new Procedimento(TipoProcedimento.BASICO));
        prontuario.addProcedimento(new Procedimento(TipoProcedimento.COMUM));
        prontuario.addProcedimento(new Procedimento(TipoProcedimento.COMUM));
        prontuario.addProcedimento(new Procedimento(TipoProcedimento.AVANCADO));

        final String respostaEsperada = "----------------------------------------------------------------------------------------------" +
                "\nA conta do(a) paciente Nando Reis tem valor total de __ R$ 1.210,00 __" +
                "\n" +
                "\nConforme os detalhes abaixo:" +
                "\n" +
                "\nValor Total Diárias:			R$ 360,00" +
                "\n					4 diárias em apartamento" +
                "\n" +
                "\nValor Total Procedimentos:		R$ 850,00" +
                "\n					1 procedimento básico" +
                "\n					2 procedimentos comuns" +
                "\n					1 procedimento avançado" +
                "\n" +
                "\nVolte sempre, a casa é sua!" +
                "\n----------------------------------------------------------------------------------------------";

        assertEquals(respostaEsperada, prontuario.imprimaConta());
    }

    @Test
    public void testSomenteInternacao() {
        Prontuario prontuario = new Prontuario("MC Criolo");
        prontuario.setInternacao(new Internacao(TipoLeito.ENFERMARIA, 1));

        final String respostaEsperada = "----------------------------------------------------------------------------------------------" +
                "\nA conta do(a) paciente MC Criolo tem valor total de __ R$ 40,00 __" +
                "\n" +
                "\nConforme os detalhes abaixo:" +
                "\n" +
                "\nValor Total Diárias:			R$ 40,00" +
                "\n					1 diária em enfermaria" +
                "\n" +
                "\nVolte sempre, a casa é sua!" +
                "\n----------------------------------------------------------------------------------------------";

        assertEquals(respostaEsperada, prontuario.imprimaConta());
    }

    @Test
    public void testCarregarArquivoSemInternacao() {
        String path = "src/test/resources/prontuario_exportado_sem_internacao.csv";

        Prontuario prontuario = null;

        try {
            prontuario = new Prontuario(null).carregueProntuario(path);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        assertEquals("Ermenegildo Godofredo", prontuario.getNomePaciente());
        assertNull(prontuario.getInternacao());

        Map<TipoProcedimento, Long> procedimentosAgrupados = prontuario.getProcedimentos().stream().collect(
                Collectors.groupingBy(Procedimento::getTipoProcedimento, Collectors.counting()));

        assertEquals(10L, procedimentosAgrupados.get(TipoProcedimento.BASICO).longValue());
        assertEquals(2L, procedimentosAgrupados.get(TipoProcedimento.COMUM).longValue());
        assertNull(procedimentosAgrupados.get(TipoProcedimento.AVANCADO));
    }

    @Test
    public void testCarregarArquivoSemProcedimentos() {
        String path = "src/test/resources/prontuario_exportado_sem_procedimentos.csv";

        Prontuario prontuario = null;

        try {
            prontuario = new Prontuario(null).carregueProntuario(path);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            fail(ioException.getMessage());
        }

        assertEquals("Ruither Silveira", prontuario.getNomePaciente());
        assertEquals(0, prontuario.getProcedimentos().size());
        Internacao internacao = prontuario.getInternacao();
        assertEquals(10, internacao.getQtdeDias());
        assertEquals(TipoLeito.APARTAMENTO, internacao.getTipoLeito());
    }

    @Test
    public void testCarregarArquivoCompleto() {
        String path = "src/test/resources/prontuario_exportado_completo.csv";

        Prontuario prontuario = null;

        try {
            prontuario = new Prontuario(null).carregueProntuario(path);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        assertEquals("Adalgisa da Silva", prontuario.getNomePaciente());
        Internacao internacao = prontuario.getInternacao();
        assertEquals(20, internacao.getQtdeDias());
        assertEquals(TipoLeito.ENFERMARIA, internacao.getTipoLeito());

        Map<TipoProcedimento, Long> procedimentosAgrupados = prontuario.getProcedimentos().stream().collect(
                Collectors.groupingBy(Procedimento::getTipoProcedimento, Collectors.counting()));

        assertEquals(20L, procedimentosAgrupados.get(TipoProcedimento.BASICO).longValue());
        assertEquals(15L, procedimentosAgrupados.get(TipoProcedimento.AVANCADO).longValue());
        assertNull(procedimentosAgrupados.get(TipoProcedimento.COMUM));
    }

}
