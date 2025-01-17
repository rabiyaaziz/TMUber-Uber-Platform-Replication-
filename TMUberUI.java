// Rabiya Aziz
// Simulation of a Simple Command-line based Uber App 

// This system supports "ride sharing" service and a delivery service
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

public class TMUberUI
{
  public static void main(String[] args)
  {
    // Create the System Manager - the main system code is in here 

    TMUberSystemManager tmuber = new TMUberSystemManager();
    
    Scanner scanner = new Scanner(System.in);
    System.out.print(">");

    // Process keyboard actions
    while (scanner.hasNextLine())
    {

    
      String action = scanner.nextLine();
    try {
      if (action == null || action.equals("")) 
      {
        System.out.print("\n>");
        continue;
      }
      // Quit the App
      else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
        return;
      // Print all the registered drivers
      else if (action.equalsIgnoreCase("DRIVERS"))  // List all drivers
      {
        tmuber.listAllDrivers(); 
      }
      // Print all the registered users
      else if (action.equalsIgnoreCase("USERS"))  // List all users
      {
        tmuber.listAllUsers(tmuber.getUserList()); 
      }
      // Print all current ride requests or delivery requests
      else if (action.equalsIgnoreCase("REQUESTS"))  // List all requests
      {
        tmuber.listAllServiceRequests(); 
      }
      // Register a new driver
      else if (action.equalsIgnoreCase("REGDRIVER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String carModel = "";
        System.out.print("Car Model: ");
        if (scanner.hasNextLine())
        {
          carModel = scanner.nextLine();
        }
        String license = "";
        System.out.print("Car License: ");
        if (scanner.hasNextLine())
        {
          license = scanner.nextLine();
        }
        String address = "";
        System.out.println("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        tmuber.registerNewDriver(name, carModel, license, address );
          System.out.println(""); 
          System.out.printf("Driver: %-15s Car Model: %-15s License Plate: %-10s", name, carModel, license);
      }
      // Register a new user
      else if (action.equalsIgnoreCase("REGUSER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        double wallet = 0.0;
        System.out.print("Wallet: ");
        if (scanner.hasNextDouble())
        {
          wallet = scanner.nextDouble();
          scanner.nextLine(); // consume nl!! Only needed when mixing strings and int/double
        }
        tmuber.registerNewUser(name, address, wallet);
          System.out.printf("User: %-15s Address: %-15s Wallet: %2.2f", name, address, wallet);
      }
      // Request a ride
      else if (action.equalsIgnoreCase("REQRIDE")) 
      {
        // Get the following information from the user (on separate lines)
        // Then use the TMUberSystemManager requestRide() method properly to make a ride request
        // "User Account Id: "      (string)
        // "From Address: "         (string)
        // "To Address: "           (string)
        String id = "";
        System.out.print("User Account ID: ");
        if (scanner.hasNextLine())
        {
          id = scanner.nextLine();
        }
        String from_address = "";
        System.out.print("From Address: ");
        if (scanner.hasNextLine())
        {
          from_address = scanner.nextLine();
        }
        String to_address = "";
        System.out.print("To Address: ");
        if (scanner.hasNextLine())
        {
          to_address = scanner.nextLine();

      }


    tmuber.requestRide(id, from_address, to_address);
     System.out.printf("RIDE for: %-15s From: %-15s To: %-15s", tmuber.getUser(id).getName(), from_address, to_address);
        
      }

      // Request a food delivery
      else if (action.equalsIgnoreCase("REQDLVY")) 
      {
        // Get the following information from the user (on separate lines)
        // Then use the TMUberSystemManager requestDelivery() method properly to make a ride request
        // "User Account Id: "      (string)
        // "From Address: "         (string)
        // "To Address: "           (string)
        // "Restaurant: "           (string)
        // "Food Order #: "         (string)
        String id = "";
        System.out.print("User Account ID: ");
        if (scanner.hasNextLine())
        {
          id = scanner.nextLine();
        }
        String from_address = "";
        System.out.print("From Address: ");
        if (scanner.hasNextLine())
        {
          from_address = scanner.nextLine();
        }
        String to_address = "";
        System.out.print("To Address: ");
        if (scanner.hasNextLine())
        {
          to_address = scanner.nextLine();

      }
      String restaurant = "";
      System.out.print("Restaurant: ");
      if (scanner.hasNextLine())
      {
        restaurant = scanner.nextLine();

    }
    String order = "";
    System.out.print("Food Order #: ");
    if (scanner.hasNextLine())
    {
      order = scanner.nextLine();

    }

      tmuber.requestDelivery(id, from_address, to_address, restaurant, order);
      System.out.printf("DELIVERY for: %-15s From: %-15s To: %-15s", tmuber.getUser(id).getName(), from_address, to_address);
        
      }
      
      // Sort users by name
      else if (action.equalsIgnoreCase("SORTBYNAME")) 
      {
        tmuber.sortByUserName();
      }
      // Sort users by number of ride they have had
      else if (action.equalsIgnoreCase("SORTBYWALLET")) 
      {
        tmuber.sortByWallet();
      }
      // Sort current service requests (ride or delivery) by distance
      
      // Cancel a current service (ride or delivery) request
      else if (action.equalsIgnoreCase("CANCELREQ")) 
      {
        int request = -1;
        System.out.print("Request #: ");
        if (scanner.hasNextInt())
        {
          request = scanner.nextInt();
          scanner.nextLine(); // consume nl character
        }
        int zone = -1;
        System.out.print("Zone #: ");
        if (scanner.hasNextInt())
        {
          zone = scanner.nextInt();
          scanner.nextLine(); // consume nl character
        }

        tmuber.cancelServiceRequest(request, zone);
        System.out.println("Service request #" + request + " cancelled");
      }
      // Drop-off the user or the food delivery to the destination address
      else if (action.equalsIgnoreCase("DROPOFF")) 
      {
        String driverId = "";
        System.out.print("Driver ID: ");
        if (scanner.hasNextInt())
        {
          driverId = scanner.next();
          scanner.nextLine(); // consume nl
        }
        tmuber.dropOff(driverId);
          System.out.println("Driver " + driverId + " Dropping off");
      }
      // Get the Current Total Revenues
      else if (action.equalsIgnoreCase("REVENUES")) 
      {
        System.out.println("Total Revenue: " + tmuber.totalRevenue);
      }
      // Unit Test of Valid City Address 
      else if (action.equalsIgnoreCase("ADDR")) 
      {
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        System.out.print(address);
        if (CityMap.validAddress(address))
          System.out.println("\nValid Address"); 
        else
          System.out.println("\nBad Address"); 
      }
      // Unit Test of CityMap Distance Method
      else if (action.equalsIgnoreCase("DIST")) 
      {
        String from = "";
        System.out.print("From: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }
        System.out.print("\nFrom: " + from + " To: " + to);
        System.out.println("\nDistance: " + CityMap.getDistance(from, to) + " City Blocks");
      }
      else if (action.equalsIgnoreCase("PICKUP"))
      {
        String driverID = "";
        System.out.print("Driver ID: ");
        if (scanner.hasNextLine())

        {
          driverID = scanner.nextLine();
        }
        tmuber.pickup(driverID);
        Driver driver = tmuber.getDriver(driverID);
        System.out.println("Driver " + driverID + " Picking Up in Zone " + driver.getZone());
      }

      else if (action.equalsIgnoreCase("DRIVETO"))
      {
        String driverID = "";
        System.out.println("Driver Id: ");
        if (scanner.hasNextLine())
        {
          driverID = scanner.nextLine();
        }
        System.out.println("Address: ");

        String address = "";

        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        
        tmuber.driveTo(driverID, address);
        System.out.println("Driver " + driverID + " Now in Zone " + CityMap.getCityZone(address));
      }
      else if (action.equalsIgnoreCase("LOADUSERS"))
      {
        String filename = "";
        System.out.println("User File: ");
        if (scanner.hasNextLine())
        {
          filename = scanner.nextLine();
        }
    
        try {tmuber.setUsers(TMUberRegistered.loadPreregisteredUsers(filename));}
        catch(FileNotFoundException e) { System.out.println("Users File:" + filename + " Not Found"); continue;}         
        System.out.println("Users Loaded");
      }

      else if (action.equalsIgnoreCase("LOADDRIVERS"))
      {
        String filename = "";
        System.out.println("Drivers File: ");

        if (scanner.hasNextLine())
        {
          filename = scanner.nextLine();
        }
  
          try {tmuber.setDrivers(TMUberRegistered.loadPreregisteredDrivers(filename));}
          catch (FileNotFoundException e) { System.out.println("Drivers File: " + filename + " Not Found"); continue;}
          System.out.println("Drivers Loaded");

      }
     
      System.out.print("\n>");

    } 
    
    // Catches any exceptions thrown (except File not found, thats handled inside the try block so they can try again), prints the corresponding message, then continues the loop for the next input 
    catch ( UserNotFoundException | UserExistsException | DriverNotFoundException | DriverExistsException | DriverUnavailableException | InvalidAddressException | InsufficientFundsException | InsufficientDistanceException | ServiceExistsException | IllegalArgumentException | IncompleteRequestException e) 
    { System.out.println(e.getMessage()); continue;}

    }
  }
}

