# C195 assignment
This application is for C195 assignment.

## Overview
This is a calendar application with JavaFX. This has MySQL database.
You need to login to the application to use the app. You can see your appointment data as a user.
This app allows you to export data from the database. It also logs to a log file when you manipulate database.


## Technical Overview
* JDBC(MySQL)
* JavaFX
* File IO
* Time(LocalTimeZone, 
* Localize
* ObservableList
* Listener
* Labmda
* Prepared statement


## For assignment
A - you have to change labels, buttons or all interface language on login into two languages.
F - you have to imiplement all exception controls. I thought I needed to implement only two of them.
I - Number of appointment types by month means that you need to sum numbers with year and month and type.
    So it should be like this. 1999-07 Type1 11.

* (A) Login and translate messages.
* (B) Customer database functions, add, update, and delete.
* (C) Appointment database functions, add, update, and delete. 
* (D) Calendar view change.
* (E) Adjust appointment times based on user's time zone.
	* Store time as UTC in the database and convert local time when it needs.
* (F) Exception controls
	* Scheduling an appointment outside business hours.
		* Business hour is set to 9 to 18.
	* Entering an incorrect username and password.
* (G) Two or more lambda expressions.
	* See below
		* CalendarController#initialize()
		* CalendarDetailController#setCustomer()
	* Note: used more lambda expressioins but I left comments only two of them.
* (H) Message is shown if there is an appointment within 15 mins when you login.
* (I) Reports functions. These functions create files for each purpose as different file name on the application directory under NetBeans project directory.
	* Number of appointment types by month =  TypeOfMonth.txt
	* Schedule for each consultant = ScheduleForEachConsul.txt
		* Note: You need to select consultant from choiceBox.
	* All customer information = AllCustomer.txt
* (J) Ability to track user activity.
	* log.txt is stored on the application root directory under NetBeans project. This logs login and database updates such as update, insert and delete.
