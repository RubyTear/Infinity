# coding: utf-8
a = 3
if a == 3:
	print("it's if block")
	print("it's also if block")
print("not if block")

months = [ 'Jan', 'Feb', 'Mar', 'Apr',
           'May', 'Jun', 'Jul', 'Aug',
           'Sep', 'Oct', 'Nov', 'Dec' ]

total = 123 \
      + 456 \
      + 789
print("total is" , total)

num=3.14J
bool = True
errmsg = "Can't open file"
errcode = 19042
msg = "ERROR: %s (%d)" % (errmsg, errcode)
print(msg)  

# and, or, not
print(not bool)


a = [10, 'ABC',20,30]
for n in a:
    print n
print(a[2])
print(a[1:4])
print(a[1:4:2])
print(a[-3:-1])

b = [100,200,300]

print(a + b)

#ダブル（tuple）
#タプルはリストとほぼ同じように使用できますが、要素を変更できない点が異なります。
a = (10,20,30,40)

#辞書(dict)
#{...} は、辞書(dict)と呼ばれるキーと値のペアのリストを保持します。
d = {'Yamada': 30, 'Suzuki': 40, 'Tanaka': 80}
d1 = d['Yamada']
d2 = d['Suzuki']
d3 = d['Tanaka']


for k, v in d.items():
    print k, v             # Tanaka 80, Yamada 30, Suzuki 40

for k in d.keys():
    print k, d[k]          # Suzuki 40, Yamada 30, Tanaka 80

for v in d.values():
    print v                # 80, 30, 40

for k, v in d.iteritems():
    print k, v             # Tanaka 80, Yamada 30, Suzuki 40

#map() はリストの各要素に対して処理を行い、行った結果を返します。
a = [1, 2, 3]

def double(x): return x * 2
print map(double, a)                #=> [2, 4, 6] : 関数方式
print map(lambda x: x * 2, a)       #=> [2, 4, 6] : lambda方式
print [x * 2 for x in a]            #=> [2, 4, 6] : 内包表記(後述)

#filter() はリストの各要素に対して処理を行い、処理結果が真となる要素のみを取り出します。
def isodd(x): return x % 2
print filter(isodd, a)              #=> [1, 3] : 関数方式
print filter(lambda x: x % 2, a)    #=> [1, 3] : lambda方式
print [x for x in a if x % 2]       #=> [1, 3] : 内包表記(後述)

#reduce() はリストの最初の2要素を引数に処理を呼び出し、結果と次の要素を引数に処理の呼び出しを繰り返し、単一の結果を返します。
a = [1, 2, 3, 4, 5]

def add(x, y): return x + y
print reduce(add, a)                #=> 15 : 関数方式
print reduce(lambda x, y: x + y, a) #=> 15 : lambda方式


#リストの内包表記
a = [1, 2, 3]
print [x * 2 for x in a]                        #=> [2, 4, 6]
print [x * 2 for x in a if x == 3]              #=> [6]
print [[x, x * 2] for x in a]                   #=> [[1, 2], [2, 4], [3, 6]]
print [(x, x * 2) for x in a]                   #=> [(1, 2), (2, 4), (3, 6)]

b = [4, 5, 6]
print [x * y for x in a for y in b]             #=> [4, 5, 6, 8, 10, 12, 12, 15, 18]
print [a[i] * b[i] for i in range(len(a))]      #=> [4, 10, 18]

#セット(set)
a = set(['red', 'blue', 'green'])
b = set(['green', 'yellow', 'white'])

print a                #=> set(['red', 'blue', 'green'])
print b                #=> set(['green', 'yellow', 'white'])
print a - b            #=> set(['red', 'blue'])
print a | b            #=> set(['red', 'blue', 'green', 'yellow', 'white'])
print a & b            #=> set(['green'])
print a ^ b            #=> set(['red', 'blue', 'yellow', 'white'])
print 'green' in a     #=> True
a.add('black')
print a                #=> set(['red', 'blue', 'green', 'black'])





class MyException(Exception):
    def __init__(self, file, lineno):
        self.file = file
        self.lineno = lineno

try:
    raise MyException("test.txt", 1163)
except MyException as e:
    print "MyException"
    print e.file
    print e.lineno






#関数の中でグローバル変数を参照することはできますが、代入することはできません。
#代入する場合は、global で宣言する必要があります。

count = 0                   # グローバル変数

def func():
    print count             # 参照することはできる
    global cound            # global宣言してやれば
    count += 1              # 代入することもできる

#globals() はグローバル変数、locals() はローカル変数の一覧を辞書として返却します。


#def func():
#    for k in globals().keys():
#        print "GLOBAL: %s = %s" % (k, globals()[k])
#    for k in locals().keys():
#        print "LOCAL: %s = %s" % (k, locals()[k])
#
#func()



#イテレータ(iterator)
class MyClass:
    def __init__(self):
        self.data = (1, 2, 3, 4, 5)
        self.index = 0
    def __iter__(self):
        return self
    def next(self):
        if self.index < len(self.data):
            self.index += 1
            return self.data[self.index - 1]
        else:
            raise StopIteration

for n in MyClass():
    print n                      #=> 1, 2, 3, 4, 5


#ジェネレータ(yield)
##def funcB(list):
##    for n in list:
##        yield n * 2
##
##for n in funcB([1, 2, 3, 4, 5]):
##    print n                      #=> 2, 4, 6, 8, 10
##
##
##def readfileB(f):
##    for line in f:
##        yield line.rstrip()
##
##f = open("test.txt")
##for line in readfileB(f):
##    if (line == "__END__"):
##        break
##    print line
##f.close()

#デコレータ(@)
def mydecolater(func):
    import functools
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        print "Funcname:", func.__name__
        print "Arguments:", args
        print "Keywords:", kwargs
        ret = func(*args, **kwargs)
        print "Return:", ret
        return ret
    return wrapper

@mydecolater
def func(msg1, msg2, flag=1, mode=2):
    """A sample function"""
    print "----", msg1, msg2, "----"
    return 1234

n = func("Hello", "Hello2", flag=1)
print n

print repr(func)
print func.__doc__












