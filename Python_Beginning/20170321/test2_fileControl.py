# -*- coding: utf-8 -*-

import codecs

def main():
    #ファイルを開く
    f = open("data.txt","r")
    for line in f:
        print(line)
    f.close()

if __name__=="__main__":
    main()


def main2():
    data = []
    f = open("data2.txt","r")
    for line in f:
        data += line[:-1].split("\t")

    print(data)

if __name__=="__main__":
    main2()


def main3():
    f = open("data3.txt","w")
    text = "test data"
    f.write(text)
    f.close()

    print("file w complete!!")


if __name__=="__main__":
    main3()



def main4():
    f = codecs.open("data4.txt","w","utf-8")
    text = "it is utf-8 file"
    f.write(text)
    f.close

    print("file is generated!!")


if __name__=="__main__":
    main4()



def returnmain():
    return "first","second"



if __name__=="__main__":
    x,y = returnmain()
    print(x,y)




    
