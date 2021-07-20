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
	
	private Image screenImage; // 더블 버퍼링을 위해 전체 화면에 대한 이미지를 담는 인스턴스
	private Graphics screenGraphic;
	
	//URL exitButtonImage = SP_Omok.class.getClassLoader().getResource("exitButtonImage.png");
	
	// -------------------- 이미지 객체 선언 --------------------
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
	
	// -------------------- 버튼 선언 --------------------
	private JButton start1pButton = new JButton(singleButtonImage);
	private JButton start2pButton = new JButton(multiButtonImage);
	private JButton quitButton = new JButton(quitButtonImage);
	private JButton backButton = new JButton(backButtonImage);
	private JButton rePlayButton = new JButton(resetButtonImage);
	private JButton exitButton = new JButton(exitButtonImage);
	
	public boolean isMainScreen = true;
	public boolean isGameScreen = false;
	public boolean isEndingScreen = false;
	private int mouseX, mouseY;						// 마우스의 좌표 읽기
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
		setBackground(new Color(0, 0, 0, 0));	// 배경 없애려면 주석처리
		setLayout(null);

		addMouseListener(new MouseListener(map, this, ai));
		
		backButton.setVisible(false);
		rePlayButton.setVisible(false);
		
		// ------------- 메뉴 바 우측 x표 버튼 -------------
		exitButton.setBounds(Main.SCREEN_WIDTH-30, 0, 30, 30);
		exitButton.setBorderPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setFocusPainted(false);
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { 		// 마우스 버튼을 눌렀을때 이벤트
				System.exit(0);								// 프로그램 종료
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {		 // 마우스가 해당 버튼 위에 올라왔을때 이벤트
				exitButton.setIcon(exitButtonOnImage);  // 해당 이미지로 변경
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 		 // 마우스가 해당 버튼 위에 없을때 이벤트
				exitButton.setIcon(exitButtonImage);		 // 해당 이미지로 변경
			}
		});
		add(exitButton);
		
		// ----------------- 메뉴 바 -----------------
		menuBar.setBounds(0, 0, Main.SCREEN_WIDTH, 30);
		menuBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { 				// 마우스를 눌렀을때 마우스의 좌표를 기록
				mouseX = e.getX();					
				mouseY = e.getY();
			}
		});
		menuBar.addMouseMotionListener(new MouseMotionAdapter() {	// 마우스 드래그 할 때 마우스 좌표로 창이 같이 이동
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x-mouseX, y-mouseY);
			}
		});
		add(menuBar);		
		// ----------------- 혼자서 하기 버튼 -----------------
		start1pButton.setBounds(325, 355, 200, 45);
		start1pButton.setBorderPainted(false);
		start1pButton.setContentAreaFilled(false);
		start1pButton.setFocusPainted(false);
		start1pButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { 		// 마우스 버튼을 눌렀을때 이벤트
				toGameScreen();
				is1P=true;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {		 // 마우스가 해당 버튼 위에 올라왔을때 이벤트
				start1pButton.setIcon(singleButtonOnImage);  // 해당 이미지로 변경
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 		 // 마우스가 해당 버튼 위에 없을때 이벤트
				start1pButton.setIcon(singleButtonImage);		 // 해당 이미지로 변경
			}
		});
		add(start1pButton);

		// ----------------- 둘이서 하기 버튼 -----------------
		start2pButton.setBounds(325, 430, 200, 45);
		start2pButton.setBorderPainted(false);
		start2pButton.setContentAreaFilled(false);
		start2pButton.setFocusPainted(false);
		start2pButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { 		// 마우스 버튼을 눌렀을때 이벤트
				toGameScreen();				
				is1P=false;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {		 // 마우스가 해당 버튼 위에 올라왔을때 이벤트
				start2pButton.setIcon(multiButtonOnImage);  // 해당 이미지로 변경
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 		 // 마우스가 해당 버튼 위에 없을때 이벤트
				start2pButton.setIcon(multiButtonImage);		 // 해당 이미지로 변경
			}
		});
		add(start2pButton);

		// ----------------- 종료 버튼 -----------------
		quitButton.setBounds(325, 505, 200, 45);
		quitButton.setBorderPainted(false);
		quitButton.setContentAreaFilled(false);
		quitButton.setFocusPainted(false);
		quitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { 		// 마우스 버튼을 눌렀을때 이벤트
				System.exit(0);								// 프로그램 종료
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {		 // 마우스가 해당 버튼 위에 올라왔을때 이벤트
				quitButton.setIcon(quitButtonOnImage);  // 해당 이미지로 변경
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 		 // 마우스가 해당 버튼 위에 없을때 이벤트
				quitButton.setIcon(quitButtonImage);		 // 해당 이미지로 변경
			}
		});
		add(quitButton);

		// ----------------- 메뉴로 돌아가기 버튼 -----------------
		backButton.setBounds(615, 555, 200, 45);
		backButton.setBorderPainted(false);
		backButton.setContentAreaFilled(false);
		backButton.setFocusPainted(false);
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { 		// 마우스 버튼을 눌렀을때 이벤트
				toMainScreen();
				map.resetMap();
				ai.resetAI();
				is1P=false;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {		 // 마우스가 해당 버튼 위에 올라왔을때 이벤트
				backButton.setIcon(backButtonOnImage);  // 해당 이미지로 변경
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 		 // 마우스가 해당 버튼 위에 없을때 이벤트
				backButton.setIcon(backButtonImage);		 // 해당 이미지로 변경
			}
		});
		add(backButton);

		// ----------------- 다시 하기 버튼 -----------------
		rePlayButton.setBounds(615, 475, 200, 45);
		rePlayButton.setBorderPainted(false);
		rePlayButton.setContentAreaFilled(false);
		rePlayButton.setFocusPainted(false);
		rePlayButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { 		// 마우스 버튼을 눌렀을때 이벤트
				map.resetMap();
				ai.resetAI();
				toGameScreen();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {		 // 마우스가 해당 버튼 위에 올라왔을때 이벤트
				rePlayButton.setIcon(resetButtonOnImage);  // 해당 이미지로 변경
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 		 // 마우스가 해당 버튼 위에 없을때 이벤트
				rePlayButton.setIcon(resetButtonImage);		 // 해당 이미지로 변경
			}
		});
		add(rePlayButton);
	}
	
	
	public void paint(Graphics g) { // JFrame을 상속받은 GUI 프로그램에서 화면을 그려주는 함수
		screenImage = createImage(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);
		// Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT 만큼의 크기인 이미지를 만들어 screenImage에 넣어줌
		screenGraphic = screenImage.getGraphics();
		// screenImage를 통해 그래픽 객체를 얻어옴
		
		screenDraw((Graphics)screenGraphic);	
		
		g.drawImage(screenImage, 0, 0, null); 		// screenImage를 0,0 위치에 그림
	}

	public void screenDraw(Graphics g) {
		g.drawImage(mainBackground, 0, 0, null);	// 역동적인 이미지들을 그려줌 (일반적)		배경 없애려면 주석처리
	
		if(isGameScreen) {
			if(map.CheckWin()==1) {					// 이겼는지 체크 후 화면 이동
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
				if(map.isBlack) {						// 턴 표시
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
			if(is1P&&map.isWhite) {							// 혼자서 하기 클릭 시, AI 동작 (백돌 때만)
				ai.alphaOh();
				map.screenDraw(g);
				//count++;
			}
			else if(is1P==false || (is1P&&map.isBlack)) {	// 둘이서 하기 클릭 시, 일반 오목 알고리즘 동작 (+혼자서 하기 흑돌 때)
				map.screenDraw(g);
			}
		}
		
		paintComponents(g); 		// 고정된 이미지들을 그려줌 (add 함수가 사용된 이미지를 그림)
		
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