from flask import Flask, render_template, request
from flask_restful import Api, Resource, reqparse
from flask_mysqldb import MySQL
from datetime import datetime, date
import json
from json import JSONEncoder

class Userdata(Resource):

    def get(self, user_id = 0):
        cur = mysql.connection.cursor()
        if (user_id == 0):
            cur.execute("SELECT * FROM my_users")        
        else:
            cur.execute("SELECT * FROM my_users WHERE user_id = '%s' " % (user_id))        
        row_headers=[x[0] for x in cur.description]
        row_value = cur.fetchall()
        
        user_data=[]
        for result in row_value:
            user_data.append(dict(zip(row_headers,result)))
        
        i = 0
        for x in user_data:
            user_data[i]['reg_date'] = user_data[i]['reg_date'].strftime("%d/%m/%Y %H:%M:%S")
            i = i + 1
        
        return user_data, 200
    
    #POST - For creating user id    
    def post(self, user_id):
        cur = mysql.connection.cursor()
        user_present = cur.execute("SELECT user_id FROM my_users WHERE user_id = '%s' " % (user_id))
        #user_data = cur.fetchall()
        if (user_present == 0):
            parser = reqparse.RequestParser()
            parser.add_argument("group_id")
            #parser.add_argument("user_id")
            parser.add_argument("firstname")
            parser.add_argument("lastname")
            parser.add_argument("phone_no")
            parser.add_argument("email")
            parser.add_argument("user_type")
            params = parser.parse_args()

            user_create = cur.execute("INSERT INTO my_users (group_id, user_id, firstname, lastname, phone_no, email, user_type) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')" % ( params.group_id, user_id, params.firstname, params.lastname, params.phone_no, params.email, params.user_type) )
            mysql.connection.commit()            
            cur.close()
            if (user_create > 0): 
                return 1, 201 #user created
            else:
                return 0, 204 #user not created Database failure
        else:
            return 0, 204 #user not created already present
    
    #PUT - For editing user details
    def put(self, user_id):
        cur = mysql.connection.cursor()
        user_present = cur.execute("SELECT * FROM my_users WHERE user_id = '%s' " % (user_id))
        #user_data = cur.fetchall()
        if (user_present > 0):
            parser = reqparse.RequestParser()
            parser.add_argument("group_id")
            #parser.add_argument("user_id")
            parser.add_argument("firstname")
            parser.add_argument("lastname")
            parser.add_argument("phone_no")
            parser.add_argument("email")
            parser.add_argument("user_type")
            params = parser.parse_args()
            
            #now = datetime.now()
            #setattr(params, 'mod_date', now.strftime("%d/%m/%Y %H:%M:%S"))
            user_modified = cur.execute("UPDATE my_users SET group_id = '%s', firstname = '%s', lastname = '%s', phone_no = '%s', email = '%s', user_type = '%s' WHERE my_users.user_id = '%s' " % ( params.group_id, params.firstname, params.lastname, params.phone_no, params.email, params.user_type, user_id) )
            mysql.connection.commit()            
            cur.close()
            if (user_modified > 0):
                return 1, 201 #User Modified
            else:
                return 0, 204 #Database failure
        else:
            return 0, 204 # User not present
    
    #DELETE - For deleting user details
    def delete(self, user_id):
        cur = mysql.connection.cursor()
        user_present = cur.execute("SELECT * FROM my_users WHERE user_id = '%s'  " % (user_id))
        #user_data = cur.fetchall()
        if(user_present > 0):
            user_delete = cur.execute("DELETE FROM my_users WHERE my_users.user_id = '%s' " % user_id)
            mysql.connection.commit()         
            cur.close()
            if(user_delete > 0):
                return 1, 200 #user Deteted
            else:
                return 0, 204 #DB Failure
        else:
            return 0, 204 # user not found

 
class Parkingdata(Resource):

    def get(self, parking_id = 0):
        cur = mysql.connection.cursor()
        if (parking_id == 0):
            cur.execute("SELECT * FROM my_parkings")        
        else:
            cur.execute("SELECT * FROM my_parkings WHERE parking_id = '%s' " % (parking_id))        
        
        row_headers=[x[0] for x in cur.description]
        row_value = cur.fetchall()
        
        parking_data=[]
        for result in row_value:
            parking_data.append(dict(zip(row_headers,result)))
        return parking_data, 200
        
    def post(self, parking_id):
        cur = mysql.connection.cursor()
        parking_present = cur.execute("SELECT * FROM my_parkings WHERE parking_id = '%s' " % (parking_id))
        parking_data = cur.fetchall()
        if (parking_present == 0):
            parser = reqparse.RequestParser()
            parser.add_argument("group_id")
            parser.add_argument("parking_id")
            parser.add_argument("parking_status")
            parser.add_argument("mod_by")
            params = parser.parse_args()

            parking_create = cur.execute("INSERT INTO my_parkings (group_id, parking_id, parking_status, mod_by) VALUES ('%s', '%s', '%s', '%s')" % ( params.group_id, params.parking_id, params.parking_status, params.mod_by) )
            mysql.connection.commit()            
            
            cur.close()
            return parking_create

        else:
            return parking_data
    
    def put(self, parking_id):
        cur = mysql.connection.cursor()
        parking_present = cur.execute("SELECT * FROM my_parkings WHERE parking_id = '%s' " % (parking_id))
        user_data = cur.fetchall()
        if (parking_present == 1):
            parser = reqparse.RequestParser()
            parser.add_argument("group_id")
            parser.add_argument("parking_id")
            parser.add_argument("parking_status")
            parser.add_argument("mod_by")
            params = parser.parse_args()
        
            parking_modified = cur.execute("UPDATE my_parkings SET group_id = '%s', parking_id = '%s', parking_status = '%s', mod_by = '%s' WHERE my_parkings.parking_id = '%s' " % ( params.group_id, params.parking_id, params.parking_status, params.mod_by, parking_id) )
            mysql.connection.commit()            
            cur.close()
            return parking_modified
        
        else:
            return user_data
    
    #DELETE - For deleting parking details
    def delete(self, parking_id):
        cur = mysql.connection.cursor()
        parking_present = cur.execute("SELECT * FROM my_parkings WHERE parking_id = '%s' " % (parking_id))
        parking_data = cur.fetchall()
        if (parking_present == 1):
            parking_delete = cur.execute("DELETE FROM my_parkings WHERE my_parkings.parking_id = '%s' " % parking_id)
            mysql.connection.commit()            
            cur.close()
            return parking_delete
        
        else:
            return parking_data



class Jobdata(Resource):

    def get(self, job_id):
        cur = mysql.connection.cursor()
        cur.execute("SELECT * FROM my_jobs WHERE job_id = '%s' " % (job_id))        
        row_headers=[x[0] for x in cur.description]
        row_value = cur.fetchall()
        
        job_data=[]
        for result in row_value:
            job_data.append(dict(zip(row_headers,result)))
        return job_data, 200
        
    def post(self, job_id):
        cur = mysql.connection.cursor()
        job_present = cur.execute("SELECT * FROM my_jobs WHERE job_id = '%s' " % (job_id))
        job_data = cur.fetchall()
        if (job_present == 0):
            parser = reqparse.RequestParser()
            parser.add_argument("group_id")
            parser.add_argument("job_id")
            parser.add_argument("user_id")
            parser.add_argument("parking_id")
            parser.add_argument("job_status")
            parser.add_argument("in_time")
            parser.add_argument("out_time")
            parser.add_argument("customer_name")
            parser.add_argument("customer_phone")
            parser.add_argument("customer_car_no")
            parser.add_argument("customer_license")
            parser.add_argument("mod_by")
            params = parser.parse_args()
            
            now = datetime.now()
            setattr(params, 'reg_date', now.strftime("%d/%m/%Y %H:%M:%S"))
            setattr(params, 'mod_date', now.strftime("%d/%m/%Y %H:%M:%S"))

            job_create = cur.execute("INSERT INTO my_jobs (group_id, job_id, user_id, parking_id, job_status, in_time, out_time, customer_name, customer_phone, customer_car_no, customer_license, mod_by, reg_date, mod_date) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')" % ( params.group_id, params.job_id, params.user_id, params.parking_id, params.job_status, params.in_time, params.out_time, params.customer_name, params.customer_phone, params.customer_car_no, params.customer_license, params.mod_by, params.reg_date, params.mod_date) )
            mysql.connection.commit()            
            
            cur.close()
            return job_create

        else:
            return job_data
    
    def put(self, job_id):
        cur = mysql.connection.cursor()
        job_present = cur.execute("SELECT * FROM my_jobs WHERE job_id = '%s' " % (job_id))
        job_data = cur.fetchall()
        if (job_present == 1):
            parser = reqparse.RequestParser()
            parser.add_argument("group_id")
            #parser.add_argument("job_id")
            parser.add_argument("user_id")
            parser.add_argument("parking_id")
            parser.add_argument("job_status")
            parser.add_argument("in_time")
            parser.add_argument("out_time")
            parser.add_argument("customer_name")
            parser.add_argument("customer_phone")
            parser.add_argument("customer_car_no")
            parser.add_argument("customer_license")
            parser.add_argument("mod_by")
            params = parser.parse_args()
            
            now = datetime.now()
            setattr(params, 'mod_date', now.strftime("%d/%m/%Y %H:%M:%S"))
            job_modified = cur.execute("UPDATE my_jobs SET group_id = '%s', user_id = '%s', parking_id = '%s', job_status = '%s', in_time = '%s', out_time = '%s', customer_name = '%s', customer_phone = '%s', customer_car_no = '%s', customer_license = '%s', mod_by = '%s', mod_date = '%s' WHERE my_jobs.job_id = '%s' " % ( params.group_id, params.user_id, params.parking_id, params.job_status, params.in_time, params.out_time, params.customer_name, params.customer_phone, params.customer_car_no, params.customer_license, params.mod_by, params.mod_date, job_id) )
            mysql.connection.commit()            
            cur.close()
            return job_modified
        
        else:
            return job_data
    
    def delete(self, job_id):
        cur = mysql.connection.cursor()
        job_present = cur.execute("SELECT * FROM my_jobs WHERE job_id = '%s' " % (job_id))
        job_data = cur.fetchall()
        if (job_present == 1):
            job_delete = cur.execute("DELETE FROM my_jobs WHERE my_jobs.job_id = '%s' " % job_id)
            mysql.connection.commit()            
            cur.close()
            return job_delete
        
        else:
            return job_data


app = Flask(__name__)

app.config['MYSQL_HOST'] = 'localhost'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = ''
app.config['MYSQL_DB'] = 'parking_app_database'

api = Api(app)
mysql = MySQL(app)

api.add_resource(Userdata, "/users/", "/users/<string:user_id>")
api.add_resource(Parkingdata, "/parkings/", "/parkings/<string:parking_id>")
api.add_resource(Jobdata, "/jobs/", "/jobs/<string:job_id>")

if __name__ == '__main__':
    app.run(host='192.168.5.100', port=80, debug=True)
