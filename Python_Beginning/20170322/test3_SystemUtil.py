# -*- coding:utf-8 -*-

import random

import urllib,urllib2

print(__name__)

a = random.random()
print(a)

a = random.randint(1,10)
print(a)


def main():
    # 取得先URL
    url = "http://szwrs001ata:8080/res/" 
 
    # HTMLファイルを開く
    data = urllib.urlopen(url)

    # HTMLの取得      
    html = data.read()
    html = html.decode('utf-8')
    #html = html.decode('utf-8')
    
    # 表示
    print(html)
    
    # HTMLファイルを閉じる
    data.close()
   
    
if __name__ == "__main__":
    main()
