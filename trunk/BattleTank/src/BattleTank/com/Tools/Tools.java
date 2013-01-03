package BattleTank.com.Tools;

public class Tools {
	//------------------------------------------------------------
	//Lấy giá trị ngẫu nhiên từ khoảng min->max
	public static int getRandomIndex(int min, int max) {
		return (int) (Math.random() * (max - min + 1)) + min;
	}
}
