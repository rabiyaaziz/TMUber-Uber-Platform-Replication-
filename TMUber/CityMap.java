import java.util.Arrays;
import java.util.Scanner;
// 
// The city consists of a grid of 9 X 9 City Blocks

// Streets are east-west (1st street to 9th street)
// Avenues are north-south (1st avenue to 9th avenue)



public class CityMap
{
  // Checks for string consisting of all digits
  private static boolean allDigits(String s)
  {
    for (int i = 0; i < s.length(); i++)
      if (!Character.isDigit(s.charAt(i)))
        return false;
    return  true;
  }

  // Get all parts of address string
  
  private static String[] getParts(String address)
  {
    String parts[] = new String[3];
    
    if (address == null || address.length() == 0)
    {
      parts = new String[0];
      return parts;
    }
    int numParts = 0;
    Scanner sc = new Scanner(address);
    while (sc.hasNext())
    {
      if (numParts >= 3)
        parts = Arrays.copyOf(parts, parts.length+1);

      parts[numParts] = sc.next();
      numParts++;
    }
    if (numParts == 1)
      parts = Arrays.copyOf(parts, 1);
    else if (numParts == 2)
      parts = Arrays.copyOf(parts, 2);
    return parts;
  }

  // Checks for a valid address
  public static boolean validAddress(String address)
  {
    // There are quite a few error conditions to check for 
    // e.g. number of parts != 3
   

    String [] parts = getParts(address);
    int partscount = parts.length;
    if (partscount != 3) { return false;}
    String part1 = parts[0];
    String part2 = parts [1];
    String part3 = parts[2];

    if (part1.length()!=2) { return false; }

    else if (part2.length()!=3) { return false; }

    else if (!part3.equalsIgnoreCase("Street") && !part3.equalsIgnoreCase("Avenue")) { return false;}

    if (!Character.isDigit(part2.charAt(0))) { return false;}

    else if (!allDigits(part1)) { return false;}

    return true;
  }

  // Computes the city block coordinates from an address string
  // returns an int array of size 2. e.g. [3, 4] 
  // where 3 is the avenue and 4 the street
  public static int[] getCityBlock(String address)
  {
    int[] block = {-1, -1};
    String [] parts = getParts(address);
    String part1 = parts[0];
    String part2 = parts [1];

    block[0] = part2.charAt(0);
    block[1] = part1.charAt(0);

    return block;
  }
  
  // Calculates the distance in city blocks between the 'from' address and 'to' address
  
  // This skeleton version generates a random distance
  public static int getDistance(String from, String to)
  {
    
 
  int [] block_from = getCityBlock(from);
  int [] block_to = getCityBlock(to);

  int result = Math.abs(block_to[0]-block_from[0]) + Math.abs(block_to[1]-block_from[1]);
  return result;


  }
}
