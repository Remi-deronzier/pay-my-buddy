<div align="center">
  <img src="https://user-images.githubusercontent.com/49198371/186253100-30b4cd11-93a7-4acb-bb79-7d1a62a204a2.png" alt="Pay My Buddy logo" style="width:150px;height:150px;"/>
</div>


<h1 align="center">Pay My Buddy</h1>
<p align="center">
  <img src="https://user-images.githubusercontent.com/49198371/186252792-fcc84d0b-8426-456b-b400-8e22d6a75240.gif" alt="Pay My Buddy demo"/>
</p>



## Demo
👉 [See the web app](https://pay-my-buddy-remi.herokuapp.com/)
### Test credentials (if you don't want to create an account) : 
- To test a user account: 
  - username: **remax21**
  - password: **remi**
- To test an admin account: 
  - username: **PayMyBuddy**
  - password: **root**

## Overview
This project is a fictitious but very secure banking application that allow customers to transfer money to manage their finances or pay their friends.

## Features
- New users can register using a unique identifier.
- Users can log in from their accounts in the database.
- After logging in, users can add people to their contact lists from their username (if the person already exists in the database).
- A user can contribute money to their account in our application.
- From the available balance, users can make payments to any other user registered on the application.
- At any time, users can transfer the money to their personal bank accounts.
- For each transaction, a percentage of 0.5% is taken to monetize the application (Setting up a periodic task that takes place every day at 00:15 to take the daily commissions related to transactions). A backoffice for administrators has been developed.

## Security layer
- Account activation only after validation of the user's email address (via a link sent to this email address).
- Two-factor authentication with [Google Authenticator](https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2&hl=en&gl=US).
- SMS notification for each monetary exchange that represents a debit for a user.
- Possibility to change the password if you forget it via a link sent by email.
- Obligation to create a very secure password.
- Impossible to connect to 2 sessions at the same time.

### NB
Since I use a paid service to send SMS ([Twilio](https://www.twilio.com/)) and I don't want to pay, when you register your phone during the sign up stage, it is normal that the verification of your phone number fails afterwards.

## Tech stack
- **Front**: Thymeleaf, Bootstrap 5, JQuery
- **Back**: Spring (Java)
- **DB**: MySQL
- **Hosting**: Heroku

## Installation and usage
1. Be sure, you have installed a JRE to run Srping/Java project on your computer : [AdoptOpenJDK](https://adoptopenjdk.net/).
2. Be sure, you have installed Apache Maven : [Maven](https://maven.apache.org/install.html).

### Running the project
Clone this repository:
```
git clone https://github.com/Remi-deronzier/pay-my-buddy/
cd vinted-frontend
```

Install dependencies and launch the project:
```
mvn spring-boot:run
```

Enjoy !!! 😊

