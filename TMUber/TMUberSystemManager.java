/* Rabiya Aziz
 * This class contains the main logic of the system.
 * 
 *  It keeps track of all users, drivers and service requests (RIDE or DELIVERY)
 * 
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
public class TMUberSystemManager
{
  private Map<String, User> users;
  //private ArrayList<User> users;
  private ArrayList<Driver> drivers;

  private Queue<TMUberService>[] serviceRequests;
  
  public double totalRevenue; // Total revenues accumulated via rides and deliveries
  
  // Rates per city block
  private static final double DELIVERYRATE = 1.2;
  private static final double RIDERATE = 1.5;
  // Portion of a ride/delivery cost paid to the driver
  private static final double PAYRATE = 0.1;

  //These variables are used to generate user account and driver ids
  int userAccountId = 900;
  int driverId = 700;

  public TMUberSystemManager()
  {
    users = new TreeMap<String, User>();
    drivers = new ArrayList<Driver>();
    totalRevenue = 0;
    serviceRequests = new LinkedList[4]; 
    serviceRequests[0] = new LinkedList<TMUberService>();
    serviceRequests[1] = new LinkedList<TMUberService>();
    serviceRequests[2] = new LinkedList<TMUberService>();
    serviceRequests[3] = new LinkedList<TMUberService>();

  }

  public void setUsers(ArrayList<User> userList)
  {
    for(User user: userList)
    {
      users.put(user.getAccountId(), user);
    }
  }
  
  public void setDrivers(ArrayList<Driver> drivers)
  {
    this.drivers = drivers;
  }

  public ArrayList<User> getUserList() // an arraylist version of the user map used to sort and print info
    {
      ArrayList<User> userList = new ArrayList<>();
      for (User user: users.values())
      {
        userList.add(user);
      }
      return userList;
    }
  
  public User getUser(String accountId)
  {
    
   for (String key: users.keySet())
   {
    if (key.equals(accountId))
    {
      return users.get(key); 
    }
   }
   throw new UserNotFoundException("User Account Not Found");
   
  }
  public Driver getDriver (String driverID)
  {
    for (Driver item: drivers)
    
    if (item.getId().equals(driverID))
    {
      return item; 
    }
   throw new DriverNotFoundException("Driver Account Not Found");  
  }
  
  // Checks for duplicate user
  private boolean userExists(User user)
  {
    ArrayList<User> userList = getUserList();
    return userList.contains(user);
  }
  
 // Checks for duplicate driver
 private boolean driverExists(Driver driver)
 {
   return drivers.contains(driver);
 }
  


  // Calculate the cost of a ride or of a delivery based on distance 
  private double getDeliveryCost(int distance)
  {
    return distance * DELIVERYRATE;
  }

  private double getRideCost(int distance)
  {
    return distance * RIDERATE;
  }


  // Print Information (printInfo()) about all registered users in the system
  public void listAllUsers(ArrayList<User> userList)
  {
    System.out.println();
    for (int i = 0; i < userList.size(); i++)
    {
      int index = i + 1;
      System.out.printf("%-2s. ", index);
      userList.get(i).printInfo();
      System.out.println(); 
    }
  }

  // Print Information (printInfo()) about all registered drivers in the system
  public void listAllDrivers()
  {
    // This for loop goes through each driver and prints it's info and then prints another line
    System.out.println();
    for (int i = 0; i < drivers.size(); i++)
    {
      int index = i+1;
      System.out.printf("%-2s. ", index);
      drivers.get(i).printInfo();
      System.out.println(); 
    }
  }

  // Print Information (printInfo()) about all current service requests
  public void listAllServiceRequests()
  {
    System.out.println();
    // Used same method as listAllDrivers
    for (int zone = 0; zone < serviceRequests.length; zone++)
    {
      System.out.printf("ZONE %-2s ", zone);
      System.out.println("\n======");
      {
        int index = 0;
        for (TMUberService service: serviceRequests[zone])
        {
          index ++; 
          System.out.println(index + ". ---------------------------------------------------------");
          service.printInfo();
          System.out.println("\n");
        }
      }
    }
  }
  // Add a new user to the system
  public void registerNewUser(String name, String address, double wallet)
  {

    if (name == null || name.equals("")) 
    { throw new UserNotFoundException("Invalid User Name"); } // Checks whether name parameter is not empty

     else if (!CityMap.validAddress(address)) // Checks whether valid address
      {
        throw new InvalidAddressException("Invalid User Address");
      }
    

    else if (wallet < 0 ) // Checks whether wallet is below zero
    { 
      throw new InsufficientFundsException("Invalid Money in Wallet");
    } 

    String userId = TMUberRegistered.generateUserAccountId(getUserList());
    User new_user = new User( userId, name, address, wallet); // Creates the new user with the given parameters and generates the id using the pre-defined method
    
    if (userExists(new_user)) // checks if user exists
    { 
      throw new UserExistsException("User Already Exists In System");    
    }

    users.put(userId, new_user); // Otherwise if new user doesn't exist then it will be added to the list of users

  }

  // Add a new driver to the system
  public void registerNewDriver(String name, String carModel, String carLicencePlate, String address)
  {
    if (name == null || name.equals("")) // Checks whether name parameter is not empty
    {
      throw new DriverNotFoundException("Invalid Driver Name");
    }

    else if (carModel == null || carModel.equals("")) // Checks if car model parameter is empty
    {
      throw new IllegalArgumentException("Invalid Car Model");
    } 

    else if (carLicencePlate == null || carLicencePlate.equals("")) // Checks whether license parameter is not empty
    {
      throw new IllegalArgumentException("Invalid Car License");
    } 
    else if (!CityMap.validAddress(address)) // Checks whether valid address
      {
        throw new InvalidAddressException("Invalid Driver Address");
      }
    Driver New_driver = new Driver(TMUberRegistered.generateDriverId(drivers), name, carModel, carLicencePlate, address); // Creates the new driver with the given parameters and generates the id using the pre-defined method
    

    if (driverExists(New_driver)) // checks if driver already exists

    { 

      throw new DriverExistsException("Driver Already Exists In System");    
      
    }

    drivers.add(New_driver); // if new driver doesn't exist then it will be added to the list of drivers

  }

  // Request a ride. User wallet will be reduced when drop off happens
  public void requestRide(String accountId, String from, String to)
  {
   
    User current_user = getUser(accountId);

    if (!CityMap.validAddress(from) || !CityMap.validAddress(to)) 
    {
      throw new InvalidAddressException("Invalid Address");
    } 

      
      int distance = CityMap.getDistance(from, to); // calculates the distance using the pre-defined method

        if (distance <= 1 )  // Distance has to be greater than 1 block otherwise method returns false
        { 
          throw new InsufficientDistanceException("Insufficient Travel Distance");
        }
        int zone = CityMap.getCityZone(from);
      
        TMUberRide new_ride = new TMUberRide( from, to, current_user, distance, getRideCost(distance));
        
        for (TMUberService request: serviceRequests[zone]) // Iterating through all the service requests in specified zone to check whether the new ride request already exists
        {
          if (request.getServiceType()=="RIDE")
          {
            TMUberRide ride = (TMUberRide) request;

            if (ride.equals(new_ride)) // if it exists then method returns false
            {
              { throw new ServiceExistsException("User Already Has Ride Request"); }
            } 
          } 
        }


        if (current_user.getWallet()< getRideCost(distance)) // If user doesn't have enough money for the ride, method returns false, error message is changed accordingly
        { 
          throw new InsufficientFundsException("Insufficient Funds" );
        } 

        serviceRequests[zone].add(new_ride);

        current_user.addRide(); // increments the number of rides the user has

      
    }    


  // Request a food delivery. User wallet will be reduced when drop off happens
  public void requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId)
  {
    
    /* Method to request delivery
     * Throws exception of distance is not greater than 1
     * Throws exception if service already exists 
     * Throws exception if user doesn't have enough money for request
     * If successful, request is added to users delivery count and to service requests queue
     */

    if (!CityMap.validAddress(from) || !CityMap.validAddress(to)) 
    {
      throw new InvalidAddressException("Invalid Address");

    }
    User current_user = getUser(accountId);
      
    int distance = CityMap.getDistance(from, to);
        if (distance <= 1 ) 
        { throw new InsufficientDistanceException("Insufficient Travel Distance");}

        TMUberDelivery new_delivery = new TMUberDelivery(from, to, current_user, distance, getDeliveryCost(distance), restaurant, foodOrderId);
        int zone = CityMap.getCityZone(from);
        for (TMUberService request: serviceRequests[zone])
        {
          if (request.getServiceType()=="DELIVERY")
          {
            TMUberDelivery delivery = (TMUberDelivery) request;
            if (delivery.equals(new_delivery)) 
            { throw new ServiceExistsException("User Already Has Delivery Request at Restaurant with this Food Order"); }
          }
        }
        if (current_user.getWallet() < getDeliveryCost(distance)) 
        { 
          throw new InsufficientFundsException("Insufficient Funds" );
        }


        serviceRequests[zone].add(new_delivery);

        current_user.addDelivery();

    }
  


  // Cancel an existing service request. 

  public void cancelServiceRequest(int request, int zone)
  {
    /* Takes parameters request # and zone #
     * Will remove the request at that zone by using an iterator
     */
    if (serviceRequests[zone].isEmpty()) { throw new ServiceExistsException("No Service Requests in Zone " + zone);}
    Iterator<TMUberService> requestIterator =  serviceRequests[zone].iterator();
    TMUberService req = null;
    while (requestIterator.hasNext()) 
    {
      int index = 1;
      if (index == request)
      {
        req = requestIterator.next();
        requestIterator.remove();
        break;
      }
      index ++;
    }

    if (req == null) { throw new ServiceExistsException("Invalid Request #");}
    User user = req.getUser(); // gets the user who requested this service

    if (req.getServiceType().equals("RIDE")) // if the service is a ride then the user's number of rides will be decremented
    { 
      user.decrementRide();
    }

    else if (req.getServiceType().equals("DELIVERY")) // if the service is a delivery then the user's number of delivery will be decremented
    {
      user.decrementDelivery();
    }
  }

  public void pickup(String driverID)
  {
    /*
     * Method for picking up a service request
     * Driver can pick up available requests from their own zone
     * If they have an outstanding requesting, exception will be thrown
     * If there are no requests in this zone, exception will be thrown
     */

    Driver driver = getDriver(driverID);
    int zone = CityMap.getCityZone(driver.getAddress());
    if (driver.getService() != null) { throw new IncompleteRequestException("Driver has an incomplete request");}
    if (driver.getStatus() == Driver.Status.DRIVING) {throw new DriverUnavailableException("Driver is Unavailable");}
    TMUberService service = serviceRequests[zone].poll();
    if (service == null) { throw new ServiceExistsException("No Service Request in Zone " + zone); }
    driver.setService(service);
    driver.setStatus(Driver.Status.DRIVING);
    driver.setAddress(service.getFrom());

  }
  
  
  public void dropOff(String driverId)
  {
  /*
     * Method for dropping off a service request
     * If he has not picked up a request, exception will be thrown
     * Driver is paid
     * Total revenue is updated
     * Money is deducted from user's wallet
     * Driver is set to AVAILABLE after successful dropoff
     */
    Driver driver = getDriver(driverId);
    TMUberService req = driver.getService();
    if (driver.getStatus() == Driver.Status.AVAILABLE) { throw new DriverUnavailableException("Driver Has Not Picked Up a Request");}
    if (req == null) {throw new ServiceExistsException("Driver Has No Current Service Request");}
    String toAddress = req.getTo();
    
    if (req.getServiceType().equals("RIDE")) // depending on service type, will calculate all the costs
    { double cost = getRideCost(req.getDistance());
      double fee = TMUberSystemManager.PAYRATE * cost; 
      driver.pay(fee);
      totalRevenue += cost - fee; 
      req.getUser().payForService(cost);

    }
    else if (req.getServiceType().equals("DELIVERY")) // depending on service type, will calculate all the costs
    {
      double cost = getDeliveryCost(req.getDistance());
      double fee = TMUberSystemManager.PAYRATE * cost; 
      totalRevenue += cost - fee; 
      driver.pay(fee);
      req.getUser().payForService(cost);
    }
    driver.setAddress(toAddress);   
    driver.setZone(CityMap.getCityZone(toAddress));
    driver.setStatus(Driver.Status.AVAILABLE);
    driver.setService(null);
  }

  public void driveTo(String driverId, String address)
  {
    /*
     * Method for driving to another address
     * Checks for address and driver exceptions
     * Driver address and zone is changed accordingly
     */
    Driver driver = getDriver(driverId);
    if (!CityMap.validAddress(address)) 
    {
      throw new InvalidAddressException("Invalid Address");
    }
    if (driver == null)
    {
      throw new DriverNotFoundException("Driver Account Not Found");
    }
    
      if (driver.getStatus().equals(Driver.Status.DRIVING))
      {
        throw new DriverUnavailableException("Driver is Already Driving");
      }
      

      driver.setAddress(address);
      driver.setZone(CityMap.getCityZone(address));      
      
  }
  

  public void sortByUserName()

  {
    Collections.sort(getUserList(), new NameComparator()); // sorts users alphabetically
    listAllUsers(getUserList()); // lists all users
  }

  private class NameComparator implements Comparator <User>
  {
    public int compare(User user1, User user2) 
    {
    return user1.getName().compareTo(user2.getName()); // compares alphabetically
    }
  }

 
  public void sortByWallet()
  {
    Collections.sort(getUserList(), new UserWalletComparator()); // sorts the users list by wallet
    listAllUsers(getUserList()); // lists all users

  }
  private class UserWalletComparator implements Comparator <User>
  {
    public int compare(User user1, User user2)
    {
      return Double.compare(user1.getWallet(), user2.getWallet()); // comparing wallets
    }
  }


}
// CUSTOM EXCEPTION CLASSES
class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {}
  public UserNotFoundException(String message)
  { 
    super(message);
  }
}

class UserExistsException extends RuntimeException {
  public UserExistsException() {}
  public UserExistsException(String message)
  { 
    super(message);
  }
}

class DriverNotFoundException extends RuntimeException {
  public DriverNotFoundException() {}
  public DriverNotFoundException(String message)
  { 
    super(message);
  }
}
class DriverExistsException extends RuntimeException {
  public DriverExistsException() {}
  public DriverExistsException(String message)
  { 
    super(message);
  }
}

class InvalidAddressException extends RuntimeException {
    public InvalidAddressException () {}
    public InvalidAddressException (String message) 
    {
      super(message);
    }

}
class InsufficientFundsException extends RuntimeException {

  public InsufficientFundsException() {}
  public InsufficientFundsException(String message)
  { 
    super(message);
  }
} 
class InsufficientDistanceException extends RuntimeException {
  public InsufficientDistanceException(){}
  public InsufficientDistanceException(String message )
  {
    super(message);
  }
} 

class ServiceExistsException extends RuntimeException {
  public ServiceExistsException (){}
  public ServiceExistsException (String message){super(message);}

}

class DriverUnavailableException extends RuntimeException {
  public DriverUnavailableException(){}
  public DriverUnavailableException(String message){super(message);}

}

class IncompleteRequestException extends RuntimeException {
  public IncompleteRequestException(){}
  public IncompleteRequestException(String message){super(message);}

}

