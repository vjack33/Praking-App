#!C:\Users\Dexter\AppData\Local\Programs\Python\Python38-32\python.exe
print("Content-Type: json\n")

import mysql.connector


mydb = mysql.connector.connect(
  host="localhost",
  user="root",
  passwd="",
  database="parking_app_database"
)

mycursor = mydb.cursor()
mycursor.execute("SELECT * FROM my_jobs INNER JOIN my_parkings ON my_parkings.parking_id = my_jobs.parking_id;")
result = mycursor.fetchall()
for row in result:
      print(row)
var json_str = json.dumps(mydb, indent=4)
print(json_str)

print ("Hello Python Web Browser!! This is cool!!")