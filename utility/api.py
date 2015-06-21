import json
import clusterpoint_db
from flask import Flask
from flask.ext.restful import Api, Resource, reqparse

app = Flask(__name__)
api = Api(app)
parser = reqparse.RequestParser()
parser.add_argument('doc', type=str)
#parser.add_argument('User-Agent', location='headers')
class Database_api(Resource):
    
    def get(self, data):
        return clusterpoint_db.ClusterPoint().search(data)
    
    def update(self):
        return None
    
    def delete(self, data):
        return clusterpoint_db.ClusterPoint().delete(data)
        

class Post(Resource):
    
    def post(self):
        args = parser.parse_args()
        print args['doc']
        doc = str((args['doc']))
        doc = json.loads(doc)
        return clusterpoint_db.ClusterPoint().insert(doc)

    
api.add_resource(Database_api, '/rest_api/<data>')
api.add_resource(Post,'/rest_api')

if __name__ =='__main__':
    app.run(debug=True, host='0.0.0.0')