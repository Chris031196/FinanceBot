package htmlTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Test {
	
	public static String getHTML() {
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader("test.html"));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    in.close();
		} catch (IOException e) {
		}
		return contentBuilder.toString();
	}

}
