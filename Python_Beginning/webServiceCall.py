# -*- coding:UTF-8 -*-
import urllib
import urllib2

def callApi():
    commissionServ = 'http://10.160.168.232:9080/DecisionServiceTest/rest/v1/FORCOMMDecisionServiceRuleApp/FORCOMMDecisionService'
    commissionServ2 = 'http://szwrs001ata:9080/DecisionServiceTest/ws/FORCOMMDecisionServiceRuleApp/FORCOMMDecisionService/v75'
    testReq = open('C:\\Users\\ZSYS9RK\\Desktop\\testTool_RegularCall\\prod_ODMWarmupScript\\requestXML\\commissionReqMsg4API.xml')
    testReq2 = open('C:\\Users\\ZSYS9RK\\Desktop\\testTool_RegularCall\\prod_ODMWarmupScript\\requestXML\\commissionReqMsg.xml')
    conn = urllib2.urlopen(commissionServ,data = testReq.read())
    resp = conn.read()
    print resp


def callSoap():
    commissionServ = 'http://10.160.168.232:9080/DecisionServiceTest/rest/v1/FORCOMMDecisionServiceRuleApp/FORCOMMDecisionService'
    commissionServ2 = 'http://szwrs001ata:9080/DecisionServiceTest/ws/FORCOMMDecisionServiceRuleApp/FORCOMMDecisionService/v75'
    testReq = open('C:\\Users\\ZSYS9RK\\Desktop\\testTool_RegularCall\\prod_ODMWarmupScript\\requestXML\\commissionReqMsg4API.xml')
    testReq2 = open('C:\\Users\\ZSYS9RK\\Desktop\\testTool_RegularCall\\prod_ODMWarmupScript\\requestXML\\commissionReqMsg.xml')
    conn = urllib.urlopen(commissionServ2,data = testReq2.read())
    resp = conn.read()
    print resp

if __name__ == '__main__':
    #callSoap()
    #callApi()
