from firebase import firebase
from SingletonException import SingletonException


class DatabaseConnector:
    __instance = None
    __link_to_database = "https://weatherapp-265a1.firebaseio.com/"

    @staticmethod
    def getInstance():
        # object not existing yet
        if DatabaseConnector.__instance is None:
            DatabaseConnector()
        # object existing already
        return DatabaseConnector.__instance

    def __init__(self):
        """private constructor"""
        if DatabaseConnector.__instance is not None:
            raise SingletonException("This class is a singleton class!")
        else:
            DatabaseConnector.__instance = self
            # initialize database connection, handled
            self.__conn = firebase.FirebaseApplication(DatabaseConnector.__link_to_database, None)
            self.__database = "WeatherData"

    def addValue(self, key, data):
        """function to add/modify a value at a given key, as data item
        - update is not a separate operation, if it exists, it will update it, since the keys are unique"""
        try:
            self.__conn.put(self.__database, name=key, data=data)
            print(str(data) + " added successfully to database, at key position " + str(key))
        except Exception as e:
            print("Request cannot be made, check link to database, parameters!")

    def getValue(self, key):
        """function to retrieve a data field, based on the key value given as a parameter"""
        return self.__conn.get(self.__database, name=key)
