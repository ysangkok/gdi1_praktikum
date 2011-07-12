package gruppe16;

import java.util.Formatter;
import java.util.List;

public class Map2DHelper<T> {
	public String getBoardString(T[][] a) {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				sb.append(f.format("%1$3s", a[i][j]));
			}
			sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	public String getBoardString(List<List<T>> a) {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter();
		for (int i = 0; i < a.size(); i++) {
			for (int j = 0; j < a.get(i).size(); j++) {
				sb.append(f.format("%1$3s", a.get(i).get(j)));
			}
			sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}
}