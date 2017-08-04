# -*- coding: utf-8 -*-

import os
import codecs
import ConfigParser
import sys

encode = ""
path = ""
indentLevel = 0
typed=""

def listFile(path,indentLevel):

    for fileName in os.listdir(path):

        print('  ' * indentLevel + '[' + path + ']');

        absFilePath = path + '/' + fileName
        
        if(os.path.isdir(absFilePath)):
            listFile(absFilePath,indentLevel+1)
        else:
            if(not (fileName.endswith("java") or fileName.endswith("csv"))): continue
            fileDecode(absFilePath)


def fileDecode(filePath):
    global typed
    f_r = open(filePath,"r")
    filePath = filePath.replace("CommissionUT-R3","CommissionUT-R3_new");
    if(os.path.exists("C:/Users/ZSYS9RK/Desktop/tempfile.txt")):
        os.remove("C:/Users/ZSYS9RK/Desktop/tempfile.txt")
    f_t = open("C:/Users/ZSYS9RK/Desktop/tempfile.txt","a")
    if(not os.path.exists(os.path.dirname(filePath))):
        os.makedirs(os.path.dirname(filePath))
        #open(filePath,"r")
    if(os.path.exists(filePath)):
        os.remove(filePath)
    #f_w = codecs.open(filePath,"a",encode)
    f_w = open(filePath,"w")
    for line in readFileLine(f_r):
        try:
            if(isinstance(line, str)):
                f_w.write(unicode(line, encode).encode(encode))
            elif(isinstance(line,unicode)):
                f_w.write(unicode(line, encode).encode(encode))
            else:
                print("dont know!!!!!!!")
                raise(UnicodeDecodeError)
        except UnicodeDecodeError as e:
            print("###OUT###\n"+line)
            f_t.write(line)
            print("###OUT###")
            #print(e.args)
            #print(sys.exc_info())
            import traceback
            traceback.print_exc()
            if(typed != "runall"):
                typed = raw_input("enter sth >>>")
                if (typed.lower() == "end"):
                    sys.exit()
                elif (typed.lower() == "runall"):
                    typed = "runall"

    f_r.close()
    f_w.close()
    f_t.close()


def readFileLine(file):

     for line in file:
         yield line


if __name__=="__main__":
    
    inifile = ConfigParser.SafeConfigParser() 
    inifile.read('./config.ini') 
    encode = inifile.get("settings","encode")
    path = inifile.get("settings","path")

    listFile(path,indentLevel)
