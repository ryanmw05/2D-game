package com.company;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main extends JFrame implements ActionListener, KeyListener, ChangeListener{

    private static final long serialVersionUID = 445134924344204802L;

    //Declaring different objects and variables that make up the program
    private JButton jBActBtn, jBRunBtn, jBResetBtn;

    //The menubar and its components
    private JMenuBar mb;
    private JMenu scenario;
    private JMenuItem newStrideScenario, newJavaScenario, open, close, saveAs, quit;
    private JMenu edit;
    private JMenuItem newClass, importClass, deleteClass, convertToJava, preferences;
    private JMenu controls;
    private JMenuItem act, run, pause, reset, saveTheWorld;
    private JMenu help;
    private JMenuItem about, copyright;

    //The three main panels within the frame
    private JPanel jPMaze, jPBottomPanel, jPRightPanel;

    //Borders for the different panels or anywhere necessary
    Border lightGreyBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
    Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

    //Icon declaration for different buttons
    private Icon actIcon, runIcon, pauseIcon, resetIcon;

    //The slider for the bottom panel and labels for the slider
    private JSlider slider;
    private JLabel sliderLabel1, sliderLabel2, sliderLabel3, sliderLabel4, sliderLabel5;

    //The right panel option, square, direction and score labels and text-fields
    private JLabel jLOption, jLSquare, jLDirection, jLScore;
    private JTextField jTFOptionText, jTFSquareText, jTFDirectionText, jTFScoreText;

    //Digital Timer portion
    private JLabel jLDigitalTimer;
    private JTextField timerHours, timerMinutes, timerSeconds;
    private JLabel jLTimerSeparatorOne, jLTimerSeparatorTwo;

    //The up down left right and the empty buttons for the right panel
    private JButton upButton, leftButton, rightButton, downButton;
    private JButton[] emptyButton = new JButton[5];
    private JButton jBOption1, jBOption2, jBOption3, jBExit;

    //The maze 2d array labels and icons
    private JLabel[][] jLMazeLabels = new JLabel[20][20];
    Icon goldenBallIcon = new ImageIcon("images/sand60x60.png");
    Icon sandBlockIcon = new ImageIcon("images/sand.jpg");
    Icon greyBlockIcon = new ImageIcon("images/sandstone.jpg");
    Icon enemyIcon = new ImageIcon("images/enemy.jpg");
    Icon cherryIcon = new ImageIcon("images/cherries.jpg");

    //The grid-bag layout constraint
    GridBagConstraints c = new GridBagConstraints();

    //The counter used to move the ball and get its position
    private int xCounter = 15, yCounter = 0;

    //The right panel direction icon set to change according to the faced direction
    private JLabel jLDirectionIcon;
    private Icon directionImageWest = new ImageIcon("images/west.jpg");
    private Icon directionImageEast = new ImageIcon("images/east.jpg");
    private Icon directionImageNorth = new ImageIcon("images/north.jpg");
    private Icon directionImageSouth = new ImageIcon("images/south.jpg");

    //Different timers used for different purposes.
    private Timer timerForRun = null;
    private Timer timerForBall = null;
    private Timer timerIntermediate = null;
    private Timer timerAdvanced = null;
    private Timer enemyMoveTimer = null;

    //Different integers declared for the time counter values
    private int nextSeconds = 0;
    private int currentSeconds = 0;
    private int currentMinutes = 0;
    private int nextMinutes = 0;
    private int currentHours = 0;
    private int nextHours = 0;

    /*
     * Strings declared to be used in the moveBall() and move methods.
     *	These strings can be passed in the move method as move(MOVE_LEFT)
     *	When the ball is to be moved left
     */

    private String MOVE_LEFT = "left";
    private String MOVE_DOWN = "down";
    @SuppressWarnings("unused")
    private String MOVE_RIGHT = "right";
    @SuppressWarnings("unused")
    private String MOVE_UP = "up";


    //Importing audio files as URL and the audio clips for different sounds
    URL fallSoundUrl = null;
    AudioClip fallSoundClip = null;
    URL winSoundUrl = null;
    AudioClip winSoundClip = null;
    URL loseSoundUrl = null;
    AudioClip loseSoundClip = null;

    //The variables used to position and move the enemy
    int enemyOneX = 0;
    int enemyTwoX = 15;
    boolean enemyOneCanMoveRight = true;
    boolean enemyTwoCanMoveRight = true;

    //The icons used within the JOptionPane to display win or game over messages
    private Icon winIcon = new ImageIcon("images/winIcon.png");
    private Icon loseIcon = new ImageIcon("images/loseIcon.png");

    /*
     * The constructor(The main frame of the application)
     * The set size is 775x650
     * Dock icon is set for the application
     * The frame is set to be restricted in resizing and appear across the middle of the screen
     */

    Main(){
        super ("2D Ball Maze Game"); //Sets the title of the program
        setSize(775, 650); //Sets the size of the program to 775x650
        setIconImage(new ImageIcon("images/greenfoot.png").getImage()); //Sets the dock icon
        setLayout(null); //Sets the layout of the main frame to null
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Sets default close operation as exit
        createMenuBar();
        setJMenuBar(mb); //Sets the menu bar to mb
        setResizable(false); //Makes the window non-resizable
        setLocationRelativeTo(null); //Brings the window to the monitor's centre
        createjPMaze();
        createjPBottomPanel();
        createjPRightPanel();
        addKeyListener(this); //Adds keylistener for this jframe
        setFocusable(true); //Makes this frame focusable
        setVisible(true); //Makes this frame visible
    }


    /*
     *
     * 	This method deals with Creating the Menu bar and adding items to the menu bar.
     * 	It also deals with the different events associated with the Menu items.
     *
     */

    private void createMenuBar() {
        mb = new JMenuBar();

        //The Scenario Menu and its menu-items

        scenario = new JMenu("Scenario");

        newStrideScenario = new JMenuItem("New Stride Scenario");
        newJavaScenario = new JMenuItem("New Java Scenario");
        open = new JMenuItem("Open");
        close = new JMenuItem("Close");
        saveAs = new JMenuItem("Save as");
        saveAs.addActionListener(this);

        quit = new JMenuItem("Quit");
        quit.addActionListener(this);

        scenario.add(newStrideScenario);
        scenario.add(newJavaScenario);
        scenario.add(open);
        scenario.add(close);
        scenario.add(saveAs);
        scenario.add(quit);
        mb.add(scenario);

        //The Edit menu
        edit = new JMenu("Edit");

        newClass = new JMenuItem("New Class");
        importClass = new JMenuItem("Import Class");
        deleteClass = new JMenuItem("Delete Class");
        convertToJava = new JMenuItem("Convert to Java Scenario");
        preferences = new JMenuItem("Preferences");

        edit.add(newClass);
        edit.add(importClass);
        edit.add(deleteClass);
        edit.add(convertToJava);
        edit.add(preferences);

        mb.add(edit);

        //The controls menu
        controls = new JMenu("Controls");

        act = new JMenuItem("Act");
        act.addActionListener(this);
        run = new JMenuItem("Run");
        run.addActionListener(this);
        pause = new JMenuItem("Pause");
        pause.addActionListener(this);
        reset = new JMenuItem("Reset");
        reset.addActionListener(this);
        saveTheWorld = new JMenuItem("Save the World");

        controls.add(act);
        controls.add(run);
        controls.add(pause);
        controls.add(reset);
        controls.add(saveTheWorld);

        mb.add(controls);

        //The help menu
        help = new JMenu("Help");
        about = new JMenuItem("About");
        copyright = new JMenuItem("Copyright");
        about.addActionListener(this);

        help.add(about);
        help.add(copyright);

        mb.add(help);
    }

    /* ------------------------- THE  MAIN METHOD -------------------------*/
    public static void main(String[] args) {
        new Main();
    }


    //This method creates maze panel
    private void createjPMaze() {
        Container window = getContentPane();
        jPMaze = new JPanel();
        jPMaze.setBackground(Color.white);
        jPMaze.setBounds(0, 0, 610, 497);
        jPMaze.setBorder(etchedBorder);
        jPMaze.setLayout(new GridBagLayout());
        createjPMazeLayout();
        window.add(jPMaze);
    }

    //This method creates the maze panel's layout (the sand-blocks and the ball)
    private void createjPMazeLayout() {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 15;
        c.gridy = 0;

        //The initial position of the ball
        jLMazeLabels[c.gridx][c.gridy] = new JLabel(goldenBallIcon);
        jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);

        //The first full row of the sand-blocks and the columns follow
        c.gridx = 0;
        c.gridy = 0;
        for (int i = 0; i<=14; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridx++;
        }

        c.gridx=1;
        c.gridy=1;

        for(int i = 1; i<=2; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridy++;
        }

        c.gridx=5;
        c.gridy=1;
        for(int i = 1; i<=2; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridy++;
        }

        c.gridx=9;
        c.gridy=1;
        for(int i = 1; i<=2; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridy++;
        }


        //The second full row of the sand-blocks and the columns follow
        c.gridx = 0;
        c.gridy = 3;
        for (int i = 0; i<16; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridx++;
        }

        c.gridx=2;
        c.gridy=4;
        for(int i = 4; i<=5; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridy++;
        }

        c.gridx=6;
        c.gridy=4;
        for(int i = 4; i<=5; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridy++;
        }

        c.gridx=11;
        c.gridy=4;
        for(int i = 4; i<=5; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridy++;
        }

        //The third full row of the sand-blocks and the columns follow
        c.gridx = 0;
        c.gridy = 6;
        for (int i = 0; i<16; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridx++;
        }

        c.gridx=1;
        c.gridy=7;

        for(int i = 7; i<=8; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridy++;
        }

        c.gridx=5;
        c.gridy=7;
        for(int i = 7; i<=8; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridy++;
        }

        c.gridx=12;
        c.gridy=7;
        for(int i = 7; i<=8; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridy++;
        }

        //The third full row of the sand-blocks and the columns follow.
        c.gridx = 0;
        c.gridy = 9;
        for (int i = 0; i<16; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridx++;
        }

        c.gridx=2;
        c.gridy=10;
        for(int i = 10; i<=11; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridy++;
        }

        c.gridx=6;
        c.gridy=10;
        for(int i = 10; i<=11; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridy++;
        }

        //The last full row of the sand-blocks. This is the end of the maze
        c.gridx = 1;
        c.gridy = 12;
        for (int i = 0; i<15; i++) {
            jLMazeLabels[c.gridx][c.gridy] = new JLabel(sandBlockIcon);
            jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);
            c.gridx++;
        }

        // The first column from the last row would contain the grey block icon.
        c.gridx=0;
        c.gridy=12;
        jLMazeLabels[c.gridx][c.gridy] = new JLabel(greyBlockIcon);
        jPMaze.add(jLMazeLabels[c.gridx][c.gridy], c);

    }


    /*
     *  This method creates the Bottom Panel, also adds buttons and slider to the panel.
     */
    private void createjPBottomPanel() {
        Container window = getContentPane();
        jPBottomPanel = new JPanel();

        //Icons for the buttons
        actIcon = new ImageIcon("images/step.png");
        runIcon = new ImageIcon("images/run.png");
        pauseIcon = new ImageIcon("images/pause.png");
        resetIcon = new ImageIcon("images/reset.png");

        //Act button
        jBActBtn = new JButton("Act", actIcon);
        jBActBtn.setBackground(Color.white);
        jBActBtn.addActionListener(this);
        jBActBtn.setBounds(80, 25, 90, 30);
        jBActBtn.addKeyListener(this);

        //Run button
        jBRunBtn = new JButton("Run");
        jBRunBtn.setIcon(runIcon);
        jBRunBtn.addActionListener(this);
        jBRunBtn.addKeyListener(this);
        jBRunBtn.setBackground(Color.white);
        jBRunBtn.setBounds(180, 25, 90, 30);

        //Reset button
        jBResetBtn = new JButton("Reset", resetIcon);
        jBResetBtn.addActionListener(this);
        jBResetBtn.setBackground(Color.white);
        jBResetBtn.setBounds(280, 25, 90, 30);

        //Slider
        slider = new JSlider(1, 1500);
        slider.setBounds(500, 20, 200, 30);
        slider.addChangeListener(this);

        //Slider labels	CSS has been used for coloring the labels
        sliderLabel1 = new JLabel("<html><font style = \" color: gray; font-size: 7px; \">|</font>");
        sliderLabel2 = new JLabel("<html><font style = \" color: gray; font-size: 7px; \">|</font>");
        sliderLabel3 = new JLabel("<html><font style = \" color: gray; font-size: 7px; \">|</font>");
        sliderLabel4 = new JLabel("<html><font style = \" color: gray; font-size: 7px; \">|</font>");
        sliderLabel5 = new JLabel("<html><font style = \" color: gray; font-size: 7px; \">|</font>");

        sliderLabel1.setBounds(507, 30, 10, 40);
        sliderLabel2.setBounds(552, 30, 10, 40);
        sliderLabel3.setBounds(599, 30, 10, 40);
        sliderLabel4.setBounds(645, 30, 10, 40);
        sliderLabel5.setBounds(690, 30, 10, 40);


        jPBottomPanel.add(jBActBtn);
        jPBottomPanel.add(jBRunBtn);
        jPBottomPanel.add(jBResetBtn);
        jPBottomPanel.add(slider);
        jPBottomPanel.add(sliderLabel1);
        jPBottomPanel.add(sliderLabel2);
        jPBottomPanel.add(sliderLabel3);
        jPBottomPanel.add(sliderLabel4);
        jPBottomPanel.add(sliderLabel5);

        jPBottomPanel.setLayout(null);
        jPBottomPanel.setBorder(lightGreyBorder);
        jPBottomPanel.setBounds(0, 499, 775, 120);

        window.add(jPBottomPanel);

    }

    /*
     * This method creates the right panel, also adding different components to it.
     */

    private void createjPRightPanel() {
        Container window = getContentPane();
        jPRightPanel = new JPanel();

        //Panel three the first three labels and TextFields
        jLOption = new JLabel("Option: ");
        jLSquare = new JLabel("Square: ");
        jLDirection = new JLabel("Direction: ");
        jLScore = new JLabel("Score: ");


        jLOption.setBounds(10, 10, 70, 30);
        jLSquare.setBounds(10, 50, 70, 30);
        jLDirection.setBounds(10, 90, 70, 30);
        jLScore.setBounds(10, 130, 70, 30);

        jTFOptionText = new JTextField("1");
        jTFSquareText = new JTextField("15, 0");
        jTFDirectionText = new JTextField("W");
        jTFScoreText = new JTextField("0");
        jTFOptionText.setEditable(false);
        jTFSquareText.setEditable(false);
        jTFDirectionText.setEditable(false);
        jTFScoreText.setEditable(false);
        jTFOptionText.setBackground(Color.WHITE);
        jTFSquareText.setBackground(Color.WHITE);
        jTFDirectionText.setBackground(Color.WHITE);
        jTFScoreText.setBackground(Color.WHITE);

        jTFOptionText.setBounds(75, 10, 70, 30);
        jTFSquareText.setBounds(75, 50, 70, 30);
        jTFDirectionText.setBounds(75, 90, 70, 30);
        jTFScoreText.setBounds(75, 130, 70, 30);

        jPRightPanel.add(jLOption);
        jPRightPanel.add(jLSquare);
        jPRightPanel.add(jLDirection);
        jPRightPanel.add(jLScore);

        jPRightPanel.add(jTFOptionText);
        jPRightPanel.add(jTFSquareText);
        jPRightPanel.add(jTFDirectionText);
        jPRightPanel.add(jTFScoreText);


        //Panel Three digital timer labels and text-fields
        jLDigitalTimer = new JLabel("DIGITAL TIMER");
        jLDigitalTimer.setBounds(35, 165, 150, 25);

        timerHours = new JTextField("00");
        timerHours.setForeground(Color.white);
        timerHours.setBorder(etchedBorder);
        timerHours.setBackground(Color.black);
        timerHours.setBounds(10, 195, 35, 25);

        jLTimerSeparatorOne = new JLabel(":");
        jLTimerSeparatorOne.setBounds(50, 195, 5, 25);

        timerMinutes = new JTextField("00");
        timerMinutes.setForeground(Color.white);
        timerMinutes.setBorder(etchedBorder);
        timerMinutes.setBackground(Color.black);
        timerMinutes.setBounds(60, 195, 35, 25);

        jLTimerSeparatorTwo = new JLabel(":");
        jLTimerSeparatorTwo.setBounds(100, 195, 5, 25);

        timerSeconds = new JTextField("00");
        timerSeconds.setForeground(Color.white);
        timerSeconds.setBorder(etchedBorder);
        timerSeconds.setBackground(Color.black);
        timerSeconds.setBounds(110, 195, 35, 25);

        timerSeconds.setEditable(false);
        timerMinutes.setEditable(false);
        timerHours.setEditable(false);

        jPRightPanel.add(jLDigitalTimer);
        jPRightPanel.add(timerHours);
        jPRightPanel.add(timerMinutes);
        jPRightPanel.add(timerSeconds);
        jPRightPanel.add(jLTimerSeparatorOne);
        jPRightPanel.add(jLTimerSeparatorTwo);

        //Panel Three arrow buttons
        upButton = new JButton("^");
        upButton.addActionListener(this);
        leftButton = new JButton("<");
        leftButton.addActionListener(this);
        rightButton = new JButton(">");
        rightButton.addActionListener(this);
        downButton = new JButton("v");
        downButton.addActionListener(this);

        upButton.setBackground(Color.white);
        leftButton.setBackground(Color.white);
        rightButton.setBackground(Color.white);
        downButton.setBackground(Color.white);

        upButton.setBounds(55, 230, 45, 25);
        leftButton.setBounds(10, 255, 45, 25);
        rightButton.setBounds(100, 255, 45, 25);
        downButton.setBounds(55, 280, 45, 25);

        jPRightPanel.add(upButton);
        jPRightPanel.add(leftButton);
        jPRightPanel.add(rightButton);
        jPRightPanel.add(downButton);

        //Panel Three empty buttons
        for (int i=0; i<5; i++) {
            emptyButton[i]= new JButton();
            emptyButton[i].setEnabled(false);
            jPRightPanel.add(emptyButton[i]);

        }
        emptyButton[0].setBounds(10, 230, 45, 25);
        emptyButton[1].setBounds(100, 230, 45, 25);
        emptyButton[2].setBounds(55, 255, 45, 25);
        emptyButton[3].setBounds(10, 280, 45, 25);
        emptyButton[4].setBounds(100, 280, 45, 25);


        //Panel Three option buttons
        jBOption1 = new JButton("Option 1");
        jBOption1.setFont(new Font("Serif", Font.BOLD, 10));
        jBOption1.addActionListener(this);
        jBOption2 = new JButton("Option 2");
        jBOption2.setFont(new Font("Serif", Font.BOLD, 10));
        jBOption2.addActionListener(this);
        jBOption3 = new JButton("Option 3");
        jBOption3.setFont(new Font("Serif", Font.BOLD, 10));
        jBOption3.addActionListener(this);
        jBExit = new JButton("Exit");
        jBExit.setFont(new Font("Serif", Font.BOLD, 10));
        jBExit.addActionListener(this);

        jBOption1.setBounds(3, 312, 74, 30);
        jBOption2.setBounds(79, 312, 74, 30);
        jBOption3.setBounds(3, 344, 74, 30);
        jBExit.setBounds(79, 344, 74, 30);

        jPRightPanel.add(jBOption1);
        jPRightPanel.add(jBOption2);
        jPRightPanel.add(jBOption3);
        jPRightPanel.add(jBExit);

        //Panel Three direction image
        jLDirectionIcon = new JLabel(directionImageWest);
        jLDirectionIcon.setBorder(lightGreyBorder);
        jLDirectionIcon.setBounds(30, 385, 100, 100);

        jPRightPanel.add(jLDirectionIcon);

        jPRightPanel.setBackground(Color.getHSBColor(0, 235, 1));
        jPRightPanel.setLayout(null);
        jPRightPanel.setBorder(lightGreyBorder);
        jPRightPanel.setBounds(612, 2, 156, 495);
        window.add(jPRightPanel);
    }


    /*
     * moveBall method for the act and run buttons the move(MOVE_RIGHT).. have been
     * implemented.
     *
     * Different sounds have been added to be used across multiple events
     * Such as: winning the game, the ball falling and touching the enemy.
     *
     * Additionally, the different options have different move configurations set up
     * Option 1 is the default
     * The option 2 would activate the intermediate move methods
     * In option 3, the ball is set to be moved by the user
     * THIS IS WHY THE BALL AUTO MOVES, NEED TO EDIT
     */

    private void moveBall() {
        boolean canMoveLeft = true;
        JLabel jLCheckDown = jLMazeLabels[xCounter][yCounter+1];
        String optionNumber = jTFOptionText.getText();

        try {
            fallSoundUrl = new URL("file:images/fall.wav");
        } catch (MalformedURLException e1) {}

        fallSoundClip = Applet.newAudioClip(fallSoundUrl);

        switch(optionNumber) {
            //When the option selected is Option 1
            case "1":
                /*
                if(jLCheckDown != null) {
                    canMoveLeft = false;
                    move(MOVE_DOWN);
                }
                if (xCounter>0) {
                    JLabel jLCheckLeft = jLMazeLabels[xCounter-1][yCounter];
                    if(jLCheckLeft != null && canMoveLeft && jLCheckLeft.getIcon().equals(sandBlockIcon)) {
                        move(MOVE_LEFT);
                    }
                    if(jLCheckLeft != null && canMoveLeft && jLCheckLeft.getIcon().equals(greyBlockIcon)) {
                        move(MOVE_LEFT);
                        win();
                    }
                }
                 */
                break;

            //When the option selected is Option 2
            case "2":
                if(jLCheckDown != null){
                    canMoveLeft = false;
                    fallSoundClip.play();
                    //The intermediate timer which makes the ball fall down
                    timerIntermediate = new Timer(50, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            move(MOVE_DOWN);
                        }
                    });
                    timerIntermediate.start();
                }

                if (xCounter>0) {
                    JLabel jLCheckLeft = jLMazeLabels[xCounter-1][yCounter];
                    if(jLCheckLeft != null && canMoveLeft && jLCheckLeft.getIcon().equals(sandBlockIcon)) {
                        if(timerIntermediate != null && timerIntermediate.isRunning())timerIntermediate.stop();
                        move(MOVE_LEFT);
                    }
                    if(jLCheckLeft != null && canMoveLeft && jLCheckLeft.getIcon().equals(greyBlockIcon)) {
                        move(MOVE_LEFT);
                        win();
                    }
                }

                break;

            //When the option selected is Option 3
            case "3":
                if(jLCheckDown != null){
                    fallSoundClip.play();
                    canMoveLeft = false;
                    timerAdvanced = new Timer(50, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            move(MOVE_DOWN);
                        }
                    });
                    timerAdvanced.start();
                }

                if(canMoveLeft && timerAdvanced != null && timerAdvanced.isRunning()) {
                    timerAdvanced.stop();
                }

                if(canMoveLeft && timerIntermediate != null && timerIntermediate.isRunning()) {
                    timerIntermediate.stop();
                }
        }

    }


    /**
     * The move method that takes a direction string as parameter
     * The method calls the respective method to move the ball
     * @param direction
     */
    private void move(String direction) {
        switch(direction) {
            case "left":
                moveBallLeft();
                break;
            case "down":
                moveBallDown();
                break;
            case "up":
                moveBallUp();
                break;
            case "right":
                moveBallRight();
                break;
        }
    }


    //	Moving ball to the Left With the respective button or method calls
    private void moveBallLeft() {
        if (xCounter>0) {
            JLabel jLCheck = jLMazeLabels[xCounter-1][yCounter];
            if(jLCheck != null && (jLCheck.getIcon().equals(sandBlockIcon)
                    ||jLCheck.getIcon().equals(cherryIcon))) {
                jLMazeLabels[xCounter][yCounter].setIcon(sandBlockIcon);
                jLMazeLabels[xCounter-1][yCounter].setIcon(goldenBallIcon);
                xCounter--;
                jLDirectionIcon.setIcon(directionImageWest);
                jTFDirectionText.setText("W");
                jTFSquareText.setText(""+xCounter+", "+yCounter);
            }
            else if(jLCheck != null && jLCheck.getIcon().equals(greyBlockIcon)) {
                jLMazeLabels[xCounter][yCounter].setIcon(sandBlockIcon);
                jLMazeLabels[xCounter-1][yCounter].setIcon(goldenBallIcon);
                xCounter--;
                jTFSquareText.setText(""+xCounter+", "+yCounter);
                win();
            }
        }
    }

    //	Moving ball to the Right With the respective button or method calls
    private void moveBallRight() {
        JLabel jLCheck = jLMazeLabels[xCounter+1][yCounter];
        if(jLCheck != null && (jLCheck.getIcon().equals(sandBlockIcon)
                ||jLCheck.getIcon().equals(cherryIcon))) {
            jLMazeLabels[xCounter][yCounter].setIcon(sandBlockIcon);
            jLMazeLabels[xCounter+1][yCounter].setIcon(goldenBallIcon);
            xCounter++;
            jLDirectionIcon.setIcon(directionImageEast);
            jTFDirectionText.setText("E");
            jTFSquareText.setText(""+xCounter+", "+yCounter);
        }
    }

    //	Moving ball downwards With the respective button or method calls
    private void moveBallDown() {
        JLabel jLCheck = jLMazeLabels[xCounter][yCounter+1];
        if(jLCheck != null && (jLCheck.getIcon().equals(sandBlockIcon)
                ||jLCheck.getIcon().equals(cherryIcon))) {
            jLMazeLabels[xCounter][yCounter].setIcon(sandBlockIcon);
            jLMazeLabels[xCounter][yCounter+1].setIcon(goldenBallIcon);
            yCounter++;
            jLDirectionIcon.setIcon(directionImageSouth);
            jTFDirectionText.setText("S");
            jTFSquareText.setText(""+xCounter+", "+yCounter);
        }

    }

    //	Moving ball upwards With the respective button or method calls
    private void moveBallUp() {
        if (yCounter>0) {
            JLabel jLCheck = jLMazeLabels[xCounter][yCounter-1];
            if(jLCheck != null && (jLCheck.getIcon().equals(sandBlockIcon)
                    ||jLCheck.getIcon().equals(cherryIcon))) {
                jLMazeLabels[xCounter][yCounter].setIcon(sandBlockIcon);
                jLMazeLabels[xCounter][yCounter-1].setIcon(goldenBallIcon);
                yCounter--;
                jLDirectionIcon.setIcon(directionImageNorth);
                jTFDirectionText.setText("N");
                jTFSquareText.setText(""+xCounter+", "+yCounter);
            }
        }
    }

    /*
     * This method creates the option three layout
     */

    private void createOptionThreeLayout() {
//Create the five cherries two of the cherries are placed randomly
        int secondlastRandom = (int)(1 + Math.ceil(Math.random()*14));
        int lastRandom = (int)(1 + Math.ceil(Math.random()*14));

        jLMazeLabels[9][2].setIcon(cherryIcon);
        jLMazeLabels[11][5].setIcon(cherryIcon);
        jLMazeLabels[secondlastRandom][6].setIcon(cherryIcon);
        jLMazeLabels[5][8].setIcon(cherryIcon);
        jLMazeLabels[lastRandom][12].setIcon(cherryIcon);

//Create the five enemies
        jLMazeLabels[0][3].setIcon(enemyIcon);
        jLMazeLabels[15][9].setIcon(enemyIcon);
        enemyMoveTimer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveEnemy();
                detectCollision();
            }
        });
    }


    //Moving the enemies in option 3 layout
    private void moveEnemy() {
        if (enemyOneX<15 && enemyOneCanMoveRight) {
            jLMazeLabels[enemyOneX][3].setIcon(sandBlockIcon);
            enemyOneX++;
            jLMazeLabels[enemyOneX][3].setIcon(enemyIcon);
        }
        else if(enemyOneX>0) {
            enemyOneCanMoveRight = false;
            jLMazeLabels[enemyOneX][3].setIcon(sandBlockIcon);
            enemyOneX--;
            jLMazeLabels[enemyOneX][3].setIcon(enemyIcon);
        }
        else {
            enemyOneCanMoveRight = true;
        }

        if (enemyTwoX<15 && enemyTwoCanMoveRight) {
            jLMazeLabels[enemyTwoX][9].setIcon(sandBlockIcon);
            enemyTwoX++;
            jLMazeLabels[enemyTwoX][9].setIcon(enemyIcon);
        }
        else if(enemyTwoX>0) {
            enemyTwoCanMoveRight = false;
            jLMazeLabels[enemyTwoX][9].setIcon(sandBlockIcon);
            enemyTwoX--;
            jLMazeLabels[enemyTwoX][9].setIcon(enemyIcon);
        }
        else {
            enemyTwoCanMoveRight = true;
        }
    }

    /*
     * The function of the method detectCollision is
     * detecting that the ball is touched by the enemy or the cherries are touched by
     * the ball
     *
     */
    private void detectCollision() {
        JLabel jCheckIcon;
        int ballCounter = 0;
        int cherryCounter = 0;
        for (int i = 0; i<16; i++) {
            for (int j=0; j<13; j++) {
                jCheckIcon = jLMazeLabels[i][j];
                if(jCheckIcon!=null && jCheckIcon.getIcon().equals(goldenBallIcon)) {
                    ballCounter = 1;
                }
                if(jCheckIcon!=null && jCheckIcon.getIcon().equals(cherryIcon)) {
                    cherryCounter++;
                }
            }
        }
        jTFScoreText.setText((5-cherryCounter) + "0");
        if(ballCounter==0)gameOver();
    }

//Start moving the ball. For the ball to start moving, the run button must be pressed.
//This is true for all the options.

    private void run() {
        jBRunBtn.setText("Pause");
        jBRunBtn.setIcon(pauseIcon);

        timerForRun = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent runClick) {
                startTimer();
            }
        });

        timerForBall = new Timer(750, new ActionListener() {
            public void actionPerformed(ActionEvent runClick) {
                moveBall();
            }
        });

        timerForBall.start();
        timerForRun.start();
        if(enemyMoveTimer!=null)enemyMoveTimer.start();
    }

    //Pause the game, stop the ball from moving.
    private void pause() {
        jBRunBtn.setText("Run");
        jBRunBtn.setIcon(runIcon);
        if (timerForRun!=null && timerForRun.isRunning())timerForRun.stop();
        if (timerForBall!=null && timerForBall.isRunning())timerForBall.stop();
        if(enemyMoveTimer!=null && enemyMoveTimer.isRunning())enemyMoveTimer.stop();
    }

    //Win The Game
    private void win() {
        if (timerForRun!=null && timerForRun.isRunning())timerForRun.stop();
        if (timerForBall!=null && timerForBall.isRunning())timerForBall.stop();

        try {
            winSoundUrl = new URL("file:images/win.wav");
        } catch (MalformedURLException e1) {}
        winSoundClip = Applet.newAudioClip(winSoundUrl);
        winSoundClip.play();
        pause();

        if(jTFOptionText.getText().equals("3")) {
            JOptionPane.showMessageDialog(null, "You Win\n"
                            + "Score: "+ jTFScoreText.getText() + "\n \n",
                    "Congratulations!!",
                    JOptionPane.INFORMATION_MESSAGE,
                    winIcon);
            reset();
        }
    }

    //Game Over
    private void gameOver() {
        try {
            loseSoundUrl = new URL("file:images/lose.wav");
        } catch (MalformedURLException e1) {}
        loseSoundClip = Applet.newAudioClip(loseSoundUrl);
        loseSoundClip.play();
        pause();
        JOptionPane.showMessageDialog(null, "You Lose!\n"
                        + "Score: "+ jTFScoreText.getText() + "\n \n",
                "Try next time!",
                JOptionPane.INFORMATION_MESSAGE,
                loseIcon);
        reset();
    }

    //The about description JOptionPane method
    private void aboutHelp() {
        JOptionPane.showMessageDialog(null,
                "CBallMaze - Ball Maze Application \n"
                        + "Version 1.0 \n",
                "About CBallMaze", 1, null);
    }

    //Exit and restart the game
    private void reset() {
        if (timerForRun!=null && timerForRun.isRunning())timerForRun.stop();
        if (timerForBall!=null && timerForBall.isRunning())timerForBall.stop();
        dispose();
        new Main();
    }


    //Start Digital Timer (This happens when the run button is clicked).
    private void startTimer() {
        currentSeconds = Integer.parseInt(timerSeconds.getText());
        currentMinutes = Integer.parseInt(timerMinutes.getText());
        currentHours = Integer.parseInt(timerHours.getText());

        nextSeconds = currentSeconds + 1;
        if(nextSeconds<10)timerSeconds.setText("0" + String.valueOf(nextSeconds));
        else timerSeconds.setText(String.valueOf(nextSeconds));

        if(nextSeconds>59) {
            nextSeconds = 0;
            timerSeconds.setText("0" + String.valueOf(nextSeconds));

            nextMinutes = currentMinutes + 1;
            if(nextMinutes<10)timerMinutes.setText("0" + String.valueOf(nextMinutes));
            else timerMinutes.setText(String.valueOf(nextMinutes));
        }

        if(nextMinutes>59) {
            nextMinutes = 0;
            timerMinutes.setText("0" + String.valueOf(nextMinutes));

            nextHours = currentHours + 1;
            if(nextHours <10)timerHours .setText("0" + String.valueOf(nextHours));
            else timerHours .setText(String.valueOf(nextHours));

        }
    }

    //Different action listener events for different buttons or menu items
    public void actionPerformed(ActionEvent click){
        if (click.getActionCommand().equals("Quit")||click.getActionCommand().equals("Exit")) {
            System.exit(0);
        }
        if (click.getActionCommand().equals("Act")) {
            moveBall();
        }
        if (click.getActionCommand().equals("Run")) {
            run();
        }
        if (click.getActionCommand().equals("Pause")) {
            pause();
        }
        if(click.getActionCommand().equals("Reset")){
            reset();
        }

        if(click.getActionCommand().equals("<")) {
            moveBallLeft();
        }

        if(click.getActionCommand().equals(">")) {
            moveBallRight();
        }

        if(click.getActionCommand().equals("v")) {
            moveBallDown();
        }

        if(click.getActionCommand().equals("^")) {
            moveBallUp();
        }
        if(click.getSource().equals(jBOption1)) {
            jTFOptionText.setText("1");
            jBRunBtn.grabFocus();
            slider.setEnabled(true);
        }
        if(click.getSource().equals(jBOption2)) {
            jTFOptionText.setText("2");
            jBRunBtn.grabFocus();
            slider.setEnabled(true);
        }

        if(click.getSource().equals(jBOption3)) {
//	If the option 3 is already selected, reset the tile to regenerate the random
//	cherries in option 3
            if(!jTFOptionText.getText().equals("3")){
                jTFOptionText.setText("3");
                createOptionThreeLayout();
                jBRunBtn.grabFocus();
                slider.setEnabled(false);
            }
            else {
                reset();
            }
        }
        if(click.getActionCommand().equals("About")) {
            aboutHelp();
        }

    }

    //KeyListener key press event for up down left right keys
//The ball moves with these only when the option 3 is selected.
    public void keyPressed(KeyEvent e) {
        if (jTFOptionText.getText().equals("3") && jBRunBtn.getText().equals("Pause")){
            if (e.getKeyCode()==37) {
                moveBallLeft();
                jBRunBtn.grabFocus();
            }
            if (e.getKeyCode()==38) {
                moveBallUp();
                jBRunBtn.grabFocus();
            }
            if (e.getKeyCode()==39) {
                moveBallRight();
                jBRunBtn.grabFocus();
            }
            if (e.getKeyCode()==40) {
                moveBallDown();
                jBRunBtn.grabFocus();
            }
        }
    }

    //No features have been added for key release and key type events
    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    //State Change even for the slider. The timer values are set with this event
    public void stateChanged(ChangeEvent e){
        if (jBRunBtn.getText().equals("Pause")) {
            int timeGap = 1501 - slider.getValue();
            if (timerForBall.isRunning()) {
                timerForBall.setDelay(timeGap);
            }
        }
    }
}