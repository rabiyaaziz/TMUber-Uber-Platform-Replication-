/* Rabiya Aziz
 * General class that simulates a ride or a delivery in a simple Uber app
 * 
 * This class is made abstract since we never create an object. We only create subclass objects. 
 * 
 * Implement the Comparable interface and compare two service requests based on the distance
 */

 import java.util.Comparator;

abstract public class TMUberService  implements Comparable<TMUberService> // Implementing comparable interface
{
  private String from;
  private String to;
  private User user;
  private String type;  // Currently Ride or Delivery but other services could be added      
  private int distance; // Units are City Blocks
  private double cost;  // Cost of the service
  
  public TMUberService(String from, String to, User user, int distance, double cost, String type)
  {
    this.from = from;
    this.to = to;
    this.user = user;
    this.distance = distance;
    this.cost = cost;
    this.type = type;
  }


  // Subclasses define their type (e.g. "RIDE" OR "DELIVERY") 
  abstract public String getServiceType();

  // Getters and Setters

  
  public String getFrom()
  {
    return from;
  }
  public void setFrom(String from)
  {
    this.from = from;
  }
  public String getTo()
  {
    return to;
  }
  public void setTo(String to)
  {
    this.to = to;
  }
  public User getUser()
  {
    return user;
  }
  public void setUser(User user)
  {
    this.user = user;
  }
  public int getDistance()
  {
    return distance;
  }
  public void setDistance(int distance)
  {
    this.distance = distance;
  }
  public double getCost()
  {
    return cost;
  }
  public void setCost(double cost)
  {
    this.cost = cost;
  }

  // Compares 2 service requests based on distance
  
    public int compareTo(TMUberService other)
    {
      // Returns 1 if distance > other distance
      // Returns -1 if distance < other distance
      // Returns 0 if both distances are equal

      if (this.getDistance() > other.getDistance()) 
      {
        return 1;
      }  
      else if (this.getDistance() < other.getDistance()) 
      {
        return -1;
      }
        
      else return 0;

    }
  
  // Check if 2 service requests are equal (this and other)
  // They are equal if its the same type and the same user

  public boolean equals(Object other)
  {

    // First I casted other into a TMUberService object
    // Then I checked whether both have the same service type AND user
    // If so, then method returns true
    TMUberService service = (TMUberService) other;

    return this.getServiceType() == service.getServiceType() && this.getUser() == service.getUser();
  }
  
  // Print Information 
  public void printInfo()
  {
    System.out.printf("\nType: %-9s From: %-15s To: %-15s Zone: %-15s", type, from, to, CityMap.getCityZone(from));
    System.out.print("\nUser: ");
    user.printInfo();
  }
}
