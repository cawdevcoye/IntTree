public class Encoder {

    public int[] encodeINT(int num)
    {
        String binaryString = Integer.toBinaryString(num);
        binaryString = leftPad(binaryString, 32);

        int encoding[] = new int[binaryString.length()];
        for(int i=0;i<32;i++)
        {
            if(i==0) {
                if (num >= 0)
                    encoding[0]=1;
                else
                    encoding[0]=0;
            }
            else {
                encoding[i] =  ((binaryString.charAt(i) == '0') ? 0 : 1);
            }
        }
        return encoding;
    }
    public String leftPad(String s, int encoderLen)
    {
        int originalLen = s.length();
        for(int i=0;i<encoderLen-originalLen;i++)
        {
            s = "0"+s;
        }
        return s;
    }
}
