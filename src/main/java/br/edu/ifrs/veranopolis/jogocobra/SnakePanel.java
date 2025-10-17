/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifrs.veranopolis.jogocobra;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.swing.JPanel;
import java.util.Random;
import java.awt.GradientPaint;
/**
 * Painel principal do jogo. É aqui que toda a lógica de movimento e o desenho da cobra e das frutas acontecem.
 */
public class SnakePanel extends JPanel {

    // Constantes para representar as direções. Fica mais legível que usar só números.
    public static final int CIMA = 0, BAIXO = 1, ESQ = 2, DIR = 3;

    // Gerador de números aleatórios para a posição da fruta.
    private final Random rand = new Random();
    
    // ---- Configurações da tela do jogo ----
    private final int cols; // Quantidade de colunas do grid.
    private final int rows; // Quantidade de linhas do grid.
    private final int tile; // Tamanho de cada "quadradinho" (tile) em pixels.

    // ---- Estado atual do jogo ----
    // Usamos um Deque (fila dupla) para a cobra. A cabeça é o primeiro elemento.
    private final Deque<Point> cobra = new ArrayDeque<>();
    private int direcao = DIR; // Direção atual do movimento.
    private Fruta fruta;       // A fruta que está na tela.

    /**
     * Construtor do painel do jogo.
     * @param cols Número de colunas.
     * @param rows Número de linhas.
     * @param tile Tamanho de cada tile em pixels.
     */
    public SnakePanel(int cols, int rows, int tile) {
        this.cols = cols;
        this.rows = rows;
        this.tile = tile;
        // Define o tamanho preferencial do painel e a cor de fundo.
        setPreferredSize(new Dimension(cols * tile, rows * tile));
        setBackground(Color.BLACK);
        // Prepara o jogo para começar.
        reset();
    }

    // -------- Métodos para controlar o jogo de fora --------
    
    /**
     * Reseta o jogo para o estado inicial.
     */
    public void reset() {
        cobra.clear(); // Limpa a cobra antiga.
        // Coloca a cabeça da cobra no centro da tela.
        int cx = cols / 2, cy = rows / 2;
        cobra.addFirst(new Point(cx, cy));
        // Adiciona um pedaço do corpo atrás da cabeça.
        cobra.addLast(new Point(cx - 1, cy));
        direcao = DIR; // A cobra sempre começa indo para a direita.
        fruta = null;  // Nenhuma fruta no início.
        repaint();     // Pede para a tela se redesenhar.
    }

    // Define uma nova direção para a cobra.
    public void setDirecao(int novaDirecao) {
        this.direcao = novaDirecao;
    }

    // Retorna a direção atual.
    public int getDirecao() {
        return direcao;
    }

    // (Outros getters e setters que permitem a classe principal acessar informações do jogo)
    public void setFruta(Fruta fruta) { this.fruta = fruta; repaint(); }
    public Fruta getFruta() { return fruta; }
    public int getLarguraGrid() { return cols; }
    public int getAlturaGrid() { return rows; }
    public int getTamanhoCobra() { return cobra.size(); }

    // -------- Lógica Principal do Jogo --------
    
    /**
     * Executa um "passo" do jogo: move a cobra e verifica colisões.
     * @return O resultado do passo (OK, COME_FRUTA ou COLISAO).
     */
    public StepResult passo() {
        if (cobra.isEmpty()) {
            return StepResult.COLISAO; // Se não tem cobra, deu algum problema.
        }

        // Pega a posição da cabeça atual para calcular a próxima.
        Point cabeca = cobra.peekFirst();
        int nx = cabeca.x, ny = cabeca.y;
        
        // Atualiza as coordenadas da nova cabeça com base na direção.
        switch (direcao) {
            case CIMA -> ny--;
            case BAIXO -> ny++;
            case ESQ -> nx--;
            case DIR -> nx++;
        }

        // Verifica colisão com as paredes.
        if (nx < 0 || nx >= cols || ny < 0 || ny >= rows) {
            return StepResult.COLISAO;
        }

        // Verifica se a cobra vai comer a fruta na próxima jogada.
        boolean vaiComer = (fruta != null && fruta.getX() == nx && fruta.getY() == ny);

        // Verifica colisão com o próprio corpo.
        // O truque aqui é ignorar a última parte da cauda, pois ela vai sair do lugar
        // no próximo movimento (se a cobra não comer a fruta).
        if (colideComCorpo(nx, ny, !vaiComer)) {
            return StepResult.COLISAO;
        }

        // Se chegou até aqui, o movimento é válido. Adiciona a nova cabeça.
        cobra.addFirst(new Point(nx, ny));

        if (vaiComer) {
            // Se comeu, a cauda antiga não é removida (a cobra cresce).
            fruta = null; // A fruta "some". A classe externa vai criar outra.
            repaint();
            return StepResult.COME_FRUTA;
        } else {
            // Se não comeu, a cobra apenas anda, então removemos a última parte da cauda.
            cobra.removeLast();
            repaint();
            return StepResult.OK;
        }
    }

    /**
     * Verifica se uma coordenada (x, y) colide com o corpo da cobra.
     */
    private boolean colideComCorpo(int x, int y, boolean ignorarUltimo) {
        int i = 0;
        // Define até onde verificar: o corpo todo ou tudo menos a cauda.
        int limite = cobra.size() - (ignorarUltimo ? 1 : 0);
        for (Point p : cobra) {
            if (i >= limite) break;
            if (p.x == x && p.y == y) return true; // Achou uma colisão.
            i++;
        }
        return false; // Nenhuma colisão encontrada.
    }

    /**
     * Cria uma nova fruta em uma posição aleatória que não esteja ocupada pela cobra.
     */
    public boolean spawnFruit() {
        // Se a cobra preencheu a tela toda, não tem onde criar fruta.
        if (cobra.size() >= cols * rows) {
            fruta = null;
            return false;
        }

        int x, y;
        boolean ocupado;
        // Loop para garantir que a fruta não apareça em cima da cobra.
        do {
            x = rand.nextInt(cols);
            y = rand.nextInt(rows);
            ocupado = false;
            for (Point p : cobra) {
                if (p.x == x && p.y == y) {
                    ocupado = true;
                    break;
                }
            }
        } while (ocupado);

        fruta = new Fruta(x, y);
        repaint();
        return true;
    }

    // -------- Desenho dos Componentes na Tela --------
    
    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D gg = (Graphics2D) g.create();
    // Ativa o anti-aliasing pra deixar os desenhos mais suaves.
    gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Desenha as linhas do grid (opcional, mas ajuda na visualização).
    gg.setColor(new Color(40, 40, 40));
    for (int c = 0; c <= cols; c++) gg.drawLine(c * tile, 0, c * tile, rows * tile);
    for (int r = 0; r <= rows; r++) gg.drawLine(0, r * tile, cols * tile, r * tile);

    // Desenha a fruta na tela.
    if (fruta != null) {
        gg.setColor(new Color(220, 40, 40)); // Um vermelho mais vivo
        int fx = fruta.getX() * tile;
        int fy = fruta.getY() * tile;
        gg.fillOval(fx + 2, fy + 2, tile - 4, tile - 4);
        gg.setColor(new Color(255, 150, 150)); // Um brilho na fruta
        gg.fillOval(fx + 4, fy + 4, tile - 8, tile - 8);
    }

    // Pega uma referência ao rabo da cobra para pintá-lo diferente
    Point rabo = cobra.peekLast();
    int idx = 0;

    // Desenha a cobra, parte por parte.
    for (Point p : cobra) {
        int x = p.x * tile;
        int y = p.y * tile;

        if (idx == 0) { // --- Estilizando a CABEÇA ---
            // Cria um gradiente legal pra cabeça
            Color corClara = new Color(100, 255, 120);
            Color corEscura = new Color(60, 160, 100);
            gg.setPaint(new GradientPaint(x, y, corClara, x + tile, y + tile, corEscura));
            gg.fillRoundRect(x + 1, y + 1, tile - 2, tile - 2, 8, 8);

            // Adiciona o contorno
            gg.setColor(Color.DARK_GRAY);
            gg.drawRoundRect(x + 1, y + 1, tile - 2, tile - 2, 8, 8);

            // --- Desenha os OLHOS ---
            gg.setColor(Color.BLACK);
            int eyeSize = tile / 5; // Tamanho do olho
            
            // A posição dos olhos muda com a direção da cobra
            switch (direcao) {
                case DIR:
                    gg.fillOval(x + tile / 2, y + tile / 4 - eyeSize / 2, eyeSize, eyeSize);
                    gg.fillOval(x + tile / 2, y + (tile * 3) / 4 - eyeSize / 2, eyeSize, eyeSize);
                    break;
                case ESQ:
                    gg.fillOval(x + tile / 2 - eyeSize, y + tile / 4 - eyeSize / 2, eyeSize, eyeSize);
                    gg.fillOval(x + tile / 2 - eyeSize, y + (tile * 3) / 4 - eyeSize / 2, eyeSize, eyeSize);
                    break;
                case CIMA:
                    gg.fillOval(x + tile / 4 - eyeSize / 2, y + tile / 2 - eyeSize, eyeSize, eyeSize);
                    gg.fillOval(x + (tile * 3) / 4 - eyeSize / 2, y + tile / 2 - eyeSize, eyeSize, eyeSize);
                    break;
                case BAIXO:
                    gg.fillOval(x + tile / 4 - eyeSize / 2, y + tile / 2, eyeSize, eyeSize);
                    gg.fillOval(x + (tile * 3) / 4 - eyeSize / 2, y + tile / 2, eyeSize, eyeSize);
                    break;
            }

        } else { //Estilizando o CORPO e o RABO
            
            // Se for o rabo, usa uma cor um pouco mais escura
            if (p.equals(rabo)) {
                 gg.setColor(new Color(40, 120, 80));
            } else {
                 gg.setColor(new Color(60, 160, 100)); // Cor normal do corpo
            }
            
            gg.fillRoundRect(x + 1, y + 1, tile - 2, tile - 2, 6, 6);

            // Adiciona o contorno pro corpo também
            gg.setColor(Color.DARK_GRAY);
            gg.drawRoundRect(x + 1, y + 1, tile - 2, tile - 2, 6, 6);
        }

        idx++;
    }
    
    // Libera os recursos gráficos.
    gg.dispose();
}
}