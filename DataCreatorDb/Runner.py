from DataCreator import DataCreator


class Runner:
    __wolfram_app_id = "E98URP-JL95T4YEYY"

    def __init__(self):
        # change app_id each year
        self.dataCreator = DataCreator(Runner.__wolfram_app_id)

    def runner(self):
        # ------------------------ 2015 -------------------------
        # self.dataCreator.addMultipleDates("04/01/2015", "01/04/2015")
        # -- missed ones: 2015 Feb 23; Mar 01; Mar 03;
        # self.dataCreator.addMultipleDates("04/01/2015", "01/04/2015")

        # self.dataCreator.addMultipleDates("02/04/2015", "27/05/2015")
        # -- missed ones: 2015 May 4; 5; 6;
        # self.dataCreator.addMultipleDates("02/04/2015", "27/05/2015")

        # self.dataCreator.addMultipleDates("28/05/2015", "01/12/2015")
        # -- missed ones: 2015 Sep 17; Oct 22; Nov 31;
        # self.dataCreator.addOneDate("17/09/2015")
        # self.dataCreator.addOneDate("22/10/2015")

        # self.dataCreator.addMultipleDates("02/12/2015", "31/12/2015")
        # -- missed ones: None

        # ------------------------ 2016 -------------------------
        # self.dataCreator.addMultipleDates("01/01/2016", "04/04/2016")
        # -- missed ones: 08/02/2016
        # self.dataCreator.addOneDate("08/02/2016")
        # already added until 14/06/2016

        # self.dataCreator.addMultipleDates("15/06/2016", "31/12/2016")
        # -- missed ones: None

        # ------------------------ 2017 -------------------------
        # self.dataCreator.addMultipleDates("01/01/2017", "31/12/2017")
        # -- missed ones: 2017 Feb 15; Apr 4; Jul 13; Aug 21, 22; Nov 2, 3, 4, 7; Dec 2, 7, 8, 9, 10
        # self.dataCreator.addMultipleDates("01/01/2017", "31/12/2017")

        # ------------------------ 2018 -------------------------
        # self.dataCreator.addMultipleDates("01/01/2018", "31/12/2018")
        # -- missed ones: 2018 Jan 28, 29, 30; Mar 7; Apr 7; Jun 6, 7, 9; Aug 9, 11 - 14;
        #                 Sep 2, 9, 13, 16, 17; Nov 14, 15, 17
        # self.dataCreator.addMultipleDates("01/01/2018", "31/12/2018")

        # ------------------------ 2019 -------------------------
        # self.dataCreator.addMultipleDates("01/01/2019", "26/12/2019")
        # --missed ones: 2019 May 31; Jul 27; Aug 2; Oct 27, 29; Nov 5; Dec 7;
        # self.dataCreator.addMultipleDates("01/01/2019", "26/12/2019")
        # self.dataCreator.addUntilRecent()  # done in 1 January

        # ------------------------ 2020 -------------------------
        self.dataCreator.addUntilRecent()  # can be run any day, will update until previous day
        #
        # self.dataCreator.plotter("01/04/2017", "05/04/2017")


r = Runner()
r.runner()
