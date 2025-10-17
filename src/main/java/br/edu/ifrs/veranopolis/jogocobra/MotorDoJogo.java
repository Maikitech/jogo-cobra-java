package br.edu.ifrs.veranopolis.jogocobra;

import javax.swing.SwingUtilities;


 //Controla o "coração" do jogo.
 
public class MotorDoJogo implements Runnable {

    //referências para a janela principal e o painel do jogo, para poder interagir com eles.
    private final TelaJogo tela;
    private final SnakePanel painel;

    // controla se o loop do jogo deve continuar rodando.
    // 'volatile' garante que a mudança no valor seja vista por todas as threads imediatamente.
    private volatile boolean running = false;

    //variaveis que controlam a velocidade do jogo.
    private long delay; // O tempo de pausa entre cada movimento em milissegundos.
    private static final long INITIAL_DELAY = 150; // Velocidade inicial (mais alto = mais lento).
    private static final long MIN_DELAY = 40;      // A velocidade máxima que o jogo pode atingir.
    private static final long SPEED_INCREMENT = 5; // O quanto o jogo acelera a cada fruta comida.

    
     //Construtor do motor. Recebe a tela e o painel para poder controlá-los.
     
    public MotorDoJogo(TelaJogo tela, SnakePanel painel) {
        this.tela = tela;
        this.painel = painel;
        this.delay = INITIAL_DELAY; // Inicia com a velocidade padrão.
    }

    //Inicia a execução do jogo.
    
    public void start() {
        this.running = true;
        this.delay = INITIAL_DELAY; // Reseta a velocidade para o caso de ser um novo jogo.
        Thread gameThread = new Thread(this); // Cria uma nova thread para este motor.
        gameThread.start(); // Inicia a thread, que vai chamar o método 'run()'.
    }


// para a execução do jogo de forma segura.
 
    public void stop() {
        this.running = false;
    }


//Este é o método principal que roda em loop na thread separada.

    @Override
    public void run() {
        // O loop principal do jogo, continua enquanto 'running' for verdadeiro.
        while (running) {
            // Executa um passo da lógica do jogo (mover a cobra, etc.)
            StepResult resultado = painel.passo();
            SwingUtilities.invokeLater(() -> {
                tela.atualizarStatus("Tamanho: " + painel.getTamanhoCobra());
            });

            // Analisa o que aconteceu no último passo.
            switch (resultado) {
                case COLISAO:
                    stop(); // Para o loop do jogo.
                    // Agenda a exibição da tela de "Fim de Jogo" na thread do Swing.
                    SwingUtilities.invokeLater(() -> {
                        tela.fimDeJogo();
                    });
                    break;

                case COME_FRUTA:
                    painel.spawnFruit(); // Cria uma nova fruta.
                    // Aumenta a velocidade do jogo diminuindo o tempo de pausa (delay).
                    if (delay > MIN_DELAY) {
                        delay -= SPEED_INCREMENT;
                    }
                    break;

                case OK:
                    // Se nada de especial aconteceu, apenas continua o loop.
                    break;
            }

            // Pausa a thread pelo tempo definido em 'delay'. Isso controla a velocidade da cobra.
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                // Se a thread for interrompida, paramos o loop de forma limpa.
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}