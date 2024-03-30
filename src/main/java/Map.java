import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Map extends JPanel {  // сделаем игровую панель к-н
    private static final Random RANDOM = new Random();
    private static final int DOT_PADDING = 5;
    private static final int STATE_DRAW = 0;
    private static final int STATE_WIN_HUMAN = 1;
    private static final int STATE_WIN_AI = 2;
    private static final String MSG_DRAW = "Draw!";
    private static final String MSG_WIN_HUMAN = "Human win!";
    private static final String MSG_WIN_AI = "Computer win!";
    private int gameOverType;
    private final int HUMAN_DOT = 1;
    private final int AI_DOT = 2;
    private final int EMPTY_DOT = 0;
    private int fieldSizeX = 3;
    private int fieldSizeY = 3;
    private char[][] field;
    private int panelWidth;
    private int panelHeight;
    private int cellHeight;
    private int cellWidth;
    private boolean isGameOver;
    private boolean isInitialized;

    Map(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e){
                update(e);
            }
        });
        isInitialized = false;
    }
    private void update(MouseEvent e) {
        if (isGameOver || !isInitialized) {
            return;
        }
        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;
        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellX, cellY)) {
            return;
        }
        field[cellY][cellX] = HUMAN_DOT;
        repaint();
        if (checkEndGame(HUMAN_DOT, STATE_WIN_HUMAN)) {
            return;
        }
        aiTurn();
        repaint();
        if (checkEndGame(AI_DOT, STATE_WIN_AI)) {
            return;
        }
    }


    // Напишем метод старта новой игры:
    // mode - режим игры (компьютер против игрока / игрок против компьютера)
    // fSzX - размерность поля по x
    // fSzY - размерность поля по y
    // wLen - выигрышная длина (число крестиков / ноликов, необходимых для победы игрока)
    void startNewGame(int mode, int fSzX, int fSzY, int wLen) {
        System.out.printf("Mode: %d;\nSize: x=%d, y=%d;\nWin length: %d\n", mode, fSzX, fSzY, wLen);
        initMap();
        isGameOver = false;
        isInitialized = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g){
        if (!isInitialized) return;
        panelWidth = getWidth();
        panelHeight = getHeight();
        cellHeight = panelHeight / 3;
        cellWidth = panelWidth / 3;

        g.setColor(Color.BLACK);
        for (int h = 0; h < 3; h++) {
            int y = h * cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }

        for (int w = 0; w < 3; w++) {
            int x = w * cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }

        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == EMPTY_DOT) {
                    continue;
                }
                if (field[y][x] == HUMAN_DOT) {
                    g.setColor(Color.BLUE);
                    g.fillOval(x * cellWidth + DOT_PADDING, y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2, cellHeight - DOT_PADDING * 2);
                } else if (field[y][x] == AI_DOT) {
                    g.setColor(Color.RED);
                    g.fillOval(x * cellWidth + DOT_PADDING, y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2, cellHeight - DOT_PADDING * 2);
                } else {
                    throw new RuntimeException("Unexpected value " + field[y][x] +
                            " in cell: x = " + x + " y = " + y);
                }
            }
        }

        if (isGameOver) {
            showMessageGameOver(g);
        }
    }

    private void showMessageGameOver(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Times new roman", Font.BOLD, 48));
        switch (gameOverType) {
            case STATE_DRAW -> g.drawString(MSG_DRAW, 180, getHeight() / 2);
            case STATE_WIN_AI -> g.drawString(MSG_WIN_AI, 100, getHeight() / 2);
            case STATE_WIN_HUMAN -> g.drawString(MSG_WIN_HUMAN, 110, getHeight() / 2);
            default -> throw new RuntimeException("Unexpected gameOver state: " + gameOverType);
        }
    }

    private void initMap() {
        fieldSizeX = 3;
        fieldSizeY = 3;
        field = new char[fieldSizeX][fieldSizeY];
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                field[i][j] = EMPTY_DOT;
            }
        }
    }

    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    private boolean isEmptyCell(int x, int y) {
        return field[y][x] == EMPTY_DOT;
    }

    private void aiTurn() {
        int x;
        int y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = AI_DOT;
    }

    private boolean checkWin(int symbol) {
        // Проверка по горизонталям
        if (field[0][0] == symbol && field[0][1] == symbol && field[0][2] == symbol) return true;
        if (field[1][0] == symbol && field[1][1] == symbol && field[1][2] == symbol) return true;
        if (field[2][0] == symbol && field[2][1] == symbol && field[2][2] == symbol) return true;
        // Проверка по диагоналям
        if (field[0][0] == symbol && field[1][1] == symbol && field[2][2] == symbol) return true;
        if (field[0][2] == symbol && field[1][1] == symbol && field[2][0] == symbol) return true;
        // Проверка по трем вертикалям
        if (field[0][0] == symbol && field[1][0] == symbol && field[2][0] == symbol) return true;
        if (field[0][1] == symbol && field[1][1] == symbol && field[2][1] == symbol) return true;
        if (field[0][2] == symbol && field[1][2] == symbol && field[2][2] == symbol) return true;
        return false;
    }

    private boolean isMapFull() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean checkEndGame(int dot, int gameOverType) {
        if (checkWin(dot)) {
            this.gameOverType = gameOverType;
            isGameOver = true;
            repaint();
            return true;
        }
        if (isMapFull()) {
            this.gameOverType = STATE_DRAW;
            isGameOver = true;
            repaint();
            return true;
        }
        return false;
    }

}
