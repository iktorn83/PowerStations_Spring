# PowerStations
A Spring Boot application to Power Stations management with Thymeleaf frontend. 

# About
The main assumption of the project is the ability to monitor and manage the condition of power plants.
Another important element is the analysis module, which allows to analyze data and present reports in 
graphics forms. Due to security reasons there are two data sources in use - one for storing information
about power plants and the other as a credentials database. Spring Security version 5 is responsible for 
data security.

# Screenshots
No user authenticated<br>
<img src="/screenshots/1.png"  width="800" height="350"><br>  
Normal user logged<br>
<img src="/screenshots/2.png"  width="800" height="350"><br>  
Superuser loggedr<br>
<img src="/screenshots/3.png"  width="800" height="350"><br>
-
# DONE
-REST API<br>
-WEB Controller<br>
-Thymeleaf Frontend<br>
-Spring Security - roles, users, secured pages<br>
-Tests<br>

# TO-DO
-Authentication by DB credentials<br>
-Add-Event case in WEB Controller<br>
-Analyze Module<br>

# Tools
-Java<br>
-Spring Boot<br>
-Thymeleaf<br>
-Spring Security<br>
-HSQL<br>
-Junit
-Spring MockMvc
-Mockito
