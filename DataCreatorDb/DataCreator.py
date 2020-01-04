import datetime

from WolframAlphaFetch import WolframAlphaFetch
from DatabaseConnector import DatabaseConnector

import matplotlib.pyplot as plt
import matplotlib.ticker as plticker


class DataCreator:
    """class which will connect the data gathering from the API to the Firebase database through the firebase API"""
    def __init__(self, app_id):
        self.__waf = WolframAlphaFetch(app_id)
        self.__dbc = DatabaseConnector.getInstance()

    def addOneDate(self, date):
        """function to add exactly one temperature calculated from the Wolfram API to the Firebase database,
        as a tuple of date key and temperature value"""
        date = date.replace("/", "-")
        if self.__dbc.getValue(date) is None:
            """key already existing in the database"""
            print()
            value = self.__waf.getResponse(date)
            if value != -999:
                """error case in the Wolfram API"""
                return self.__dbc.addValue(date, str(value))
            else:
                print("Wolfram API key problem!")
        else:
            print("Key " + date + " already existing")

    def addMultipleDates(self, startDate, endDate):
        """function to add every value starting from the start date until the end date"""
        startDateObj = datetime.datetime.strptime(startDate, "%d/%m/%Y").date()
        endDateObj = datetime.datetime.strptime(endDate, "%d/%m/%Y").date()
        # valid parameters
        if (endDateObj - startDateObj).days >= 0:
            date = startDateObj
            # iterate through the dates
            while date <= endDateObj:
                self.addOneDate(str(date.strftime("%d/%m/%Y")))
                # print(date.strftime("%d/%m/%Y"))
                date += datetime.timedelta(days=1)

    def addUntilRecent(self):
        """function to start from the day before the current one and go backwards by adding days one by one, until the
        first day which is added in the database already"""
        today = datetime.datetime.today().date()
        yesterday = today - datetime.timedelta(days=1)
        while self.__dbc.getValue(yesterday.strftime("%d-%m-%Y")) is None:
            self.addOneDate(str(yesterday.strftime("%d-%m-%Y")))
            yesterday -= datetime.timedelta(days=1)

    def plotter(self, startDate, endDate):
        """function to plot from a given day each day until the last one, in celsius on the y axis"""
        x = []
        startDateObj = datetime.datetime.strptime(startDate, "%d/%m/%Y").date()
        endDateObj = datetime.datetime.strptime(endDate, "%d/%m/%Y").date()
        date = startDateObj
        while date <= endDateObj:
            x.append(date.strftime("%d-%m-%Y"))
            date += datetime.timedelta(days=1)

        y = []
        for i in x:
            val = self.__dbc.getValue(i)
            y.append(float(val))

        print(y)

        fig, ax = plt.subplots()
        ax.plot(x, y)

        plt.autoscale(enable=True, axis='x')

        loc = plticker.MultipleLocator(base=len(x) / 5.5)  # this locator puts ticks at regular intervals
        ax.xaxis.set_major_locator(loc)

        plt.xlabel("days")
        plt.ylabel("temperature (°C)")
        plt.title("from " + str(startDate) + " to " + str(endDate))
        plt.grid(True)

        plt.draw()
        plt.show()
