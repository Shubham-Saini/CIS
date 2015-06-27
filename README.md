# Cloud Integration Service(CIS)
Note : The symbol "$$$" is used through out the file to mention some of the areas in which I feel improvements can be made.
So if you are interested just fork the project and play around, and send me a PULL request afterwards, I will be more than happy 
to provide any support you may need.

The project cloud integration service, actually integrates various cloud accounts like google drive, dropbox etc.
Integration here means, one can access various cloud drive accounts via a single login.

Technologies Used:
Backend : Java JSP, Servlets.
Database: Microsoft SQL Server.
FrontEnd: HTML, CSS, JavaScript, DHTMLx Suite, JQuery, AJAX.

Development Environment:
IDE      - Eclipse
Server   - Tomcat 7.0
Database - Microsoft SQL Server 2014  (May change it to MySql, later).

1.)  Currently only three drive services are available for integration:
      1.Google Drive
      2.DropBox
      3.Microsoft OneDrive
    $$$ So the project can be extended to include more drive accounts like amazon web services(AWS) etc.

2.)  OAuth 2.0 protocol is used for accessing resources from the above mentioned servers.

3.)  $$$ The FileManager can improved a lot(from design point of view). Currently I am using DHTMLx Suite.

4.)  All the files received are first standardized to a common format so including more drive services(like AWS) is easier,   
     as we only need to  convert the input to that standard format(DriveFile.java).
     
5.)  All the user related information that is needed during execution is also standardized(DriveUser.java), so we dont need to 
     worry about the service currently being used by the user.
     Above two helps greatly when it comes to extended the project to include more services.
     
6.) Security
     The user password is stored in hashed form, after 1000 iterations of SHA1(Secure Hashing Algorithm).(Hasing+Salting)
     $$$ Access tokens and refresh tokens should be stored the same way as the password otherwise it might result in a massive 
         security breach of user confidentiality.(I might address this pretty soon myself).
         
7.) From algorithmic point of view:
     $$$ Currently I am applying DFS(Depth First Search) twice before displaying the files of DropBox and OneDrive, it is because
     of the way these two provide file information, and the way DHTMLx Grid accepts data. This results in a massive time lag.
     I am open to ideas on how to tackle this.

--->> $$$ The area, which i think would be the most fun to implement is to make this a RESTful Web Service. This will help
          many developers who want to develop application related to cloud services. 
          Making it RESTful would make usage of this service very easy for those who want the functionality without any rigmarole.
          <<---
          
Feel free to contact me if you are facing any problem, or if you have some suggestions, or if you just want to say hi!!!.
Contact Info:
cis.helpcenter@gmail.com
     
