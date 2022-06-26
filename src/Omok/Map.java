package Omok;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Map extends JPanel {
	public Image blackStone = new ImageIcon(Main.class.getResource("../images/black.png")).getImage();
	public Image whiteStone = new ImageIcon(Main.class.getResource("../images/white.png")).getImage();
	public Image blackStoneR = new ImageIcon(Main.class.getResource("../images/blackR.png")).getImage();
	public Image whiteStoneR = new ImageIcon(Main.class.getResource("../images/whiteR.png")).getImage();
	
	public static short[][] map;		// 흑돌=1 , 백돌=2
	public static short[][] blockedMap;	// AI 동작 시 필요한 맵 체크
	public boolean isBlack=true;
	public boolean isWhite=false;
	public boolean endTurn=false;	// 쓸모 없을 듯
	public int recentX=0;
	public int recentY=0;
	
	public Map() {
		map = new short[30][30];
		blockedMap = new short[30][30];
	}
	
	public int getMap(int x, int y) {
		try{
			return map[x][y];
		}catch(Exception e){
			e.printStackTrace();
		}
		return map[x][y];
	}
	
	public void setMap(int x, int y) {
		if (isBlack==true) map[x][y] = 1;
		else if (isWhite==true) map[x][y] = 2;
		
		recentX = x;
		recentY = y;
	}
	
	public int getBlockedMap(int x, int y) {
		try{
			return blockedMap[x][y];
		}catch(Exception e){
			e.printStackTrace();
		}
		return blockedMap[x][y];
	}
	
	public void screenDraw(Graphics g) {
		for(int x=0;x<25;x++){
			for(int y=0;y<25;y++){
				if(getMap(x, y) == 1 && (x == recentX && y == recentY)) {			// 제일 최근 놓은 돌을 표시하기 위한 코드
					g.drawImage(blackStoneR, (x+1)*30-13, (y+2)*30-13, null);
				}
				else if(getMap(x, y) == 2 && (x == recentX && y == recentY)) {
					g.drawImage(whiteStoneR, (x+1)*30-13, (y+2)*30-13, null);
				}
				else {										// 이전 돌은 다시 원래 이미지로 바꿈
					if(getMap(x, y) == 1) {			
						g.drawImage(blackStone, (x+1)*30-13, (y+2)*30-13, null);
					}
					else if(getMap(x, y) == 2) {
						g.drawImage(whiteStone, (x+1)*30-13, (y+2)*30-13, null);
					}
				}
			}
		}
	}
	
	public void turnChange() {
		if (isBlack) {
			isBlack = false;
			isWhite = true;
		}
		else if (isWhite) {
			isWhite = false;
			isBlack = true;
		}
	}
	
	public void resetMap() {
		for(int i=0; i<20; i++) {
			for(int j=0; j<20; j++) {
				map[i][j]=0;
				blockedMap[i][j]=0;
			}
		}
		isBlack=true;
		isWhite=false;
	}
	
	public int CheckWin() {			// 아래 4개의 체크 함수를 종합하여 이겼는지 체크
		if (CheckField()==1 || CheckRecord ()==1 || CheckLDiagonal()==1 || CheckRDiagonal()==1) {
			return 1;
		}
		if (CheckField()==2 || CheckRecord ()==2 || CheckLDiagonal()==2 || CheckRDiagonal()==2) {
			return 2;
		}
		return 0;
	}
	
	public int CheckField() {		// 행 체크 (기준점으로 부터 아래로)
		for(int x=0;x<19;x++){
			for(int y=0;y<15;y++){
				if((getMap(x,y) == 1) && (getMap(x,y+1) == 1) && (getMap(x,y+2) == 1)
						&& (getMap(x,y+3) == 1) && (getMap(x,y+4) == 1)) {	// 흑돌 승리
					return 1;
				}
				if((getMap(x,y) == 2) && (getMap(x,y+1) == 2) && (getMap(x,y+2) == 2)
						&& (getMap(x,y+3) == 2) && (getMap(x,y+4) == 2)) {	// 백돌 승리
					return 2;
				}
			}
		}
		return 0;
	}
	public int CheckRecord() {		// 열 체크 (기준점으로 부터 오른쪽으로)
		for(int x=0;x<15;x++){
			for(int y=0;y<19;y++){
				if((getMap(x,y) == 1) && (getMap(x+1,y) == 1) && (getMap(x+2,y) == 1)
						&& (getMap(x+3,y) == 1) && (getMap(x+4,y) == 1)) {
					return 1;
				}
				if((getMap(x,y) == 2) && (getMap(x+1,y) == 2) && (getMap(x+2,y) == 2)
						&& (getMap(x+3,y) == 2) && (getMap(x+4,y) == 2)) {
					return 2;
				}
			}
		}
		return 0;
	}
	public int CheckLDiagonal() {	// 왼쪽 대각선 체크 (기준점으로 부터 왼쪽 아래로)
		for(int x=0;x<19;x++){
			for(int y=0;y<19;y++){
				if(x-4>0 && y+4<19){
					if((getMap(x,y) == 1) && (getMap(x-1,y+1) == 1) && (getMap(x-2,y+2) == 1)
							&& (getMap(x-3,y+3) == 1) && (getMap(x-4,y+4) == 1)) {
						return 1;
					}
					if((getMap(x,y) == 2) && (getMap(x-1,y+1) == 2) && (getMap(x-2,y+2) == 2)
							&& (getMap(x-3,y+3) == 2) && (getMap(x-4,y+4) == 2)) {
						return 2;
					}
				}
			}
		}
		return 0;
	}
	public int CheckRDiagonal() {	// 오른쪽 대각선 체크 (기준점으로 부터 오른쪽 아래로)
		for(int x=0;x<19;x++){
			for(int y=0;y<19;y++){
				if(x+4<19 && y+4<19) {
					if((getMap(x,y) == 1) && (getMap(x+1,y+1) == 1) && (getMap(x+2,y+2) == 1)
							&& (getMap(x+3,y+3) == 1) && (getMap(x+4,y+4) == 1)) {
						return 1;
					}
					if((getMap(x,y) == 2) && (getMap(x+1,y+1) == 2) && (getMap(x+2,y+2) == 2)
							&& (getMap(x+3,y+3) == 2) && (getMap(x+4,y+4) == 2)) {
						return 2;
					}
				}
			}
		}
		return 0;
	}
}
