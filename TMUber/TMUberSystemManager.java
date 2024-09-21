import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;
/* 
 * This class contains the main logic of the system.
 * 
 *  It keeps track of all users, drivers and service requests (RIDE or DELIVERY)
 * 
 */
public class TMUberSystemManager
{
  private ArrayList<User>   users;
  private ArrayList<Driver> drivers;

  private ArrayList<TMUberService> serviceRequests; 

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
    users   = new ArrayList<User>();
    drivers = new ArrayList<Driver>();
    serviceRequests = new ArrayList<TMUberService>(); 
    
    TMUberRegistered.loadPreregisteredUsers(users);
    TMUberRegistered.loadPreregisteredDrivers(drivers);
    
    totalRevenue = 0;
  }

  // General string variable used to store an error message when something is invalid 
  // (e.g. user does not exist, invalid address etc.)  
  // The methods below will set this errMsg string and then return false
  String errMsg = null;

  public String getErrorMessage()
  {
    return errMsg;
  }
  
  // Given user account id, find user in list of users
  // Return null if not found
  public User getUser(String accountId)
  {
    // Fill in the code
   for (User item: users)
   {
    if (item.getAccountId().equals(accountId))
    {
      return item;
    }
   }
   return null;

  }
  
  // Check for duplicate user
  private boolean userExists(User user)
  {
    return users.contains(user);
  }
  
 // Check for duplicate driver
 private boolean driverExists(Driver driver)
 {
   return drivers.contains(driver);
 }
  
  // Given a user, check if user ride/delivery request already exists in service requests
  private boolean existingRequest(TMUberService req)
  {
    return serviceRequests.contains(req);
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

  // Go through all drivers and see if one is available
  // Choose the first available driver
  // Return null if no available driver
  private Driver getAvailableDriver()
  {

    for (Driver driver : drivers)
    {
      if (driver.getStatus()== Driver.Status.AVAILABLE) 
      { return driver;}
    }
     errMsg = "No Drivers Available"; return null;
  }

  // Print Information (printInfo()) about all registered users in the system
  public void listAllUsers()
  {
    System.out.println();
    
    for (int i = 0; i < users.size(); i++)
    {
      int index = i + 1;
      System.out.printf("%-2s. ", index);
      users.get(i).printInfo();
      System.out.println(); 
    }
  }

  // Print Information (printInfo()) about all registered drivers in the system
  public void listAllDrivers()
  {
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

    for (int i = 0; i < serviceRequests.size(); i++)
    {
      int index = i+1;
      System.out.printf("%-2s. ", index);
      serviceRequests.get(i).printInfo();
      System.out.println(); 
    }
  }

  // Add a new user to the system
  public boolean registerNewUser(String name, String address, double wallet)
  {
    

   
    if (name == null || name.equals("")) {errMsg = "Invalid User Name" ;   return false; }

    else if (address == null || address.equals(""))
    {
      errMsg = "Invalid User Address";
      return false;
    }


    else if (wallet < 0 ) { errMsg = "Invalid Money in Wallet";  return false; }


    User New = new User( TMUberRegistered.generateUserAccountId(users), name, address, wallet);
    boolean exists = false;
    for (User item: users)
    { if (item.equals(New))
      {
        exists = true;
      }
    
    }

    if (exists) { errMsg = "User Already Exists In System"; return false;}

    users.add(New);
    return true;

    



  }

  // Add a new driver to the system
  public boolean registerNewDriver(String name, String carModel, String carLicencePlate)
  {
   
    if (name == null || name.equals(""))
    {
      errMsg = "Invalid Driver Name";
      return false;
    }

    else if (carModel == null || carModel.equals("")) {errMsg = "Invalid Car Model" ; return false;}
    else if (carLicencePlate == null || carLicencePlate.equals("")) {errMsg = "Invalid Car Licence Plate" ; return false;}

    Driver New = new Driver(TMUberRegistered.generateDriverId(drivers), name, carModel, carLicencePlate);

    if (!driverExists(New)){drivers.add(New); return true;}

    
    else { errMsg = "Driver Already Exists in System"; return false;}
  }

  // Request a ride. User wallet will be reduced when drop off happens
  public boolean requestRide(String accountId, String from, String to)
  {
    
    if (!CityMap.validAddress(from) || !CityMap.validAddress(to)) {errMsg = "Invalid Address"; return false;}

    for (int i = 0; i < users.size(); i ++)
    {
      User currentuser = users.get(i);
      if (Objects.equals(currentuser.getAccountId(),accountId))
      {

        int distance = CityMap.getDistance(from, to);
        if (distance <= 1 ) { errMsg = "Insufficient Travel Distance"; return false;}
        Driver driver = getAvailableDriver();
        if (driver == null) { return false;}

        TMUberRide new_ride = new TMUberRide(driver, from, to, currentuser, distance, getRideCost(distance));
        
        for (TMUberService request: serviceRequests)
        {
          if (request.getServiceType()=="RIDE")
          {
            TMUberRide ride = (TMUberRide) request;
            if (ride.equals(new_ride)) {errMsg = "User Already Has Ride Request"; return false; }
          } 
        }

        if (currentuser.getWallet()< getRideCost(distance)) { errMsg = "Insufficient Funds"; return false;}
        driver.setStatus(Driver.Status.DRIVING);

        serviceRequests.add(new_ride);

        currentuser.addRide();
        return true;

      }
    }
    errMsg = "User Account Not Found";
    return false;
  }

  // Request a food delivery. User wallet will be reduced when drop off happens
  public boolean requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId)
  {
    
    if (!CityMap.validAddress(from) || !CityMap.validAddress(to)) {errMsg = "Invalid Address"; return false;}

    for (int i = 0; i < users.size(); i ++)
    {
      User currentuser = users.get(i);
      if (Objects.equals(currentuser.getAccountId(),accountId))
      {
        int distance = CityMap.getDistance(from, to);
        if (distance <= 1 ) { errMsg = "Insufficient Travel Distance"; return false;}
        Driver driver = getAvailableDriver();
        if (driver == null) { return false;}

        TMUberDelivery new_delivery = new TMUberDelivery(driver, from, to, currentuser, distance, getDeliveryCost(distance), restaurant, foodOrderId);
        
        for (TMUberService request: serviceRequests)
        {
          if (request.getServiceType()=="DELIVERY")
          {
            TMUberDelivery delivery = (TMUberDelivery) request;
            if (delivery.equals(new_delivery)) {errMsg = "User Already Has Delivery Request at Restaurant with this Food Order"; return false; }
          }
        }
        if (currentuser.getWallet()< getDeliveryCost(distance)) { errMsg = "Insufficient Funds"; return false;}

        driver.setStatus(Driver.Status.DRIVING);

        serviceRequests.add(new_delivery);

        currentuser.addDelivery();
        return true;

      }
    }
    errMsg = "User Account Not Found";
    return false;
  
  }


  // Cancel an existing service request. 
  // parameter int request is the index in the serviceRequests array list
  public boolean cancelServiceRequest(int request)
  {
    // Check if valid request #
    // Remove request from list
    // Also decrement number of rides or number of deliveries for this user
    // since this ride/delivery wasn't completed
    int index = request-1;
    if (index < 0 || index >= serviceRequests.size() )
    { errMsg = "Invalid Request #" ; return false;}
    
    TMUberService req = serviceRequests.get(index);

    User user = req.getUser();
    if (req.getServiceType().equals("RIDE"))
    { 
      user.decrementRide();
    }

    else if (req.getServiceType().equals("DELIVERY"))
    {
      user.decrementDelivery();
    }

    serviceRequests.remove(index);
    return true;

  }
  
  // Drop off a ride or a delivery. This completes a service.
  // parameter request is the index in the serviceRequests array list
  public boolean dropOff(int request)
  {
    
    // Get the cost for the service and add to total revenues
    // Pay the driver
    // Deduct driver fee from total revenues
    // Change driver status
    // Deduct cost of service from user
    int index = request-1;
    if (index < 0 || index >= serviceRequests.size() )
    { 
      errMsg = "Invalid Request #" ; return false;
    }

    TMUberService req = serviceRequests.get(index);
    double fee = TMUberSystemManager.PAYRATE * req.getCost();

    totalRevenue += req.getCost() - fee;

    req.getDriver().pay(fee);

    req.getUser().payForService(req.getCost());

    req.getDriver().setStatus(Driver.Status.AVAILABLE);
    serviceRequests.remove(index);
    return true;

  }


  // Sort users by name
  // Then list all users
  public void sortByUserName()

  {
    Collections.sort(users, new NameComparator());
    listAllUsers();
    }

  // Helper class for method sortByUserName
  private class NameComparator implements Comparator <User>
  {
    public int compare(User user1, User user2) {
      return user1.getName().compareTo(user2.getName());
  }
  }

  // Sort users by number amount in wallet
  // Then list all users
  public void sortByWallet()
  {
    Collections.sort(users, new UserWalletComparator());
    listAllUsers();

  }
  // Helper class for use by sortByWallet
  private class UserWalletComparator implements Comparator <User>
  {
    public int compare(User user1, User user2)
    {
      return Double.compare(user1.getWallet(), user2.getWallet()); 
    }
  }

  // Sort trips (rides or deliveries) by distance
  // Then list all current service requests
  public void sortByDistance()
  {
    Collections.sort(serviceRequests);
    listAllServiceRequests();

}
}
