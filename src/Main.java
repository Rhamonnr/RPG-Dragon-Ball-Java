import java.util.Arrays;
import java.util.List;

interface HabilidadeEspecial {
    String getNome();

    boolean usarPoder(Personagem alvo);
}

class PoderEspecial implements HabilidadeEspecial {
    private final String nome;
    private final int dano;

    public PoderEspecial(String nome, int dano) {
        this.nome = nome;
        this.dano = dano;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public boolean usarPoder(Personagem alvo) {
        return alvo.receberDano(dano);
    }
}

abstract class Personagem {
    private final String nome;
    private int vida;
    private final int ataque;
    private final HabilidadeEspecial habilidade;

    public Personagem(String nome, int vida, int ataque, HabilidadeEspecial habilidade) {
        this.nome = nome;
        this.vida = vida;
        this.ataque = ataque;
        this.habilidade = habilidade;
    }

    public String getNome() {
        return nome;
    }

    public int getVida() {
        return vida;
    }

    public int getAtaque() {
        return ataque;
    }

    public String getNomePoder() {
        return habilidade.getNome();
    }

    public boolean atacar(Personagem alvo) {
        return alvo.receberDano(ataque);
    }

    public boolean usarPoder(Personagem alvo) {
        return habilidade.usarPoder(alvo);
    }

    public boolean receberDano(int valor) {
        vida = Math.max(0, vida - valor);
        return vida == 0;
    }
}

class Goku extends Personagem {
    public Goku() {
        super("Goku", 100, 15, new PoderEspecial("Kamehameha", 30));
    }
}

class Vegeta extends Personagem {
    public Vegeta() {
        super("Vegeta", 100, 18, new PoderEspecial("Final Flash", 35));
    }
}

class Freeza extends Personagem {
    public Freeza() {
        super("Freeza", 120, 20, new PoderEspecial("Raio Mortal", 40));
    }
}

interface Apresentador {
    void mostrarMensagem(String mensagem);

    void mostrarStatus(Personagem personagem);

    void mostrarAtaque(Personagem atacante, Personagem alvo);

    void mostrarUsoPoder(Personagem atacante, Personagem alvo);

    void mostrarDerrota(Personagem personagem);
}

class ApresentadorConsole implements Apresentador {
    @Override
    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    @Override
    public void mostrarStatus(Personagem personagem) {
        System.out.println("Nome: " + personagem.getNome());
        System.out.println("Vida: " + personagem.getVida());
        System.out.println("Ataque: " + personagem.getAtaque());
    }

    @Override
    public void mostrarAtaque(Personagem atacante, Personagem alvo) {
        System.out.println(atacante.getNome() + " está atacando " + alvo.getNome() + "!");
    }

    @Override
    public void mostrarUsoPoder(Personagem atacante, Personagem alvo) {
        System.out.println(atacante.getNome() + " está usando " + atacante.getNomePoder()
                + " em " + alvo.getNome() + "!");
    }

    @Override
    public void mostrarDerrota(Personagem personagem) {
        System.out.println(personagem.getNome() + " foi derrotado!");
    }
}

class Batalha {
    private final Apresentador apresentador;

    public Batalha(Apresentador apresentador) {
        this.apresentador = apresentador;
    }

    public void mostrarMensagem(String mensagem) {
        apresentador.mostrarMensagem(mensagem);
    }

    public void atacar(Personagem atacante, Personagem alvo) {
        apresentador.mostrarAtaque(atacante, alvo);
        if (atacante.atacar(alvo)) {
            apresentador.mostrarDerrota(alvo);
        }
    }

    public void usarPoder(Personagem atacante, Personagem alvo) {
        apresentador.mostrarUsoPoder(atacante, alvo);
        if (atacante.usarPoder(alvo)) {
            apresentador.mostrarDerrota(alvo);
        }
    }

    public void mostrarStatus(List<Personagem> personagens) {
        for (Personagem personagem : personagens) {
            apresentador.mostrarStatus(personagem);
            apresentador.mostrarMensagem("======================================\n");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Personagem goku = new Goku();
        Personagem vegeta = new Vegeta();
        Personagem freeza = new Freeza();
        List<Personagem> personagens = Arrays.asList(goku, vegeta, freeza);
        Batalha batalha = new Batalha(new ApresentadorConsole());

        batalha.mostrarMensagem("==== Personagens ====\n");
        batalha.mostrarStatus(personagens);

        batalha.mostrarMensagem("\n==== Batalha começa! ====\n");
        batalha.atacar(goku, freeza);
        batalha.atacar(vegeta, freeza);
        batalha.usarPoder(freeza, goku);

        batalha.mostrarMensagem("\n==== Status após 1° batalha: ====\n");
        batalha.mostrarStatus(personagens);

        batalha.usarPoder(goku, freeza);
        batalha.usarPoder(vegeta, freeza);
        batalha.usarPoder(freeza, vegeta);
        batalha.atacar(goku, freeza);
        batalha.atacar(vegeta, freeza);

        batalha.mostrarMensagem("\n==== Status após 2° batalha: ====\n");
        batalha.mostrarStatus(personagens);
    }
}
