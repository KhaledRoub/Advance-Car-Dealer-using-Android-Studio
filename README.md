# **Advance Car Dealer App**

**Project Description:** 
A car dealer has requested you to develop an Android application that enables users to conveniently view and reserve cars either online or through a local database. The application should be user-friendly and simple in design. The required functionalities include:

## **1- Introduction Layout**
   This layout features a 'Connect' button designed to establish a connection to a server using REST to load all car types into an ArrayList.
   - If the connection is successful, proceed to the login and registration section.
   - If the connection is unsuccessful, display an error message and remain on the same layout.

## **2- Login and Registration Layout**

   This layout should include a "Login" button and a "Sign Up" button.
    
   - **Login:** Users can enter their email and password, which are registered in the database. If the credentials match, they can log in.
   - **Sign-Up:** New users can register with their email, providing various details as outlined in the sign-up page.

   The following image shows how the data should look in the mock API. If the mock API is down, try uploading the data from 'data.json' to it.

   ![](images/data_in_api.png)


## **3- Home Layout (Sign in as Normal Customer)**
   This layout should be a Navigation Drawer Activity containing a summary of the car dealer's history on its main page.

   - **Navigation Bar Functionality:**
     - Home
     - Car Menu
     - Your Reservations
     - Your Favorites
     - Special Offers
     - Profile
     - Call Us or Find Us
     - Logout

   - **Description:**
     - **Car Menu:** Display all car types, with options to add to favorites and reserve.
     - **Your Reservations:** Display all reserved cars.
     - **Your Favorites:** Display favorite cars with reservation options.
     - **Special Offers:** Showcase special deals with reservation options.
     - **Profile:** Allow users to view and edit personal information.
     - **Call Us or Find Us:** Direct contact and location information.
     - **Logout:** Log out of the profile.

## **4- Home Layout (Sign in as Admin)**
   This layout should be a Navigation Drawer Activity, featuring the profile of the admin on its main page.

   - **Navigation Bar Functionality:**
     - Delete Customers
     - Add Admin
     - View All Reserves
     - Logout

## **5- Advanced Features**
   
   - **Advanced Profile Information:** Add a profile picture and allow users to change it.
   - **Enhanced Filtering:** Filter cars based on additional criteria.
   - **User Reviews and Ratings:** Implement a system for reviews and ratings.
   - **Push Notifications:** Notify users about special offers, promotions, or updates.

## **Images from the App**
   Here are some images from the app. For more, please check the 'Images' folder.
   
   ![](images/connect_page.png)
   ![](images/login_page.png)
   ![](images/signup_page.png)
   ![](images/special_page.png)
