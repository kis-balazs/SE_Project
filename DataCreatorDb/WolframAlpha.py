import wolframalpha


class WolframAlpha:
    def __init__(self, app_id):
        self.city_name = "Cluj-Napoca"
        self.__app_id = app_id
        self.__client = self.connection()

    def connection(self):
        """function to retrieve the connection from the wolfram alpha api, using a specific app ID"""
        return wolframalpha.Client(app_id=self.__app_id)

    def createQuery(self, date, time):
        """function to create a specific query for the different date and time in one city"""
        return "temperature in " + self.city_name + " on " + date + " " + time

    def getResponse(self, date, time):
        """function to retrieve the exact temperature, associated with some attributes as time and date from the
        Wolfram Alpha API"""
        query = self.createQuery(date=date, time=time)
        result = self.__client.query(query)
        # the exact line giving the temperature, the time and the date
        return result['pod'][1]['subpod']['img']['@alt']
