package Omok;

public class AI {
	private Map map;

	private int x=0;
	private int y=0;
	private int cnt=0;
	public boolean reCheck = false;
	public boolean stopAlphaOh=false;
	public boolean stopChecking=false;
	public int returningNum=0;
	
	private int currentAI = 0;
	public static int state=0;
	private int blockX=20;
	private int blockY=20;
	private int attackX=20;
	private int attackY=20;
	
	public AI(Map map) {
		this.map = map;
	}
	
	public void alphaOh() {					// state로 인공지능의 상태를 나눠 상태별로 할 수 있는 행동을 하게 하는 함수 (AI 종합 컨트롤 함수)

		//if(map.endTurn == true) {
			currentAI = CheckAI();			// 현재 맵을 스캔하여 나온 값에 따라 행동
			if(reCheck==true)
				currentAI = CheckAI();
			for(int i=0; i<19; i++) {
				if (stopAlphaOh) break;		// 두 번 for문 빠져나가기 (한 번 작동할 때 여러번의 상태 변경을 방지하기 위해 for 문을 두 번 빠져나가기 위한 코드. return 기능)
				for(int j=0; j<19; j++) {
					if(state==0 && map.getMap(i, j) == 1 && cnt==0) {		// 첫번째 돌 -  무조건 흑돌의 오른쪽 대각선 위 시작
						if(i==18 && j==0) {	// 흑돌이 첫 돌을 (맨 오른쪽, 맨 위)에 뒀을 때
							x=i-1;
							y=j+1;
						}
						else if(i==18) {	// 흑돌이 첫 돌을 (맨 오른쪽)에 뒀을 때
							x=i-1;
							y=j-1;
						}
						else if(j==0) {		// 흑돌이 첫 돌을 (맨 위)에 뒀을 때
							x=i+1;
							y=j+1;
						}
						else {				// 평상 시
							x=i+1;
							y=j-1;
						}
						map.setMap(x, y);
						cnt++;
						stopAlphaOh=true;	// return 기능 활성화
						break;				// 첫 번째 for문 빠져나가기
					}
					else if((state==0 && map.getMap(i, j) == 2 && cnt==1)) {	// 돌이 이어지는게 없을 때 - 양 옆이 열려있는 곳으로 둠
						int blank[]= {0,0,0,0,0,0,0,0};	
						/* [0	1 	2	(인덱스)
						 *  3	백	4	 짝 - (0,7) (1,6) (2,5) (3,4)
						 *  5	6	7] */
						int count=0;
						boolean stopCount=false;
						
						for(int k=-1; k<2; k++) {
							if (stopCount == true) break;					// 3중 for문 나가기
							for(int l=-1; l<2; l++) {
								if(k==0 && l==0) continue;					// 해당 백돌은 건너뛰기
								if(map.getMap(i+l, j+k) == 0) {
									blank[count] = 1;
								}
								System.out.print(blank[count] + " ");
								count++;
								for(int p=0; p<4; p++) {
									if (blank[p]==1 && blank[7-p]==1 && 0<=i+l && i+l<=18 && 0<=j+k && j+k<=18 && map.getMap(i+l, j+k)==0) {		// 양 옆이 열려있는 곳 우선으로 둠		
										map.setMap(i+l,j+k);
										stopCount = true;
										break;
									}
								}
								if (stopCount == true) break;
							}
						}
						if(stopCount == false) continue;
						state=2;
						stopAlphaOh=true;
						break;
					}
					else if(state==1) {										// 방어 상태, 흑돌의 연속이 3개 이상일 때 방어
						if(currentAI == 2222 || (currentAI != 111 && currentAI != 1111)) {	// 우선 순위 설정
							map.setMap(attackX,attackY);
							state = 2;
							stopAlphaOh=true;
							break;
						}
						else {
							map.setMap(blockX,blockY);
							stopAlphaOh=true;
							break;
						}
					}
					else if(state==2) {										// 공격 상태, 자신의 돌을 이어감 
						if(currentAI!=2222 && (currentAI == 111 || currentAI == 1111)) {	// 우선 순위 설정
							map.setMap(blockX,blockY);
							state = 1;
							stopAlphaOh=true;
							break;
						}
						else {
							map.setMap(attackX,attackY);
							stopAlphaOh=true;
							break;
						}
					}
					if (stopAlphaOh) break;
				}
			}
		//}
		map.turnChange();
		map.endTurn=false;			// 보고 없애도 될 듯
		//mouse.printText(x, y);	// ** 백돌 위치 출력 함수, x가 이전 x보다 낮을 시 안들어감 **
	}
	
	public void resetAI() {
		x=0;
		y=0;
		state=0;
		cnt=0;
		reCheck=false;
		stopAlphaOh=false;
		stopChecking=false;
		blockX=20;
		blockY=20;
		attackX=20;
		attackY=20;
	}
	
	public int CheckAI() {		// AI state 종합 체크 , 1과 2의 개수는 우선순위 차이 (많을수록 우선)
		if (CheckFieldForAI()==2222 || CheckRecordForAI ()==2222 || CheckLDiagonalForAI()==2222 || CheckRDiagonalForAI()==2222) {
			state=2;
			System.out.println("return 2222");
			return 2222;
		}
		else if (CheckFieldForAI()==1111 || CheckRecordForAI ()==1111 || CheckLDiagonalForAI()==1111 || CheckRDiagonalForAI()==1111) {
			state=1;
			System.out.println("return 1111");
			return 1111;
		}
		else if (CheckFieldForAI()==222 || CheckRecordForAI ()==222 || CheckLDiagonalForAI()==222 || CheckRDiagonalForAI()==222) {
			state=2;
			System.out.println("return 222");
			return 222;
		}
		else if (CheckFieldForAI()==111 || CheckRecordForAI ()==111 || CheckLDiagonalForAI()==111 || CheckRDiagonalForAI()==111) {
			state=1;
			System.out.println("return 111");
			return 111;
		}
		else if (CheckFieldForAI()==22 || CheckRecordForAI ()==22 || CheckLDiagonalForAI()==22 || CheckRDiagonalForAI()==22) {
			state=2;
			System.out.println("return 22");
			return 22;
		}
		else {
			reCheck=true;
			state=0;
			System.out.println("Re Check");
		}
		return 0;
	}
	
	public int CheckFieldForAI() {		// AI의 행 체크 (기준점으로 부터 아래로)
		for(int x=0;x<19;x++){
			if(stopChecking) break;
			for(int y=0;y<16;y++){
				if(((map.getMap(x,y) == 1) && (map.getMap(x,y+1) == 1) && (map.getMap(x,y+2) == 1)) 	// AI - 3연속 흑돌 방어
						&& ((map.getBlockedMap(x,y) != 1) && (map.getBlockedMap(x,y+1) != 1) && (map.getBlockedMap(x,y+2) != 1))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if((y-2>=0 && map.getMap(x, y-2)==1) && (y-1>=0 && map.getMap(x,y-1)==0)) {			// 10111 방어
							blockX=x;
							blockY=y-1;
							returningNum =1111;
						}
						else if(map.getMap(x,y+4)==1 && map.getMap(x,y+3)==0) {		// 11101 방어
							blockX=x;
							blockY=y+3;
							returningNum =1111;
						}
						else {
							if(((y-1>=0 && map.getMap(x, y-1)==2) || map.getMap(x, y+3)==2) && reCheck==false) {
								continue;
							}
							if((y-1>=0 && map.getMap(x,y-1)==0) && map.getMap(x, y+4)==2) {		// 진행하려는 곳 다음 곳이 막혀있을 경우 다른 방향으로 진행
								blockX=x;
								blockY=y-1;
							}
							else if(map.getMap(x,y+3)==0 && (y-2>=0 && map.getMap(x, y-2)==2)) {
								blockX=x;
								blockY=y+3;
							}
							else if(y-1>=0 && map.getMap(x,y-1)==0) {
								blockX=x;
								blockY=y-1;
							}
							else if(map.getMap(x,y+3)==0) {
								blockX=x;
								blockY=y+3;
							}
							else {
								continue;
							}	
							returningNum = 111;
						}
						map.blockedMap[x][y]=1;			// 한번 체크 하고 다음 턴에 또 다시 체크되는 것을 방지
						map.blockedMap[x][y+1]=1;
						map.blockedMap[x][y+2]=1;
					}
				}
				if(((map.getMap(x,y) == 1) && (map.getMap(x,y+1) == 1) && (map.getMap(x,y+2) == 1) && (map.getMap(x,y+3) == 1)) // AI - 4연속 흑돌 방어
						&& ((map.getBlockedMap(x,y) != 2) && (map.getBlockedMap(x,y+1) != 2) && (map.getBlockedMap(x,y+2) != 2) && (map.getBlockedMap(x,y+3) != 2))) {
					if(returningNum!=2222) {
						if(y-1>=0 && map.getMap(x,y-1)==0) {
							blockX=x;
							blockY=y-1;
						}
						else if(map.getMap(x,y+4)==0) {
							blockX=x;
							blockY=y+4;
						}
						else {
							continue;
						}
						map.blockedMap[x][y]=2;
						map.blockedMap[x][y+1]=2;
						map.blockedMap[x][y+2]=2;
						map.blockedMap[x][y+3]=2;
						
						returningNum = 1111;
					}
				}

				if(((map.getMap(x,y) == 2) && (map.getMap(x,y+1) == 2)) &&
						((map.getBlockedMap(x,y) != 3) && (map.getBlockedMap(x,y+1) != 3))) {// AI - 2연속 백돌 공격
					if(returningNum==0) {
						if(((y-1>=0 && map.getMap(x, y-1)==1) || map.getMap(x, y+2)==1) && reCheck==false) {
							continue;
						}
						if(y-1>=0 && map.getMap(x,y-1)==0) {
							attackX=x;
							attackY=y-1;
						}
						else if(map.getMap(x,y+2)==0) {
							attackX=x;
							attackY=y+2;
						}
						else {
							continue;
						}
						map.blockedMap[x][y]=3;
						map.blockedMap[x][y+1]=3;
						returningNum = 22;
					}
				}
				
				if(((map.getMap(x,y) == 2) && (map.getMap(x,y+1) == 2) && (map.getMap(x,y+2) == 2)) &&			
						((map.getBlockedMap(x,y) != 4) && (map.getBlockedMap(x,y+1) != 4) && (map.getBlockedMap(x,y+2) != 4)))  {	// AI - 3연속 백돌 공격
					if(returningNum!=1111 && returningNum!=2222) {
						if((y-2>=0 && map.getMap(x, y-2)==2) && (y-1>=0 && map.getMap(x,y-1)==0)) {			// 10111 공격
							attackX=x;
							attackY=y-1;
							returningNum =2222;
						}
						else if(map.getMap(x,y+4)==2 && map.getMap(x,y+3)==0) {		// 11101 공격
							attackX=x;
							attackY=y+3;
							returningNum =2222;
						}
						else {
							if(((y-1>=0 && map.getMap(x, y-1)==1) || map.getMap(x, y+3)==1) && reCheck==false) {
								continue;
							}
							if((y-1>=0 && map.getMap(x,y-1)==0) && map.getMap(x, y+4)==1) {		// 진행하려는 곳 다음 곳이 막혀있을 경우 다른 방향으로 진행
								attackX=x;
								attackX=y-1;
							}
							else if(map.getMap(x,y+3)==0 && (y-2>=0 && map.getMap(x, y-2)==1)) {
								attackX=x;
								attackX=y+3;
							}
							else if(y-1>=0 && map.getMap(x,y-1)==0) {
								attackX=x;
								attackY=y-1;
							}
							else if(map.getMap(x,y+3)==0) {
								attackX=x;
								attackY=y+3;
							}
							else {
								continue;
							}
							returningNum = 222;
						}
						map.blockedMap[x][y]=4;
						map.blockedMap[x][y+1]=4;
						map.blockedMap[x][y+2]=4;						
					}
				}
				
				if(((map.getMap(x,y) == 2) && (map.getMap(x,y+1) == 2) && (map.getMap(x,y+2) == 2) && (map.getMap(x,y+3) == 2)) &&	// AI - 4연속 백돌 공격
						((map.getBlockedMap(x,y) != 5) && (map.getBlockedMap(x,y+1) != 5) && (map.getBlockedMap(x,y+2) != 5) && (map.getBlockedMap(x,y+3) != 5))) {
					if(y-1>=0 && map.getMap(x,y-1)==0) {
						attackX=x;
						attackY=y-1;
					}
					else if(map.getMap(x,y+4)==0) {
						attackX=x;
						attackY=y+4;
					}
					else {
						continue;
					}
					map.blockedMap[x][y]=5;
					map.blockedMap[x][y+1]=5;
					map.blockedMap[x][y+2]=5;
					map.blockedMap[x][y+3]=5;
					
					returningNum = 2222;
				}
				
				if(((map.getMap(x,y) == 1) && (map.getMap(x,y+1) == 0) && (map.getMap(x,y+2) == 1) && (map.getMap(x,y+3) == 1)) 	// AI - 1011 방어
						&& ((map.getBlockedMap(x,y) != 6) && (map.getBlockedMap(x,y+1) != 6) && (map.getBlockedMap(x,y+2) != 6) && (map.getBlockedMap(x,y+3) != 6))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if(((y-1>=0 && map.getMap(x, y-1)==2) || map.getMap(x, y+4)==2) && reCheck==false) {
							continue;
						}
						blockX=x;
						blockY=y+1;
						map.blockedMap[x][y]=6;
						map.blockedMap[x][y+1]=6;
						map.blockedMap[x][y+2]=6;
						map.blockedMap[x][y+3]=6;

						returningNum = 111;
					}
				}
				if(((map.getMap(x,y) == 1) && (map.getMap(x,y+1) == 1) && (map.getMap(x,y+2) == 0) && (map.getMap(x,y+3) == 1)) 	// AI - 1101 방어, 11011 방어
						&& ((map.getBlockedMap(x,y) != 7) && (map.getBlockedMap(x,y+1) != 7) && (map.getBlockedMap(x,y+2) != 7) && (map.getBlockedMap(x,y+3) != 7))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if(map.getMap(x, y+4)==1) {
							blockX=x;
							blockY=y+2;
							map.blockedMap[x][y]=8;
							map.blockedMap[x][y+1]=8;
							map.blockedMap[x][y+2]=8;
							map.blockedMap[x][y+3]=8;
							map.blockedMap[x][y+4]=8;
	
							returningNum = 1111;
						}
						else {
							if(((y-1>=0 && map.getMap(x, y-1)==2) || map.getMap(x, y+4)==2) && reCheck==false) {
								continue;
							}
							blockX=x;
							blockY=y+2;
							map.blockedMap[x][y]=7;
							map.blockedMap[x][y+1]=7;
							map.blockedMap[x][y+2]=7;
							map.blockedMap[x][y+3]=7;
	
							returningNum = 111;
						}
					}
				}
				
				if(((map.getMap(x,y) == 2) && (map.getMap(x,y+1) == 0) && (map.getMap(x,y+2) == 2) && (map.getMap(x,y+3) == 2)) 	// AI - 1011 공격
						&& ((map.getBlockedMap(x,y) != 9) && (map.getBlockedMap(x,y+1) != 9) && (map.getBlockedMap(x,y+2) != 9) && (map.getBlockedMap(x,y+3) != 9))) {
					if(returningNum!=1111 && returningNum!=2222) {
						if(((y-1>=0 && map.getMap(x, y-1)==1) || map.getMap(x, y+4)==1) && reCheck==false) {
							continue;
						}
						attackX=x;
						attackY=y+1;
						map.blockedMap[x][y]=9;
						map.blockedMap[x][y+1]=9;
						map.blockedMap[x][y+2]=9;
						map.blockedMap[x][y+3]=9;

						returningNum = 222;
					}
				}
				if(((map.getMap(x,y) == 2) && (map.getMap(x,y+1) == 2) && (map.getMap(x,y+2) == 0) && (map.getMap(x,y+3) == 2)) 	// AI - 1101 공격, 11011 공격
						&& ((map.getBlockedMap(x,y) != 10) && (map.getBlockedMap(x,y+1) != 10) && (map.getBlockedMap(x,y+2) != 10) && (map.getBlockedMap(x,y+3) != 10))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if(map.getMap(x,y+4)==2) {
							attackX=x;
							attackY=y+2;
							map.blockedMap[x][y]=-1;
							map.blockedMap[x][y+1]=-1;
							map.blockedMap[x][y+2]=-1;
							map.blockedMap[x][y+3]=-1;
							map.blockedMap[x][y+4]=-1;

							returningNum = 2222;
						}
						else {
							if(((y-1>=0 && map.getMap(x, y-1)==1) || map.getMap(x, y+4)==1) && reCheck==false) {
								continue;
							}
							attackX=x;
							attackY=y+2;
							map.blockedMap[x][y]=10;
							map.blockedMap[x][y+1]=10;
							map.blockedMap[x][y+2]=10;
							map.blockedMap[x][y+3]=10;
	
							returningNum = 222;
						}
					}
				}
			}
		}
		return returningNum;
	}
	
	public int CheckRecordForAI() {		// AI의 열 체크 (기준점으로 부터 오른쪽으로)
		for(int x=0;x<16;x++){
			if(stopChecking) break;
			for(int y=0;y<19;y++){
				if(((map.getMap(x,y) == 1) && (map.getMap(x+1,y) == 1) && (map.getMap(x+2,y) == 1)) 									// AI - 3연속 흑돌 방어
						&& ((map.getBlockedMap(x,y) != 11) && (map.getBlockedMap(x+1,y) != 11) && (map.getBlockedMap(x+2,y) != 11))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if((x-2>=0 && map.getMap(x-2, y)==1) && (x-1>=0 && map.getMap(x-1,y)==0)) {			// 10111 방어
							blockX=x-1;
							blockY=y;
							returningNum =1111;
						}
						else if(map.getMap(x+4,y)==1 && map.getMap(x+3,y)==0) {		// 11101 방어
							blockX=x+3;
							blockY=y;
							returningNum =1111;
						}
						else {
							if(((x-1>=0 && map.getMap(x-1, y)==2) || map.getMap(x+3, y)==2) && reCheck==false) {
								continue;
							}
							if((x-1>=0 && map.getMap(x-1,y)==0) && map.getMap(x+4, y)==2) {		// 진행하려는 곳 다음 곳이 막혀있을 경우 다른 방향으로 진행
								blockX=x-1;
								blockY=y;
							}
							else if(map.getMap(x+3,y)==0 && (x-2>=0 && map.getMap(x, y-2)==2)) {
								blockX=x+3;
								blockY=y;
							}
							else if(x-1>=0 && map.getMap(x-1,y)==0) {
								blockX=x-1;
								blockY=y;
							}
							else if(map.getMap(x+3,y)==0) {
								blockX=x+3;
								blockY=y;
							}
							else {
								continue;
							}
							returningNum = 111;
						}
						map.blockedMap[x][y]=11;
						map.blockedMap[x+1][y]=11;
						map.blockedMap[x+2][y]=11;						
					}
				}
				
				if(((map.getMap(x,y) == 1) && (map.getMap(x+1,y) == 1) && (map.getMap(x+2,y) == 1) && (map.getMap(x+3,y) == 1)) 	// AI - 4연속 흑돌 방어
						&& ((map.getBlockedMap(x,y) != 12) && (map.getBlockedMap(x+1,y) != 12) && (map.getBlockedMap(x+2,y) != 12) && (map.getBlockedMap(x+3,y) != 12))) {
					if(returningNum!=2222) {
						if(x-1>=0 && map.getMap(x-1,y)==0) {
							blockX=x-1;
							blockY=y;
						}
						else if(map.getMap(x+4,y)==0) {
							blockX=x+4;
							blockY=y;
						}
						else {
							continue;
						}
						map.blockedMap[x][y]=12;
						map.blockedMap[x+1][y]=12;
						map.blockedMap[x+2][y]=12;
						map.blockedMap[x+3][y]=12;
						
						returningNum = 1111;
					}
				}

				if(((map.getMap(x,y) == 2) && (map.getMap(x+1,y) == 2)) &&
						((map.getBlockedMap(x,y) != 13) && (map.getBlockedMap(x+1,y) != 13))) {											// AI - 2연속 백돌 공격
					if(returningNum==0) {
						if(((x-1>=0 && map.getMap(x-1, y)==1) || map.getMap(x+2, y)==1) && reCheck==false) {
							continue;
						}
						if(x-1>=0 && map.getMap(x-1,y)==0) {
							attackX=x-1;
							attackY=y;
						}
						else if(map.getMap(x+2,y)==0) {
							attackX=x+2;
							attackY=y;
						}
						else {
							continue;
						}
						map.blockedMap[x][y]=13;
						map.blockedMap[x+1][y]=13;
					
						returningNum = 22;
					}
				}
				
				if(((map.getMap(x,y) == 2) && (map.getMap(x+1,y) == 2) && (map.getMap(x+2,y) == 2)) &&			
						((map.getBlockedMap(x,y) != 14) && (map.getBlockedMap(x+1,y) != 14) && (map.getBlockedMap(x+2,y) != 14)))  {	// AI - 3연속 백돌 공격
					if(returningNum!=1111) {
						if((x-2>=0 && map.getMap(x-2, y)==2) && map.getMap(x-1,y)==0) {			// 10111 공격
							attackX=x-1;
							attackY=y;
							returningNum =2222;
						}
						else if(map.getMap(x+4,y)==2 && map.getMap(x+3,y)==0) {		// 11101 공격
							attackX=x+3;
							attackY=y;
							returningNum =2222;
						}
						else {
							if(((x-1>=0 && map.getMap(x-1, y)==1) || map.getMap(x+3, y)==1) && reCheck==false) {
								continue;
							}
							if((x-1>=0 && map.getMap(x-1,y)==0) && map.getMap(x+4, y)==1) {		// 진행하려는 곳 다음 곳이 막혀있을 경우 다른 방향으로 진행
								attackX=x-1;
								attackY=y;
							}
							else if(map.getMap(x+3,y)==0 && (x-2>=0 && map.getMap(x-2, y)==1)) {
								attackX=x+3;
								attackY=y;
							}
							else if(x-1>=0 && map.getMap(x-1,y)==0) {
								attackX=x-1;
								attackY=y;
							}
							else if(map.getMap(x+3,y)==0) {
								attackX=x+3;
								attackY=y;
							}
							else {
								continue;
							}
							returningNum = 222;
						}
						map.blockedMap[x][y]=14;
						map.blockedMap[x+1][y]=14;
						map.blockedMap[x+2][y]=14;						
					}
				}
				if(((map.getMap(x,y) == 2) && (map.getMap(x+1,y) == 2) && (map.getMap(x+2,y) == 2) && (map.getMap(x+3,y) == 2)) &&	// AI - 4연속 백돌 공격
						((map.getBlockedMap(x,y) != 15) && (map.getBlockedMap(x+1,y) != 15) && (map.getBlockedMap(x+2,y) != 15) && (map.getBlockedMap(x+3,y) != 15))) {
					if(x-1>=0 && map.getMap(x-1,y)==0) {
						attackX=x-1;
						attackY=y;
					}
					else if(map.getMap(x+4,y)==0) {
						attackX=x+4;
						attackY=y;
					}
					else {
						continue;
					}
					map.blockedMap[x][y]=15;
					map.blockedMap[x+1][y]=15;
					map.blockedMap[x+2][y]=15;
					map.blockedMap[x+3][y]=15;
					
					returningNum = 2222;
				}
				
				if(((map.getMap(x,y) == 1) && (map.getMap(x+1,y) == 0) && (map.getMap(x+2,y) == 1) && (map.getMap(x+3,y) == 1)) 	// AI - 1011 방어
						&& ((map.getBlockedMap(x,y) != 16) && (map.getBlockedMap(x+1,y) != 16) && (map.getBlockedMap(x+2,y) != 16) && (map.getBlockedMap(x+3,y) != 16))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if(((x-1>=0 && map.getMap(x-1, y)==2) || map.getMap(x+4, y)==2) && reCheck==false) {
							continue;
						}
						blockX=x+1;
						blockY=y;
						map.blockedMap[x][y]=16;
						map.blockedMap[x+1][y]=16;
						map.blockedMap[x+2][y]=16;
						map.blockedMap[x+3][y]=16;

						returningNum = 111;
					}
				}
				if(((map.getMap(x,y) == 1) && (map.getMap(x+1,y) == 1) && (map.getMap(x+2,y) == 0) && (map.getMap(x+3,y) == 1)) 	// AI - 1101 방어, 11011 방어
						&& ((map.getBlockedMap(x,y) != 17) && (map.getBlockedMap(x+1,y) != 17) && (map.getBlockedMap(x+2,y) != 17) && (map.getBlockedMap(x+3,y) != 17))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if(map.getMap(x+4, y)==1) {
							blockX=x+2;
							blockY=y;
							map.blockedMap[x][y]=18;
							map.blockedMap[x+1][y]=18;
							map.blockedMap[x+2][y]=18;
							map.blockedMap[x+3][y]=18;
							map.blockedMap[x+4][y]=18;

							returningNum = 1111;
						}
						else {
							if(((x-1>=0 && map.getMap(x-1, y)==2) || map.getMap(x+4, y)==2) && reCheck==false) {
								continue;
							}
							blockX=x+2;
							blockY=y;
							map.blockedMap[x][y]=17;
							map.blockedMap[x+1][y]=17;
							map.blockedMap[x+2][y]=17;
							map.blockedMap[x+3][y]=17;
	
							returningNum = 111;
						}
					}
				}
				
				if(((map.getMap(x,y) == 2) && (map.getMap(x+1,y) == 0) && (map.getMap(x+2,y) == 2) && (map.getMap(x+3,y) == 2)) 	// AI - 1011 공격
						&& ((map.getBlockedMap(x,y) != 19) && (map.getBlockedMap(x+1,y) != 19) && (map.getBlockedMap(x+2,y) != 19) && (map.getBlockedMap(x+3,y) != 19))) {
					if(returningNum!=1111 && returningNum!=2222) {
						if((x-1>=0 && map.getMap(x-1, y)==1) || map.getMap(x+4, y)==1 && reCheck==false) {
							continue;
						}
						attackX=x+1;
						attackY=y;
						map.blockedMap[x][y]=19;
						map.blockedMap[x+1][y]=19;
						map.blockedMap[x+2][y]=19;
						map.blockedMap[x+3][y]=19;

						returningNum = 222;
					}
				}
				if(((map.getMap(x,y) == 2) && (map.getMap(x+1,y) == 2) && (map.getMap(x+2,y) == 0) && (map.getMap(x+3,y) == 2)) 	// AI - 1101 공격, 11011 공격
						&& ((map.getBlockedMap(x,y) != 20) && (map.getBlockedMap(x+1,y) != 20) && (map.getBlockedMap(x+2,y) != 20) && (map.getBlockedMap(x+3,y) != 20))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if(map.getMap(x+4, y)==2) {
							attackX=x+2;
							attackY=y;
							map.blockedMap[x][y]=-10;
							map.blockedMap[x+1][y]=-10;
							map.blockedMap[x+2][y]=-10;
							map.blockedMap[x+3][y]=-10;
							map.blockedMap[x+4][y]=-10;

							returningNum = 2222;
						}
						else {
							if(((x-1>=0 && map.getMap(x-1, y)==1) || map.getMap(x+4, y)==1) && reCheck==false) {
								continue;
							}
							attackX=x+2;
							attackY=y;
							map.blockedMap[x][y]=20;
							map.blockedMap[x+1][y]=20;
							map.blockedMap[x+2][y]=20;
							map.blockedMap[x+3][y]=20;
	
							returningNum = 222;
						}
					}
				}
			}
		}
		return returningNum;
	}
	
	public int CheckLDiagonalForAI() {		// AI의 왼쪽 대각선 체크 (기준점으로 부터 왼쪽 아래로)
		for(int x=0;x<19;x++){
			if(stopChecking) break;
			for(int y=0;y<19;y++){
				if(x-4>=0 && y+4<=19){
				if(((map.getMap(x,y) == 1) && (map.getMap(x-1,y+1) == 1) && (map.getMap(x-2,y+2) == 1)) 									// AI - 3연속 흑돌 방어
						&& ((map.getBlockedMap(x,y) != 21) && (map.getBlockedMap(x-1,y+1) != 21) && (map.getBlockedMap(x-2,y+2) != 21))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if((y-2>=0 && map.getMap(x+2, y-2)==1) && (y-1>=0 && map.getMap(x+1,y-1)==0)) {			// 10111 방어
							blockX=x+1;
							blockY=y-1;
							returningNum =1111;
						}
						else if(map.getMap(x-4,y+4)==1 && map.getMap(x-3,y+3)==0) {		// 11101 방어
							blockX=x-3;
							blockY=y+3;
							returningNum =1111;
						}
						else {
							if(((y-1>=0 && map.getMap(x+1, y-1)==2) || map.getMap(x-3, y+3)==2) && reCheck==false) {
								continue;
							}
							if((y-1>=0 && map.getMap(x+1,y-1)==0) && map.getMap(x-4, y+4)==2) {		// 진행하려는 곳 다음 곳이 막혀있을 경우 다른 방향으로 진행
								blockX=x+1;
								blockY=y-1;
							}
							else if(map.getMap(x-3,y+3)==0 && (y-2>=0 && map.getMap(x+2, y-2)==2)) {
								blockX=x-3;
								blockY=y+3;
							}
							else if(y-1>=0 && map.getMap(x+1,y-1)==0) {
								blockX=x+1;
								blockY=y-1;
							}
							else if(map.getMap(x-3,y+3)==0) {
								blockX=x-3;
								blockY=y+3;
							}
							else {
								continue;
							}	
							returningNum = 111;
						}
						map.blockedMap[x][y]=21;
						map.blockedMap[x-1][y+1]=21;
						map.blockedMap[x-2][y+2]=21;
					}
				}
				
				if(((map.getMap(x,y) == 1) && (map.getMap(x-1,y+1) == 1) && (map.getMap(x-2,y+2) == 1) && (map.getMap(x-3,y+3) == 1)) // AI - 4연속 흑돌 방어
						&& ((map.getBlockedMap(x,y) != 22) && (map.getBlockedMap(x-1,y+1) != 22) && (map.getBlockedMap(x-2,y+2) != 22) && (map.getBlockedMap(x-3,y+3) != 22))) {
					if(returningNum!=2222) {
						if(y-1>=0 && map.getMap(x+1,y-1)==0) {
							blockX=x+1;
							blockY=y-1;
						}
						else if(map.getMap(x-4,y+4)==0) {
							blockX=x-4;
							blockY=y+4;
						}
						else {
							continue;
						}
						map.blockedMap[x][y]=22;
						map.blockedMap[x-1][y+1]=22;
						map.blockedMap[x-2][y+2]=22;
						map.blockedMap[x-3][y+3]=22;
						
						returningNum = 1111;
					}
				}

				if(((map.getMap(x,y) == 2) && (map.getMap(x-1,y+1) == 2)) &&
						((map.getBlockedMap(x,y) != 23) && (map.getBlockedMap(x-1,y+1) != 23))) {											// AI - 2연속 백돌 공격
					if(returningNum==0) {
						if((map.getMap(x+1, y-1)==1 || map.getMap(x-2, y+2)==1) && reCheck==false) {
							continue;
						}
						if(y-1>=0 && map.getMap(x+1,y-1)==0) {
							attackX=x+1;
							attackY=y-1;
						}
						else if(map.getMap(x-2,y+2)==0) {
							attackX=x-2;
							attackY=y+2;
						}
						else {
							continue;
						}
						map.blockedMap[x][y]=23;
						map.blockedMap[x-1][y+1]=23;
					
						returningNum = 22;
					}
				}
				
				if(((map.getMap(x,y) == 2) && (map.getMap(x-1,y+1) == 2) && (map.getMap(x-2,y+2) == 2)) &&			
						((map.getBlockedMap(x,y) != 24) && (map.getBlockedMap(x-1,y+1) != 24) && (map.getBlockedMap(x-2,y+2) != 24)))  {	// AI - 3연속 백돌 공격
					if(returningNum!=1111) {
						if((y-2>=0 && map.getMap(x+2, y-2)==2) && (y-1>=0 && map.getMap(x+1,y-1)==0)) {			// 10111 공격
							attackX=x+1;
							attackY=y-1;
							returningNum =2222;
						}
						else if(map.getMap(x-4,y+4)==2 && map.getMap(x-3,y+3)==0) {		// 11101 공격
							attackX=x-3;
							attackY=y+3;
							returningNum =2222;
						}
						else {
							if(((y-1>=0 && map.getMap(x+1, y-1)==1) || map.getMap(x-3, y+3)==1) && reCheck==false) {
								continue;
							}
							if((y-1>=0 && map.getMap(x+1,y-1)==0) && map.getMap(x-4, y+4)==1) {		// 진행하려는 곳 다음 곳이 막혀있을 경우 다른 방향으로 진행
								attackX=x+1;
								attackY=y-1;
							}
							else if(map.getMap(x-3,y+3)==0 && (y-2>=0 &&  map.getMap(x+2, y-2)==1)) {
								attackX=x-3;
								attackY=y+3;
							}
							else if(y-1>=0 && map.getMap(x+1,y-1)==0) {
								attackX=x+1;
								attackY=y-1;
							}
							else if(map.getMap(x-3,y+3)==0) {
								attackX=x-3;
								attackY=y+3;
							}
							else {
								continue;
							}						
							returningNum = 222;
						}
						map.blockedMap[x][y]=24;
						map.blockedMap[x-1][y+1]=24;
						map.blockedMap[x-2][y+2]=24;
					}
				}
					
				if(((map.getMap(x,y) == 2) && (map.getMap(x-1,y+1) == 2) && (map.getMap(x-2,y+2) == 2) && (map.getMap(x-3,y+3) == 2)) &&// AI - 4연속 백돌 공격
						((map.getBlockedMap(x,y) != 25) && (map.getBlockedMap(x-1,y+1) != 25) && (map.getBlockedMap(x-2,y+2) != 25) && (map.getBlockedMap(x-3,y+3) != 25))) {
					if(y-1>=0 && map.getMap(x+1,y-1)==0) {
						attackX=x+1;
						attackY=y-1;
					}
					else if(map.getMap(x-4,y+4)==0) {
						attackX=x-4;
						attackY=y+4;
					}
					else {
						continue;
					}
					map.blockedMap[x][y]=25;
					map.blockedMap[x-1][y+1]=25;
					map.blockedMap[x-2][y+2]=25;
					map.blockedMap[x-3][y+3]=25;
					
					returningNum = 2222;
				}
				
				if(((map.getMap(x,y) == 1) && (map.getMap(x-1,y+1) == 0) && (map.getMap(x-2,y+2) == 1) && (map.getMap(x-3,y+3) == 1)) 	// AI - 1011 방어
						&& ((map.getBlockedMap(x,y) != 26) && (map.getBlockedMap(x-1,y+1) != 26) && (map.getBlockedMap(x-2,y+2) != 26) && (map.getBlockedMap(x-3,y+3) != 26))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if(((y-1>=0 && map.getMap(x+1, y-1)==2) || map.getMap(x-4, y+4)==2) && reCheck==false) {
							continue;
						}
						blockX=x-1;
						blockY=y+1;
						map.blockedMap[x][y]=26;
						map.blockedMap[x-1][y+1]=26;
						map.blockedMap[x-2][y+2]=26;
						map.blockedMap[x-3][y+3]=26;

						returningNum = 111;
					}
				}
				if(((map.getMap(x,y) == 1) && (map.getMap(x-1,y+1) == 1) && (map.getMap(x-2,y+2) == 0) && (map.getMap(x-3,y+3) == 1)) 	// AI - 1101 방어, 11011 방어
						&& ((map.getBlockedMap(x,y) != 27) && (map.getBlockedMap(x-1,y+1) != 27) && (map.getBlockedMap(x-2,y+2) != 27) && (map.getBlockedMap(x-3,y+3) != 27))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if(map.getMap(x-4, y+4)==1) {
							blockX=x-2;
							blockY=y+2;
							map.blockedMap[x][y]=28;
							map.blockedMap[x-1][y+1]=28;
							map.blockedMap[x-2][y+2]=28;
							map.blockedMap[x-3][y+3]=28;
							map.blockedMap[x-4][y+4]=28;

							returningNum = 1111;
						}
						else {
							if(((y-1>=0 && map.getMap(x+1, y-1)==2) || map.getMap(x-4, y+4)==2) && reCheck==false) {
								continue;
							}
							blockX=x-2;
							blockY=y+2;
							map.blockedMap[x][y]=27;
							map.blockedMap[x-1][y+1]=27;
							map.blockedMap[x-2][y+2]=27;
							map.blockedMap[x-3][y+3]=27;
	
							returningNum = 111;
						}
					}
				}
				if(((map.getMap(x,y) == 2) && (map.getMap(x-1,y+1) == 0) && (map.getMap(x-2,y+2) == 2) && (map.getMap(x-3,y+3) == 2)) 	// AI - 1011 공격
						&& ((map.getBlockedMap(x,y) != 29) && (map.getBlockedMap(x-1,y+1) != 29) && (map.getBlockedMap(x-2,y+2) != 29) && (map.getBlockedMap(x-3,y+3) != 29))) {
					if(returningNum!=1111 && returningNum!=2222) {
						if(((y-1>=0 && map.getMap(x+1, y-1)==1) || map.getMap(x-4, y+4)==1) && reCheck==false) {
							continue;
						}
						attackX=x-1;
						attackY=y+1;
						map.blockedMap[x][y]=9;
						map.blockedMap[x-1][y+1]=9;
						map.blockedMap[x-2][y+2]=9;
						map.blockedMap[x-3][y+3]=9;

						returningNum = 222;
					}
				}
				if(((map.getMap(x,y) == 2) && (map.getMap(x-1,y+1) == 2) && (map.getMap(x-2,y+2) == 0) && (map.getMap(x-3,y+3) == 2)) 	// AI - 1101 공격, 11011 공격
						&& ((map.getBlockedMap(x,y) != 30) && (map.getBlockedMap(x-1,y+1) != 30) && (map.getBlockedMap(x-2,y+2) != 30) && (map.getBlockedMap(x-3,y+3) != 30))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if(map.getMap(x-4, y+4)==2) {
							attackX=x-2;
							attackY=y+2;
							map.blockedMap[x][y]=-20;
							map.blockedMap[x-1][y+1]=-20;
							map.blockedMap[x-2][y+2]=-20;
							map.blockedMap[x-3][y+3]=-20;
							map.blockedMap[x-4][y+4]=-20;

							returningNum = 2222;
						}
						else {
							if(((y-1>=0 && map.getMap(x+1, y-1)==1) || map.getMap(x-4, y+4)==1) && reCheck==false) {
								continue;
							}
							attackX=x-2;
							attackY=y+2;
							map.blockedMap[x][y]=10;
							map.blockedMap[x-1][y+1]=10;
							map.blockedMap[x-2][y+2]=10;
							map.blockedMap[x-3][y+3]=10;
	
							returningNum = 222;
						}
					}
				}
				}
			}
		}
		return returningNum;
	}

	
	public int CheckRDiagonalForAI() {		// AI의 오른쪽 대각선 체크 (기준점으로 부터 오른쪽 아래로)
		for(int x=0;x<19;x++){
			if(stopChecking) break;
			for(int y=0;y<19;y++){
				if(x+4<=19 && y+4<=19) {
				if(((map.getMap(x,y) == 1) && (map.getMap(x+1,y+1) == 1) && (map.getMap(x+2,y+2) == 1)) 									// AI - 3연속 흑돌 방어
						&& ((map.getBlockedMap(x,y) != 31) && (map.getBlockedMap(x+1,y+1) != 31) && (map.getBlockedMap(x+2,y+2) != 31))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if((x-2>=0 && y-2>=0 && map.getMap(x-2, y-2)==1) && (x-1>=0 && y-1>=0 && map.getMap(x-1,y-1)==0)) {			// 10111 방어
							blockX=x-1;
							blockY=y-1;
							returningNum =1111;
						}
						else if(map.getMap(x+4,y+4)==1 && map.getMap(x+3,y+3)==0) {		// 11101 방어
							blockX=x+3;
							blockY=y+3;
							returningNum =1111;
						}
						else {
							if(((x-1>=0 && y-1>=0 && map.getMap(x-1, y-1)==2) || map.getMap(x+3, y+3)==2) && reCheck==false) {
								continue;
							}
							if((x-1>=0 && y-1>=0 && map.getMap(x-1,y-1)==0) && map.getMap(x+4, y+4)==2) {		// 진행하려는 곳 다음 곳이 막혀있을 경우 다른 방향으로 진행
								blockX=x-1;
								blockY=y-1;
							}
							else if(map.getMap(x+3,y+3)==0 && (x-2>=0 && y-2>=0 && map.getMap(x-2, y-2)==2)) {
								blockX=x+3;
								blockY=y+3;
							}
							else if(x-1>=0 && y-1>=0 && map.getMap(x-1,y-1)==0) {
								blockX=x-1;
								blockY=y-1;
							}
							else if(map.getMap(x+3,y+3)==0) {
								blockX=x+3;
								blockY=y+3;
							}
							else {
								continue;
							}							
							returningNum = 111;
						}
						map.blockedMap[x][y]=31;
						map.blockedMap[x+1][y+1]=31;
						map.blockedMap[x+2][y+2]=31;
					}
				}
				
				if(((map.getMap(x,y) == 1) && (map.getMap(x+1,y+1) == 1) && (map.getMap(x+2,y+2) == 1) && (map.getMap(x+3,y+3) == 1)) // AI - 4연속 흑돌 방어
						&& ((map.getBlockedMap(x,y) != 32) && (map.getBlockedMap(x+1,y+1) != 32) && (map.getBlockedMap(x+2,y+2) != 32) && (map.getBlockedMap(x+3,y+3) != 32))) {
					if(returningNum!=2222) {
						if(x-1>=0 && y-1>=0 && map.getMap(x-1,y-1)==0) {
							blockX=x-1;
							blockY=y-1;
						}
						else if(map.getMap(x+4,y+4)==0) {
							blockX=x+4;
							blockY=y+4;
						}
						else {
							continue;
						}
						map.blockedMap[x][y]=32;
						map.blockedMap[x+1][y+1]=32;
						map.blockedMap[x+2][y+2]=32;
						map.blockedMap[x+3][y+3]=32;
						
						returningNum = 1111;
					}
				}

				if(((map.getMap(x,y) == 2) && (map.getMap(x+1,y+1) == 2)) &&
						((map.getBlockedMap(x,y) != 33) && (map.getBlockedMap(x+1,y+1) != 33))) {											// AI - 2연속 백돌 공격
					if(returningNum==0) {
						if(((x-1>=0 && y-1>=0 && map.getMap(x-1, y-1)==1) || map.getMap(x+2, y+2)==1) && reCheck==false) {
							continue;
						}
						if(x-1>=0 && y-1>=0 && map.getMap(x-1,y-1)==0) {
							attackX=x-1;
							attackY=y-1;
						}
						else if(map.getMap(x+2,y+2)==0) {
							attackX=x+2;
							attackY=y+2;
						}
						else {
							continue;
						}
						map.blockedMap[x][y]=33;
						map.blockedMap[x+1][y+1]=33;
					
						returningNum = 22;
					}
				}
				
				if(((map.getMap(x,y) == 2) && (map.getMap(x+1,y+1) == 2) && (map.getMap(x+2,y+2) == 2)) &&			
						((map.getBlockedMap(x,y) != 34) && (map.getBlockedMap(x+1,y+1) != 34) && (map.getBlockedMap(x+2,y+2) != 34)))  {	// AI - 3연속 백돌 공격
					if(returningNum!=1111) {
						if((x-2>=0 && y-2>=0 && map.getMap(x-2, y-2)==2) && (x-1>=0 && y-1>=0 && map.getMap(x-1,y-1)==0)) {			// 10111 공격
							attackX=x-1;
							attackY=y-1;
							returningNum =2222;
						}
						else if(map.getMap(x+4,y+4)==2 & map.getMap(x+3,y+3)==0) {		// 11101 공격
							attackX=x+3;
							attackY=y+3;
							returningNum =2222;
						}
						else {
							if(((x-1>=0 && y-1>=0 && map.getMap(x-1, y-1)==1) || map.getMap(x+3, y+3)==1) && reCheck==false) {
								continue;
							}
							if((x-1>=0 && y-1>=0 && map.getMap(x-1,y-1)==0) && map.getMap(x+4, y+4)==1) {		// 진행하려는 곳 다음 곳이 막혀있을 경우 다른 방향으로 진행
								attackX=x;
								attackY=y-1;
							}
							else if(map.getMap(x+3,y+3)==0 && (x-2>=0 && y-2>=0 && map.getMap(x-2, y-2)==1)) {
								attackX=x+3;
								attackY=y+3;
							}
							else if(x-1>=0 && y-1>=0 && map.getMap(x-1,y-1)==0) {
								attackX=x-1;
								attackY=y-1;
							}
							else if(map.getMap(x+3,y+3)==0) {
								attackX=x+3;
								attackY=y+3;
							}
							else {
								continue;
							}						
							returningNum = 222;
						}
						map.blockedMap[x][y]=34;
						map.blockedMap[x+1][y+1]=34;
						map.blockedMap[x+2][y+2]=34;
					}
				}
				
				if(((map.getMap(x,y) == 2) && (map.getMap(x+1,y+1) == 2) && (map.getMap(x+2,y+2) == 2) && (map.getMap(x+3,y+3) == 2)) &&// AI - 4연속 백돌 공격
						((map.getBlockedMap(x,y) != 35) && (map.getBlockedMap(x+1,y+1) != 35) && (map.getBlockedMap(x+2,y+2) != 35) && (map.getBlockedMap(x+3,y+3) != 35))) {
					if(x-1>=0 && y-1>=0 && map.getMap(x-1,y-1)==0) {
						attackX=x-1;
						attackY=y-1;
					}
					else if(map.getMap(x+4,y+4)==0) {
						attackX=x+4;
						attackY=y+4;
					}
					else {
						continue;
					}
					map.blockedMap[x][y]=35;
					map.blockedMap[x+1][y+1]=35;
					map.blockedMap[x+2][y+2]=35;
					map.blockedMap[x+3][y+3]=35;
					
					returningNum = 2222;
				}
				
				if(((map.getMap(x,y) == 1) && (map.getMap(x+1,y+1) == 0) && (map.getMap(x+2,y+2) == 1) && (map.getMap(x+3,y+3) == 1)) 	// AI - 1011 방어
						&& ((map.getBlockedMap(x,y) != 36) && (map.getBlockedMap(x+1,y+1) != 36) && (map.getBlockedMap(x+2,y+2) != 36) && (map.getBlockedMap(x+3,y+3) != 36))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if(((x-1>=0 && y-1>=0 && map.getMap(x-1, y-1)==2) || map.getMap(x+4, y+4)==2) && reCheck==false) {
							continue;
						}
						blockX=x+1;
						blockY=y+1;
						map.blockedMap[x][y]=36;
						map.blockedMap[x+1][y+1]=36;
						map.blockedMap[x+2][y+2]=36;
						map.blockedMap[x+3][y+3]=36;

						returningNum = 111;
					}
				}
				if(((map.getMap(x,y) == 1) && (map.getMap(x+1,y+1) == 1) && (map.getMap(x+2,y+2) == 0) && (map.getMap(x+3,y+3) == 1)) 	// AI - 1101 방어, 11011 방어
						&& ((map.getBlockedMap(x,y) != 37) && (map.getBlockedMap(x+1,y+1) != 37) && (map.getBlockedMap(x+2,y+2) != 37) && (map.getBlockedMap(x+3,y+3) != 37))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if(map.getMap(x+4, y+4)==1) {
							blockX=x+2;
							blockY=y+2;
							map.blockedMap[x][y]=38;
							map.blockedMap[x+1][y+1]=38;
							map.blockedMap[x+2][y+2]=38;
							map.blockedMap[x+3][y+3]=38;
							map.blockedMap[x+4][y+4]=38;

							returningNum = 1111;
						}
						else {
							if((x-1>=0 && y-1>=0 && map.getMap(x-1, y-1)==2) || map.getMap(x+4, y+4)==2 && reCheck==false) {
								continue;
							}
							blockX=x+2;
							blockY=y+2;
							map.blockedMap[x][y]=37;
							map.blockedMap[x+1][y+1]=37;
							map.blockedMap[x+2][y+2]=37;
							map.blockedMap[x+3][y+3]=37;
	
							returningNum = 111;
						}
					}
				}
				if(((map.getMap(x,y) == 2) && (map.getMap(x+1,y+1) == 0) && (map.getMap(x+2,y+2) == 2) && (map.getMap(x+3,y+3) == 2)) 	// AI - 1011 공격
						&& ((map.getBlockedMap(x,y) != 39) && (map.getBlockedMap(x+1,y+1) != 39) && (map.getBlockedMap(x+2,y+2) != 39) && (map.getBlockedMap(x+3,y+3) != 39))) {
					if(returningNum!=1111 && returningNum!=2222) {
						if(((x-1>=0 && y-1>=0 && map.getMap(x-1, y-1)==1) || map.getMap(x+4, y+4)==1) && reCheck==false) {
							continue;
						}
						attackX=x+1;
						attackY=y+1;
						map.blockedMap[x][y]=39;
						map.blockedMap[x+1][y+1]=39;
						map.blockedMap[x+2][y+2]=39;
						map.blockedMap[x+3][y+3]=39;

						returningNum = 222;
					}
				}
				if(((map.getMap(x,y) == 2) && (map.getMap(x+1,y+1) == 2) && (map.getMap(x+2,y+2) == 0) && (map.getMap(x+3,y+3) == 2)) 	// AI - 1101 공격, 11011 공격
						&& ((map.getBlockedMap(x,y) != 40) && (map.getBlockedMap(x+1,y+1) != 40) && (map.getBlockedMap(x+2,y+2) != 40) && (map.getBlockedMap(x+3,y+3) != 40))) {
					if(returningNum!=2222 && returningNum!=1111) {
						if(map.getMap(x+4, y+4)==2) {
							attackX=x+2;
							attackY=y+2;
							map.blockedMap[x][y]=-30;
							map.blockedMap[x+1][y+1]=-30;
							map.blockedMap[x+2][y+2]=-30;
							map.blockedMap[x+3][y+3]=-30;
							map.blockedMap[x+4][y+4]=-30;

							returningNum = 2222;
						}
						if((x-1>=0 && y-1>=0 && map.getMap(x-1, y-1)==1 || map.getMap(x+4, y+4)==1) && reCheck==false) {
							continue;
						}
						attackX=x+2;
						attackY=y+2;
						map.blockedMap[x][y]=40;
						map.blockedMap[x+1][y+1]=40;
						map.blockedMap[x+2][y+2]=40;
						map.blockedMap[x+3][y+3]=40;

						returningNum = 222;
					}
				}
				}
			}
		}
		return returningNum;
	}
}
