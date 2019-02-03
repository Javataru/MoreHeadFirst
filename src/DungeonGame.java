import java.io.*;

class Dungeon implements Serializable {
	public int x = 3;
	transient long y = 4;
	private short z = 5;
	int getX() {
		return x;
	}
	long getY() {
		return y;
	}
	short getZ() {
		return z;
	}
}

class DungeonGame{
	
	public static void main(String[] args) {
Dungeon d = new Dungeon();
System.out.println(d.getX() + d.getY() + d.getZ());
try {
	FileOutputStream fos = new FileOutputStream("dg.ser");
	ObjectOutputStream oos = new ObjectOutputStream(fos);
	oos.writeObject(d);
	oos.close();
	FileInputStream fis = new FileInputStream("dg.ser");
	ObjectInputStream ois = new ObjectInputStream(fis);
	d = (Dungeon) ois.readObject();
	ois.close();
	
}
catch (Exception e) {
	e.printStackTrace();
}
System.out.println(d.getX() + d.getY() + d.getZ());
	

	}

}
