import java.util.*;

public class SortMountains {
LinkedList<Mountain> mtn = new LinkedList<Mountain> ();
class NameCompare implements Comparator <Mountain> {
	public int compare(Mountain one, Mountain two) {
		return one.name.compareTo(two.name);
	}
}
class HeightCompare implements Comparator <Mountain>{
	public int compare(Mountain one, Mountain two) {
		return (two.height - one.height);
	}
}
	public static void main(String[] args) {
		new SortMountains().go();
		

	}
	public void go() {
		mtn.add(new Mountain ("Everest", 29029));
		mtn.add(new Mountain ("K2", 28251));
		mtn.add(new Mountain ("Kangchenjunga", 28169));
		mtn.add(new Mountain ("Lhotse", 27940));
		mtn.add(new Mountain ("Makalu", 27838));
		mtn.add(new Mountain ("Nanga Parbat", 26660));
		mtn.add(new Mountain ("Annapurna I", 26545));
		mtn.add(new Mountain ("Gasherbrum I ", 26509));
		mtn.add(new Mountain ("Cho Oyu", 26864));
		mtn.add(new Mountain ("Dhaulagiri I", 26795));
		mtn.add(new Mountain ("Manaslu", 26781));
		mtn.add(new Mountain ("Broad Peak", 26414));
		
		System.out.println("In the order of entry: /n" + mtn);
		NameCompare nc = new NameCompare();
		Collections.sort(mtn, nc);
		System.out.println("In the order of name: /n" + mtn);
		HeightCompare hc = new HeightCompare();
		Collections.sort(mtn, hc);
		System.out.println("in the order of height /n" + mtn);
		
	}

}
class Mountain {
	String name;
	int height;
	Mountain(String n, int h) {
		name = n;
		height = h;
		
	}
	public String toString() {
		return name + " " + height;
	}
	
}
