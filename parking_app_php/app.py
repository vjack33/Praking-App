from flask import Flask, render_template, request
from flask_restful import Api, Resource, reqparse
from flask_mysqldb import MySQL
#from django.core.serializers.json import DjangoJSONEncoder
from datetime import datetime
import json

class Userdata(Resource):

    def get(self, user_id):
        #user_id =  "'" + "user1" + "'"
        cur = mysql.connection.cursor()
        cur.execute("SELECT * FROM my_users WHERE user_id = '%s' " % (user_id))        
        row_headers=[x[0] for x in cur.description]
        row_value = cur.fetchall()
        
        json_data=[]
        for result in row_value:
            json_data.append(dict(zip(row_headers,result)))
        return json_data, 200
        
    def post(self, user_id):
        cur = mysql.connection.cursor()
        #user_id =  "'" + user_id + "'"
        user_present = cur.execute("SELECT * FROM my_users WHERE user_id = '%s' " % (user_id))
        user_data = cur.fetchall()
        #cur.close()
        if (user_present == 0):
            parser = reqparse.RequestParser()
            parser.add_argument("group_id")
            parser.add_argument("user_id")
            parser.add_argument("firstname")
            parser.add_argument("lastname")
            parser.add_argument("phone_no")
            parser.add_argument("email")
            parser.add_argument("user_type")
            #parser.add_argument("reg_date")
            params = parser.parse_args()
            
            now = datetime.now()
            setattr(params, 'reg_date', now.strftime("%d/%m/%Y %H:%M:%S"))

            user_create = cur.execute("INSERT INTO my_users (group_id, user_id, firstname, lastname, phone_no, email, user_type) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')" % ( params.group_id, params.user_id, params.firstname, params.lastname, params.phone_no, params.email, params.user_type) )
            #cur.execute("")
            mysql.connection.commit()            
            
            cur.close()
            return user_create

        else:
            return user_data

app = Flask(__name__)

app.config['MYSQL_HOST'] = 'localhost'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = ''
app.config['MYSQL_DB'] = 'parking_app_database'

api = Api(app)
mysql = MySQL(app)

api.add_resource(Userdata, "/users/", "/users/<string:user_id>")

if __name__ == '__main__':
    app.run(debug=True)
