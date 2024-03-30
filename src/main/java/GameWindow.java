import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 555;
    private static final int WINDOW_WIDTH = 507;
    private static final int WINDOW_POSX = 800;  // положение окна по оси х
    private static final int WINDOW_POSY = 300;  // положение окна по оси y

    JButton btnStart = new JButton("New Game");  // New Game - будет  отображаться на кнопке
    JButton btnExit = new JButton("Exit");
    Map map;
    SettingsWindow settings;

        GameWindow() {  // описываем свойства окна в конструкторе
            setDefaultCloseOperation(EXIT_ON_CLOSE);  // программа прерывеется при закрытии окна, иначе окно станет невидимым и программа не завершится
            setLocation(WINDOW_POSX, WINDOW_POSY);
            setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            setTitle("TicTacToe");  // устанавливаем заголовок
            setResizable(false);  // запрещаем менять размер окна

            map = new Map();
            settings = new  SettingsWindow(this);  // передаём окну настроек экземпляр текущего окна
            btnExit.addActionListener(new ActionListener() {  // у объекта-кнопки вызывается метод добавления к объекту-кнопке некоторого слушателя действия (нажатия на кнопку)
                // в аргумент метода добавления передаётся новый объект класса "слушатель действия", у которого переопределяется метод "действие произошло"
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);  // добавили действие при нажатии на кнопку выхода, приложение завершает работу
                }
            });

            btnStart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    settings.setVisible(true);  // делаем видимым окно настроек при нажатии кнопки-старта игры
                }
            });

            JPanel panBottom = new JPanel(new GridLayout(1,2));  // добавили панель с кнопками
            panBottom.add(btnStart);  // добавили кнопку на панель
            panBottom.add(btnExit);  // добавили кнопку на панель
            add(panBottom, BorderLayout.SOUTH);  // на панели утановлен собственный компановщик (GridLayout)
            add(map);
            setVisible(true);  // по умолчанию окно невидимое, делаем видимым
            }

    void startNewGame(int mode, int fSzX, int fSzY, int wLen) {
        map.startNewGame(mode, fSzX, fSzY, wLen);
    }
}
