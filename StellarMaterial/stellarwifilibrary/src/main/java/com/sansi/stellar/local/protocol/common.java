package com.sansi.stellar.local.protocol;

public class common {

	public static byte[] stringToByte(String str){
		str = str.replaceAll("0x", "");
		str = str.replaceAll("0X", "");
		String re = str.trim();
		if(re.length() > 16)
			return null;
		if(re.length() < 16)
		{
			for(int i = 0 ; i <16-re.length() ; i ++ )
			{
				re = '0' +re; 
			}
		}
		byte[] result = new byte[8];
		for(int j = 0 ; j < 16 ; j+=2)
		{
			char up,down;
			up = re.charAt(j);
			down = re.charAt(j+1);
			result[j/2] = (byte)char2Int(up, down);
			
		}
		return result;
	}
	
	public static int bytesToInt(byte src) {  
	    int value;    
	    value = (int) (src & 0xFF) ;    
	    return value;  
	} 
	
//	public static byte[] intToBytes( int value )   
//	{   
//	    byte[] src = new byte[4];  
//	    src[3] =  (byte) ((value>>24) & 0xFF);
//	    src[2] =  (byte) ((value>>16) & 0xFF);  
//	    src[1] =  (byte) ((value>>8) & 0xFF);    
//	    src[0] =  (byte) (value & 0xFF);                  
//	    return src;   
//	} 
	
	public static byte[] intToBytes( int value )   
	{   
	    byte[] src = new byte[4];  
	    src[0] =  (byte) ((value>>24) & 0xFF);
	    src[1] =  (byte) ((value>>16) & 0xFF);  
	    src[2] =  (byte) ((value>>8) & 0xFF);    
	    src[3] =  (byte) (value & 0xFF);                  
	    return src;   
	}
	
	public static byte charToByte(char c)
	{
		if( c>= '0' && c<= '9')
		{
			return (byte) (c-'0');
		}
		else if(c == 'a'|| c=='A')
			return 10;
		else if(c == 'b'|| c=='B')
			return 11;
		else if(c == 'c'|| c=='C')
			return 12;
		else if(c == 'd'|| c=='D')
			return 13;
		else if(c == 'e'|| c=='E')
			return 14;
		else if(c == 'f'|| c=='F')
			return 15;
		else return -1;
	}
	
	public static String bytes2HexString(byte[]src, String separator){
		StringBuilder stringBuilder = new StringBuilder("");
		if(src == null || src.length<=0){
			return null;
		}
		
		
		for(int i= 0;i<src.length;i++){
			
			int v = src[i] & 0XFF;
			String hv = Integer.toHexString(v);
			if(hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv).append(separator);
		}
		return stringBuilder.toString();		
	}
	
	public static int char2Int(char up,char down)
	{
		byte up_b = charToByte(up);
		byte down_b = charToByte(down);
		return ((up_b * 16) + down_b);
	}
	public static String[] Pro(String number){  
		  String[] str;  
		  int length = number.length();  
		  int group = length/2;  
		  if(0==length % 2)  
		  {  
		      str=new String[group];          
		  }  
		  else  
		  {  
		      str= new String[group+1];   
		  }  
		  for(int i=0,j=0;i<group;i++,j+=2)  
		  {  
		      str[i]=number.substring(j, j+2);  
		      if(i==(group-1))  
		      {  
		          if(1 == length % 2)                
		          {  
		              str[i+1] = number.substring(length-1,length);  
		          }            
		      }  
		  }  
		return str;  
		 }  
	
	public static int[] getTime(String Time){
		String[] ss=Time.split("\\.");
		int[] aa=new int[7];
		aa[0]=Integer.parseInt(ss[0],16);
		aa[1]=Integer.parseInt(ss[1],16);
		aa[2]=Integer.parseInt(ss[2],16);
		aa[3]=Integer.parseInt(ss[3],16);
		aa[4]=Integer.parseInt(ss[4],16);
		aa[5]=Integer.parseInt(ss[5],16);
		aa[6]=Integer.parseInt(ss[6],16);
		return aa;
		
	}
	public static int getBCD2(int a){
		String s=""+a;
		if(s.length()==1){
			s="000"+a;
		}else if(s.length()==2){
			s="00"+a;
		}else if(s.length()==3){
			s="0"+a;
		}
		String[] q=s.split("");
		int[] b=new int[4];
		b[0]=Integer.parseInt(q[0]);
		b[1]=Integer.parseInt(q[1]);
		b[2]=Integer.parseInt(q[2]);
		b[3]=Integer.parseInt(q[3]);
		return b[0]*16*16*16+b[1]*16*16+b[2]*16+b[3];
	}
	
	/**
	 * RC4????
	 * @param addr
	 * @return
	 */
	public static String HloveyRC4(String aInput,String aKey) 
    { 
        int[] iS = new int[256]; 
        byte[] iK = new byte[256]; 
        
        for (int i=0;i<256;i++) 
            iS[i]=i; 
            
        int j = 1; 
        
        for (short i= 0;i<256;i++) 
        { 
            iK[i]=(byte)aKey.charAt((i % aKey.length())); 
        } 
        
        j=0; 
        
        for (int i=0;i<255;i++) 
        { 
            j=(j+iS[i]+iK[i]) % 256; 
            int temp = iS[i]; 
            iS[i]=iS[j]; 
            iS[j]=temp; 
        } 
    
    
        int i=0; 
        j=0; 
        char[] iInputChar = aInput.toCharArray(); 
        char[] iOutputChar = new char[iInputChar.length]; 
        for(short x = 0;x<iInputChar.length;x++) 
        { 
            i = (i+1) % 256; 
            j = (j+iS[i]) % 256; 
            int temp = iS[i]; 
            iS[i]=iS[j]; 
            iS[j]=temp; 
            int t = (iS[i]+(iS[j] % 256)) % 256; 
            int iY = iS[t]; 
            char iCY = (char)iY; 
            iOutputChar[x] =(char)( iInputChar[x] ^ iCY) ;    
        } 
        
          return new String(iOutputChar); 
                
    }
	
	 public static int[] string2ASCII(String s) {// ?????????ASCII??  
	        if (s == null || "".equals(s)) {  
	            return null;  
	        }  
	  
	        char[] chars = s.toCharArray();  
	        int[] asciiArray = new int[chars.length];  
	  
	        for (int i = 0; i < chars.length; i++) {  
	            asciiArray[i] = char2ASCII(chars[i]);  
	        }  
	        return asciiArray;  
	    }  
	 
	 public static int char2ASCII(char c) {  
	        return (int) c;  
	    }  
	 
	public static int getBCD1(int a){
		String s=""+a;
		if(s.length()==1)
			s="0"+a;
		String[] q=s.split("");
		int[] b=new int[2];
		b[0]=Integer.parseInt(q[0]);
		b[1]=Integer.parseInt(q[1]);
		return b[2]*16+b[3];
	}
}
