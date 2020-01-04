from WolframAlpha import WolframAlpha


class WolframAlphaFetch(WolframAlpha):

    def __init__(self, app_id):
        super().__init__(app_id)

    def getResponse(self, date, time=None):
        """create an overridden function which takes every data from the given date at specific times, and creates
        a mean value from these values"""
        try:
            if time is None:
                time = ["10:00 AM", "12:00 PM", "14:00 PM", "16:00 PM", "18:00 PM"]
            meanValue = 0
            nbOfTimes = 0
            for t in time:
                result = super().getResponse(date=date, time=t)
                print(result)
                # there is temperature data recorded for the wanted time
                if "(no data available for Cluj-Napoca, Cluj, Romania at" not in result:
                    nbOfTimes += 1.0
                    meanValue += int(str(result).split(" ")[0])
                # no data available for the whole day
                if nbOfTimes == 0.0:
                    nbOfTimes = 1.0
            print("Mean value for the day is: ", meanValue / nbOfTimes)
            return meanValue / nbOfTimes
        except Exception as e:
            print(e)
            return -999
