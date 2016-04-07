# Devel
1 clone
2 copy/rename folder **conf-template** to **/opt/fm**
3 under **/opt/fm/** fix **com.ferremundo.stt.GSettings.xml** and  **DB.ods**

# Scafold DB
1 start mongodb. Make sure the port matches the one in **/opt/fm/com.ferremundo.stt.GSettings.xml**.
2 start tomcat at 8080 or wherever.
3 go to **http://localhost:8080/gfm-nio/dbport?commandline=dbinit**. This will scafold the db and will create the user/pass root/ready.
4 go to **http://localhost:8080/gfm-nio/** and authenticate with root/ready
5 write the command **%updateproducts**[enter] 
