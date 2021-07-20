package Omok;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener extends MouseAdapter {

	private Map map;
	private SP_Omok gui;
	private AI ai;
	
	public MouseListener(Map map, SP_Omok gui, AI ai) {
		this.map = map;
		this.gui = gui;
		this.ai = ai;
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		if(gui.isMainScreen || gui.isEndingScreen) return;
		
		super.mousePressed(arg0);
		// 입력된 x, y 좌표를 Cell(30)으로 나눠 나온 값에  1, 2를 빼 0~19 사이로 맞춤
		int x = (int) Math.round(arg0. getX() / (double) 30)-1;
		int y = (int) Math.round(arg0. getY() / (double) 30)-2;
		
		if (x < 0 || x > 18 || y<0 || y > 18) {				 // 바둑판 외 클릭 시 리턴
			return;
		}
		if (map.getMap(x, y) == 1 || map.getMap(x, y) == 2){ // 이미 놓여진 자리일 경우 리턴
			return;
		}
		
		map.setMap(x, y);
		map.turnChange();
		map.endTurn=true;
		ai.stopAlphaOh=false;
		ai.stopChecking=false;
		ai.returningNum=0;
		ai.reCheck=false;
		gui.count=0;
		//gui.repaint();
		
		printText(x, y);
	}
	
	public void printText(int x, int y) {
		if(map.isWhite)
			System.out.print(" Black turn ");
		else
			System.out.print(" White turn ");
		System.out.println(x + ", " + y + " (" + map.getMap(x, y) + ")");
		
		if (map.CheckWin()==1) {
			System.out.println("흑돌 승리!");
		}else if (map.CheckWin()==2)
			System.out.println("백돌 승리!");
	}
}
