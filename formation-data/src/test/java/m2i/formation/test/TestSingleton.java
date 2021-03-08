package m2i.formation.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import m2i.formation.Application;

public class TestSingleton {
	public static void main(String[] args) throws ParseException {

		Application app1 = Application.getInstance();

		Application app2 = Application.getInstance();

		System.out.println("app1=" + app1);
		System.out.println("app2=" + app2);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		
		Date dt1914 = sdf.parse("01/01/1970 01:00:00");
		
		System.out.println(new Date().getTime());
		
		System.out.println(sdf.format(new Date(Long.MAX_VALUE)));
	}
}
