package Omok;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SP_Omok extends JFrame {
	
	private Image screenImage; // ���� ���۸��� ���� ��ü ȭ�鿡 ���� �̹����� ��� �ν��Ͻ�
	private Graphics screenGraphic;
	
	//URL exitButtonImage = SP_Omok.class.getClassLoader().getResource("exitButtonImage.png");
	
	// -------------------- �̹��� ��ü ���� --------------------
	private Image mainBackground = new ImageIcon(Main.class.getResource("../images/mainBackGround.png")).getImage();
	private JLabel menuBar = new JLabel(new ImageIcon(Main.class.getResource("../images/menuBar.jpg")));
	private ImageIcon exitButtonImage = new ImageIcon(Main.class.getResource("../images/exitButton.png"));
	private ImageIcon exitButtonOnImage = new ImageIcon(Main.class.getResource("../images/exitButtonOn.png"));
	private ImageIcon singleButtonImage = new ImageIcon(Main.class.getResource("../images/singleButton.png"));
	private ImageIcon singleButtonOnImage = new ImageIcon(Main.class.getResource("../images/singleButtonOn.png"));
	private ImageIcon multiButtonImage = new ImageIcon(Main.class.getResource("../images/multiButton.png"));
	private ImageIcon multiButtonOnImage = new ImageIcon(Main.class.getResource("../images/multiButtonOn.png"));
	private ImageIcon quitButtonImage = new ImageIcon(Main.class.getResource("../images/quitButton.png"));
	private ImageIcon quitButtonOnImage = new ImageIcon(Main.class.getResource("../images/quitButtonOn.png"));
	private ImageIcon resetButtonImage = new ImageIcon(Main.class.getResource("../images/resetButton.png"));
	private ImageIcon resetButtonOnImage = new ImageIcon(Main.class.getResource("../images/resetButtonOn.png"));
	private ImageIcon backButtonImage = new ImageIcon(Main.class.getResource("../images/backButton.png"));
	private ImageIcon backButtonOnImage = new ImageIcon(Main.class.getResource("../images/backButtonOn.png"));
	
	// -------------------- ��ư ���� --------------------
	private JButton start1pButton = new JButton(singleButtonImage);
	private JButton start2pButton = new JButton(multiButtonImage);
	private JButton quitButton = new JButton(quitButtonImage);
	private JButton backButton = new JButton(backButtonImage);
	private JButton rePlayButton = new JButton(resetButtonImage);
	private JButton exitButton = new JButton(exitButtonImage);
	
	public boolean isMainScreen = true;
	public boolean isGameScreen = false;
	public boolean isEndingScreen = false;
	private int mouseX, mouseY;						// ���콺�� ��ǥ �б�
	public static boolean is1P = false;
	public static int count=0;
	
	Map map = new Map();
	AI ai = new AI(map);
	MouseListener mouse = new MouseListener(map, this, ai);
	
	public SP_Omok() {
		setUndecorated(true);
		setTitle("Omok");
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);				
		setVisible(true);	
		setBackground(new Color(0, 0, 0, 0));	// ��� ���ַ��� �ּ�ó��
		setLayout(null);

		addMouseListener(new MouseListener(map, this, ai));
		
		backButton.setVisible(false);
		rePlayButton.setVisible(false);
		
		// ------------- �޴� �� ���� xǥ ��ư -------------
		exitButton.setBounds(Main.SCREEN_WIDTH-30, 0, 30, 30);
		exitButton.setBorderPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setFocusPainted(false);
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { 		// ���콺 ��ư�� �������� �̺�Ʈ
				System.exit(0);								// ���α׷� ����
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {		 // ���콺�� �ش� ��ư ���� �ö������ �̺�Ʈ
				exitButton.setIcon(exitButtonOnImage);  // �ش� �̹����� ����
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 		 // ���콺�� �ش� ��ư ���� ������ �̺�Ʈ
				exitButton.setIcon(exitButtonImage);		 // �ش� �̹����� ����
			}
		});
		add(exitButton);
		
		// ----------------- �޴� �� -----------------
		menuBar.setBounds(0, 0, Main.SCREEN_WIDTH, 30);
		menuBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { 				// ���콺�� �������� ���콺�� ��ǥ�� ���
				mouseX = e.getX();					
				mouseY = e.getY();
			}
		});
		menuBar.addMouseMotionListener(new MouseMotionAdapter() {	// ���콺 �巡�� �� �� ���콺 ��ǥ�� â�� ���� �̵�
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x-mouseX, y-mouseY);
			}
		});
		add(menuBar);		
		// ----------------- ȥ�ڼ� �ϱ� ��ư -----------------
		start1pButton.setBounds(325, 355, 200, 45);
		start1pButton.setBorderPainted(false);
		start1pButton.setContentAreaFilled(false);
		start1pButton.setFocusPainted(false);
		start1pButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { 		// ���콺 ��ư�� �������� �̺�Ʈ
				toGameScreen();
				is1P=true;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {		 // ���콺�� �ش� ��ư ���� �ö������ �̺�Ʈ
				start1pButton.setIcon(singleButtonOnImage);  // �ش� �̹����� ����
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 		 // ���콺�� �ش� ��ư ���� ������ �̺�Ʈ
				start1pButton.setIcon(singleButtonImage);		 // �ش� �̹����� ����
			}
		});
		add(start1pButton);

		// ----------------- ���̼� �ϱ� ��ư -----------------
		start2pButton.setBounds(325, 430, 200, 45);
		start2pButton.setBorderPainted(false);
		start2pButton.setContentAreaFilled(false);
		start2pButton.setFocusPainted(false);
		start2pButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { 		// ���콺 ��ư�� �������� �̺�Ʈ
				toGameScreen();				
				is1P=false;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {		 // ���콺�� �ش� ��ư ���� �ö������ �̺�Ʈ
				start2pButton.setIcon(multiButtonOnImage);  // �ش� �̹����� ����
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 		 // ���콺�� �ش� ��ư ���� ������ �̺�Ʈ
				start2pButton.setIcon(multiButtonImage);		 // �ش� �̹����� ����
			}
		});
		add(start2pButton);

		// ----------------- ���� ��ư -----------------
		quitButton.setBounds(325, 505, 200, 45);
		quitButton.setBorderPainted(false);
		quitButton.setContentAreaFilled(false);
		quitButton.setFocusPainted(false);
		quitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { 		// ���콺 ��ư�� �������� �̺�Ʈ
				System.exit(0);								// ���α׷� ����
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {		 // ���콺�� �ش� ��ư ���� �ö������ �̺�Ʈ
				quitButton.setIcon(quitButtonOnImage);  // �ش� �̹����� ����
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 		 // ���콺�� �ش� ��ư ���� ������ �̺�Ʈ
				quitButton.setIcon(quitButtonImage);		 // �ش� �̹����� ����
			}
		});
		add(quitButton);

		// ----------------- �޴��� ���ư��� ��ư -----------------
		backButton.setBounds(615, 555, 200, 45);
		backButton.setBorderPainted(false);
		backButton.setContentAreaFilled(false);
		backButton.setFocusPainted(false);
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { 		// ���콺 ��ư�� �������� �̺�Ʈ
				toMainScreen();
				map.resetMap();
				ai.resetAI();
				is1P=false;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {		 // ���콺�� �ش� ��ư ���� �ö������ �̺�Ʈ
				backButton.setIcon(backButtonOnImage);  // �ش� �̹����� ����
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 		 // ���콺�� �ش� ��ư ���� ������ �̺�Ʈ
				backButton.setIcon(backButtonImage);		 // �ش� �̹����� ����
			}
		});
		add(backButton);

		// ----------------- �ٽ� �ϱ� ��ư -----------------
		rePlayButton.setBounds(615, 475, 200, 45);
		rePlayButton.setBorderPainted(false);
		rePlayButton.setContentAreaFilled(false);
		rePlayButton.setFocusPainted(false);
		rePlayButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { 		// ���콺 ��ư�� �������� �̺�Ʈ
				map.resetMap();
				ai.resetAI();
				toGameScreen();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {		 // ���콺�� �ش� ��ư ���� �ö������ �̺�Ʈ
				rePlayButton.setIcon(resetButtonOnImage);  // �ش� �̹����� ����
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 		 // ���콺�� �ش� ��ư ���� ������ �̺�Ʈ
				rePlayButton.setIcon(resetButtonImage);		 // �ش� �̹����� ����
			}
		});
		add(rePlayButton);
	}
	
	
	public void paint(Graphics g) { // JFrame�� ��ӹ��� GUI ���α׷����� ȭ���� �׷��ִ� �Լ�
		screenImage = createImage(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);
		// Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT ��ŭ�� ũ���� �̹����� ����� screenImage�� �־���
		screenGraphic = screenImage.getGraphics();
		// screenImage�� ���� �׷��� ��ü�� ����
		
		screenDraw((Graphics)screenGraphic);	
		
		g.drawImage(screenImage, 0, 0, null); 		// screenImage�� 0,0 ��ġ�� �׸�
	}

	public void screenDraw(Graphics g) {
		g.drawImage(mainBackground, 0, 0, null);	// �������� �̹������� �׷��� (�Ϲ���)		��� ���ַ��� �ּ�ó��
	
		if(isGameScreen) {
			if(map.CheckWin()==1) {					// �̰���� üũ �� ȭ�� �̵�
				toEndingScreen();
				if(is1P==false)
					mainBackground = new ImageIcon(Main.class.getResource("../images/BlackWin.png")).getImage();
				else
					mainBackground = new ImageIcon(Main.class.getResource("../images/YouWin.png")).getImage();
			}
			else if(map.CheckWin()==2) {
				toEndingScreen();
				if(is1P==false)
					mainBackground = new ImageIcon(Main.class.getResource("../images/WhiteWin.png")).getImage();
				else
					mainBackground = new ImageIcon(Main.class.getResource("../images/YouLose.png")).getImage();
			}
			
			if(isEndingScreen == false) {
				if(map.isBlack) {						// �� ǥ��
					mainBackground = new ImageIcon(Main.class.getResource("../images/BlackTurn.png")).getImage();
				}
				else if(map.isWhite) {
					mainBackground = new ImageIcon(Main.class.getResource("../images/WhiteTurn.png")).getImage();
				}
			}
			/*
			if(count==1) {
				try{
					Thread.sleep(500);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			*/
			if(is1P&&map.isWhite) {							// ȥ�ڼ� �ϱ� Ŭ�� ��, AI ���� (�鵹 ����)
				ai.alphaOh();
				map.screenDraw(g);
				//count++;
			}
			else if(is1P==false || (is1P&&map.isBlack)) {	// ���̼� �ϱ� Ŭ�� ��, �Ϲ� ���� �˰��� ���� (+ȥ�ڼ� �ϱ� �浹 ��)
				map.screenDraw(g);
			}
		}
		
		paintComponents(g); 		// ������ �̹������� �׷��� (add �Լ��� ���� �̹����� �׸�)
		
		try{
			Thread.sleep(5);
		}catch(Exception e){
			e.printStackTrace();
		}
		this.repaint();
	}
	
	public void toGameScreen() {
		isGameScreen = true;
		isMainScreen = false;
		isEndingScreen = false;
		start1pButton.setVisible(false);
		start2pButton.setVisible(false);
		quitButton.setVisible(false);
		backButton.setVisible(true);
		rePlayButton.setVisible(true);
		mainBackground = new ImageIcon(Main.class.getResource("../images/gameBackGround.png")).getImage();
	}
	
	public void toMainScreen() {
		isGameScreen = false;
		isMainScreen = true;
		isEndingScreen = false;
		start1pButton.setVisible(true);
		start2pButton.setVisible(true);
		quitButton.setVisible(true);
		backButton.setVisible(false);
		rePlayButton.setVisible(false);
		mainBackground = new ImageIcon(Main.class.getResource("../images/mainBackGround.png")).getImage();
	}
	
	public void toEndingScreen() {
		isGameScreen = true;
		isMainScreen = false;
		isEndingScreen = true;
		start1pButton.setVisible(false);
		start2pButton.setVisible(false);
		quitButton.setVisible(false);
		backButton.setVisible(true);
		rePlayButton.setVisible(true);
	}
}