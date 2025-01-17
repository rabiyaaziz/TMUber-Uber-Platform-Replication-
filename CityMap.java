import java.util.Arrays;
import java.util.Scanner;
// Rabiya Aziz
// The city consists of a grid of 9 X 9 City Blocks

// Streets are east-west (1st street to 9th street)
// Avenues are north-south (1st avenue to 9th avenue)

// Example 1 of Interpreting an address:  "34 4th Street"
// A valid address *always* has 3 parts.
// Part 1: Street/Avenue residence numbers are always 2 digits (e.g. 34).
// Part 2: Must be 'n'th or 1st or 2nd or 3rd (e.g. where n => 1...9)
// Part 3: Must be "Street" or "Avenue" (case insensitive)

// Use the first digit of the residence number (e.g. 3 of the number 34) to determine the avenue.
// For distance calculation you need to identify the the specific city block - in this example 
// it is city block (3, 4) (3rd avenue and 4th street)

// Example 2 of Interpreting an address:  "51 7th Avenue"
// Use the first digit of the residence number (i.e. 5 of the number 51) to determine street.
// For distance calculation you need to identify the the specific city block - 
// in this example it is city block (7, 5) (7th a venue and 5th street)
//
// Distance in city blocks between (3, 4) and (7, 5) is then == 5 city blocks
// i.e. (7 - 3) + (5 - 4) 

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
   
    // The following code first generates an array with all the parts of the address
    // Then I obtained the number of parts by using the .length function
    // If the number of parts is not exactly 3, method returns false
    // After all those checks I split the parts into 1,2,3
    // If part1's length is not exactly 2 and not all digits, method returns false
    // If part2's lengths is not exactly 3 and the first character is not a digit, method returns false
    // Finally I check whether part3 equals 'Street' or 'Avenue'
    // If all the above checks don't return false then method will return false

    String [] parts = getParts(address); 
    int numparts = parts.length;
    if (numparts != 3) { return false;}
    String part1 = parts[0];
    String part2 = parts[1];
    String part3 = parts[2];

    if (part1.length()!=2) 
    { 
      return false; 
    }

    else if (!allDigits(part1)) 
    {
      return false;
    }

    else if (part2.length()!=3) 
    { 
      return false; 
    }

    else if (!Character.isDigit(part2.charAt(0))) 
    { 
      return false;
    }

    else if (!part3.equalsIgnoreCase("street") && !part3.equalsIgnoreCase("avenue")) 
    { 
      return false;
    }

    return true;

  }

  // Computes the city block coordinates from an address string
  // returns an int array of size 2. e.g. [3, 4] 
  // where 3 is the avenue and 4 the street
  public static int[] getCityBlock(String address)
  {
    int[] block = {-1, -1};

    //String[] parts = address.split(" ");
    String[] parts = getParts(address);

    

    // Get first digit of street number
    int num1 = Integer.parseInt(parts[0])/10;
    int num2 = Integer.parseInt(parts[1].substring(0, 1));
    {
      block[0] = num2;
      block[1] = num1;
    }
    
    return block;

  }
  
  // Calculates the distance in city blocks between the 'from' address and 'to' address

  public static int getDistance(String from, String to)
  {

  // To get the from and to block I utilized the getCityBlock method
  // I made sure to use the absolute value when doing subtraction so my distance wouldn't be negative

  int [] from_block = getCityBlock(from);
  int [] to_block = getCityBlock(to);
  int distance = Math.abs(to_block[0]-from_block[0]) + Math.abs(to_block[1]-from_block[1]);

  return distance;


  }

  public static int getCityZone(String address)
  {

    /*
     * Method to obtain the city zone
     * Will first generate the street and avenue number
     * Zone 0 extends from 1st avenue to 5th avenue and 6th to 9th street
     * Zone 1 extends from 6th avenue to 9th avenue and 6th to 9th street
     * Zone 2 extends from 6th avenue to 9th avenue and 1st to 5th street
     * Zone 3 extends from 1st avenue to 5th avenue and 1st to 5th street
     * If address is invalid, returns -1
     */
    if (!validAddress(address))
    {
      return -1;
    }  
    else 
    {
      int[] block = getCityBlock(address);
      int street = block[1];
      int avenue = block[0];

      if ((avenue >= 1 && avenue <= 5) && (street >= 6 && street <= 9)) {
        return 0;

    } else if ((avenue >= 6 && avenue <= 9) && (street >= 6 && street <= 9)) {
        return 1;

    } else if ((avenue >= 6 && avenue <= 9) && (street >= 1 && street <= 5)) {
        return 2;

    } else if ((avenue >= 1 && avenue <= 5) && (street >= 1 && street <= 5)) {
        return 3;

    } else {
         return -1; }

    }
  }
}
