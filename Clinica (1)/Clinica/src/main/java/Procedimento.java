public class Procedimento {

    private TipoProcedimento tipoProcedimento;

    public Procedimento(TipoProcedimento tipoProcedimento) {
        this.tipoProcedimento = tipoProcedimento;
    }

    public TipoProcedimento getTipoProcedimento() {
        return tipoProcedimento;
    }

    @Override
    public String toString() {
        return tipoProcedimento.name();
    }
}
