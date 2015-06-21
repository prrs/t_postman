import Logger
import pycps
from pycps.query import * 
from settings import *
import json

class ClusterPoint():
    
    def __init__(self):
        """ connection with DB"""
        try:
            self.logger = Logger.logging
            self.con = pycps.Connection(DB_ip, DB_name, DB_user, DB_password, DB_account)
        except pycps.ConnectionError as e:
            self.logger.error("Exception occurred in creating connection with py :%s" %(str(e)))
            
    def insert(self, doc):
        """insert and update data in DB """
        try:
            print "insert try entered"
            print doc
            print type(doc)
            k = doc.keys()[0]
            v = doc.values()
            if self.con.lookup(k).found > 0:
                present_values= self.con.retrieve(k).get_content_dict()['results']['document']
                for i in v[0]:
                    if i not in present_values:
                        present_values[i]=v[0][i]
                self.con.update({k:present_values})
                return 'data updated in db for key :%s' %str(doc.keys()[0])
            self.con.insert(doc)
            return "Data inserted into DB"
        except pycps.APIError as e:
            self.logger.error("Exception occurred in inserting doc in py :%s" %(str(e)))
            
    def search(self, data):
        """ searching data in DB"""
        try:
            query = term(and_terms(data), '')
            request = pycps.SearchRequest(self.con, query)
            request.docs = 10
            
            response = request.send()
            if response.found > 0:
                res = response.get_documents(doc_format='dict')
                res = json.dumps(res)
                res = json.loads(res)
                return res
            return "No data found"
        except pycps.APIError as e:
            self.logger.error("Exception occurred while searching in db :%s  " %(str(e)))
            
    def delete(self, key):
        """ delete data from DB"""
        try:
            self.con.delete(key)
            return "data deleted for :%s" %str(key)
        except pycps.APIError as e:
            self.logger.error("Exception occurred while deleting in db :%s  " %(str(e)))                                