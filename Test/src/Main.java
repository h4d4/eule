
public class Main {
	
	private static String obfuscateIMEI(String imei){
		String result = "";
		
		for(char c : imei.toCharArray()){
			switch(c){
				case '0' : result += 'a'; break;
				case '1' : result += 'b'; break;
				case '2' : result += 'c'; break;
				case '3' : result += 'd'; break;
				case '4' : result += 'e'; break;
				case '5' : result += 'f'; break;
				case '6' : result += 'g'; break;
				case '7' : result += 'h'; break;
				case '8' : result += 'i'; break;
				case '9' : result += 'j'; break;
				default : System.err.println("Problem in obfuscateIMEI for character: " + c);
			}
		}
		return result;
	}
	public static String iterator(String imei){
		String result = "";
		char array[] = imei.toCharArray();
		
		for(int i=0; i< array.length; i++){
			if ( array[i] == '0' )
				result +='a';
			else if ( array[i] == '1' )
				result +='b';
			else if ( array[i] == '2' )
				result +='c';
			else if ( array[i] == '3' )
				result +='d';
			else if ( array[i] == '4' )
				result +='e';
			else if ( array[i] == '5' )
				result +='f';
			else if ( array[i] == '6' )
				result +='g';
			else if ( array[i] == '7' )
				result +='h';
			else if ( array[i] == '8' )
				result +='i';
			else if ( array[i] == '9' )
				result +='j';
			else 
				System.err.println("Problem in obfuscateIMEI for character: " + array[i]);
		}
		return  result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//String s = obfuscateIMEI("1234");
		//System.out.println("obfuscateIMEI: "+s);
		System.out.println("iterator: "+iterator("u235"));

	}

}
