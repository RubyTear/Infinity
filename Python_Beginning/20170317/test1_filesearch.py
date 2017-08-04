# coding: utf-8

import os

def listFile(path, indentLevel):
    print('  ' * indentLevel + '[' + path + ']');       # ディレクトリ名を表示

    for fileName in os.listdir(path):                   # ディレクトリ内のファイルとディレクトリを全てループで確認
        if(fileName.startswith(".")): continue

        absFilePath = path + '/' + fileName
        if(os.path.isdir(absFilePath)): 
            listFile(absFilePath, indentLevel + 1)      # ディレクトリだったので、そのディレクトリをチェックする
        else:
            print('  ' * indentLevel + "- " + fileName) # ファイルだったので、ファイル名を表示
        
listFile('C:\\Users\\ZSYS9RK\\Desktop\\testTool_RegularCall', 0)


def show_dir(path):
    print("========================")
    for dirpath,dirnames,filenames in os.walk(path):
        for dirname in dirnames:
            print os.path.join(dirpath,dirname)


#show_dir('../../')
