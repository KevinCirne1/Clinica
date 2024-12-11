public class Internacao {

    private TipoLeito tipoLeito;
    private int qtdeDias;

    public Internacao(TipoLeito tipoLeito, int qtdeDias) {
        this.tipoLeito = tipoLeito;
        this.qtdeDias = qtdeDias;
    }

    public TipoLeito getTipoLeito() {
        return tipoLeito;
    }

    public int getQtdeDias() {
        return qtdeDias;
    }

    @Override
    public String toString() {
        return tipoLeito + " - " + qtdeDias + " dias";
    }
}

