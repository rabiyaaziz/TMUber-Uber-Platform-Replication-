// Rabiya Aziz
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class TMUberRegistered
{

    // These variables are used to generate user account and driver ids
    private static int firstUserAccountID = 900;
    private static int firstDriverId = 700;

    // Generate a new user account id
    public static String generateUserAccountId(ArrayList<User> current)
    {
        return "" + firstUserAccountID + current.size();
    }

    // Generate a new driver id
    public static String generateDriverId(ArrayList<Driver> current)
    {
        return "" + firstDriverId + current.size();
    }

    public static ArrayList<User> loadPreregisteredUsers(String filename) throws FileNotFoundException
    {
        // Creates a list of users from a file
        //Uses a scanner to obtain User info from the file 
        ArrayList<User> users = new ArrayList<>();
        File userFile = new File (filename);
        Scanner scanUser = new Scanner(userFile);
        while ( scanUser.hasNextLine()) 
        {
            String name = scanUser.nextLine();
            String address = scanUser.nextLine();
            Double wallet = scanUser.nextDouble();
            if (scanUser.hasNextLine())
            {scanUser.nextLine();}
            users.add(new User(generateUserAccountId(users), name, address, wallet));
        }
        scanUser.close();
        return users;
     
    }

   
    public static ArrayList<Driver> loadPreregisteredDrivers(String filename) throws FileNotFoundException
    {
        /* Creates a list of drivers from a file
           Uses a scanner to obtain Driver info from the file 
         */ 
        

        ArrayList<Driver> drivers = new ArrayList<>();
        File driverFile = new File (filename);
        Scanner scanDriver = new Scanner(driverFile);
        while ( scanDriver.hasNextLine()) 
        {

            String name = scanDriver.nextLine();
            String carmodel = scanDriver.nextLine();
            String carlicense = scanDriver.nextLine();
            String address = scanDriver.nextLine();
            drivers.add(new Driver(generateDriverId(drivers), name,  carmodel, carlicense,address));
        }
        scanDriver.close();
        return drivers;
    
    }
}

