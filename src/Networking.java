import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Networking {
	static boolean online = true;
	public static final String HOST = "localhost";
	
	static boolean changeScore(String username, int newScore) throws IOException, NoSuchAlgorithmException{
		if(online){
			URL url = new URL("http://"+HOST+"/Boards/setScore.php?username="+username.replaceAll(" ", "+")
					+"&score="+newScore+"&md5="+getTruncatedMD5(username+Integer.toString(newScore)));
			BufferedReader in = new BufferedReader(
			new InputStreamReader(url.openStream()));
			System.out.println(url.toString());
		
			String inputLine;
			inputLine=in.readLine();
			if(!inputLine.equals("0")){
				System.out.println("error");
				in.close();
				return false;
			}
			in.close();
			return true;
		}
		return true;
	}
	
	static int getScore(String username) throws IOException{
		if(online){
			URL url = new URL("http://"+HOST+"/Boards/getScore.php?username="+username.replaceAll(" ", "+"));
			BufferedReader in = new BufferedReader(
			new InputStreamReader(url.openStream()));
		
			String inputLine;
			inputLine=in.readLine();
			if(inputLine.equals("0")){
				System.out.println("user does not exist");
				in.close();
				return 0;
			}
			System.out.println("user does exist");
			in.close();
			try{
				return Integer.parseInt(inputLine);
			}catch(NumberFormatException e){
				return 0;
			}
		}else{
			return 0;
		}
	}
	
	static String getName(String username) throws IOException{
		if(online){
			URL url = new URL("http://"+HOST+"/Boards/getName.php?username="+username.replaceAll(" ", "+"));
			BufferedReader in = new BufferedReader(
			new InputStreamReader(url.openStream()));
		
			String inputLine;
			inputLine=in.readLine();
			if(inputLine.equals("0")){
				System.out.println("user does not exist");
				in.close();
				return "Guest";
			}
			System.out.println("user does exist");
			in.close();
			return inputLine;
		}
		return "Guest";
	}
	
	static boolean userExists(String username) throws IOException{
		if(online){
			URL url = new URL("http://"+HOST+"/Boards/userExists.php?username="+username.replaceAll(" ", "+"));
			BufferedReader in = new BufferedReader(
			new InputStreamReader(url.openStream()));
		
			String inputLine;
			inputLine=in.readLine();
			if(!inputLine.equals("1")){
				System.out.println("user does not exist");
				in.close();
				return false;
			}
			System.out.println("user does exist");
			in.close();
			return true;
		}
		return true;
	}
	
	static boolean makeNewUser(String name, String username) throws IOException, NoSuchAlgorithmException{
		if(online){
			URL url = new URL("http://"+HOST+"/Boards/newUser.php?name="+name+"&username="+username.replaceAll(" ", "+")
			+"&" + "md5="+getTruncatedMD5(name+username));
			System.out.println(url.toString());
			BufferedReader in = new BufferedReader(
			new InputStreamReader(url.openStream()));
		
			String inputLine;
			inputLine=in.readLine();
			if(inputLine.equals("0")){
				System.out.println("success");
				in.close();
				return true;
			}
			in.close();
			return false;
		}
		return true;
	}
	
	static String getUserName(){
		return System.getProperty("user.name");
	}
	
	static String getMD5(String m) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(m.getBytes());
		
		byte byteData[] = md.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		
		return sb.toString();
	}
	
	static String getTruncatedMD5(String m) throws NoSuchAlgorithmException{
		return getMD5(m+Salt.salt).substring(0,10);
	}
	
	static void updateGamesCounter() throws IOException{
		if(online){
			URL counterURL = new URL("http://garrett.comze.com/advertisement/");
			counterURL.openStream();
		}
	}
}
