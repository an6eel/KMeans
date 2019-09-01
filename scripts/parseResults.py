import sys
import re

if len(sys.argv) != 3:
    print("ERROR: Required data to parse.")
    exit(-1)

DATA_SCALA = sys.argv[1]
DATA_JAVA = sys.argv[2]

FIND_VALUE = "(\d+)[\.\,](\d+)"
FIND_ITER = "mean = {value}.*ci = <{value} ms, {value}.*>".format(value=FIND_VALUE)
FIND_TESTS_SCALA = r"Test group"
FIND_TESTS_JAVA = r"kmeans\."
FIND_VALUES = "iters -> (\d+).*\n.*{values}".format(values=FIND_ITER)
NUMBER =  "{number}.{decimal}"
INFO = "ITERS MEAN CI_START CI_END\n"
parseIter = lambda iter : " ".join(
                [iter[0]]+[NUMBER.format(number=iter[i],decimal=iter[i+1]) for i in range(1,len(iter),2)]
                )

def getValues(group):
    regex = re.compile(FIND_VALUES)
    result=regex.findall(group,re.S)
    values = list(map(parseIter,result))
    return "\n".join(values)

search_scala = re.split(FIND_TESTS_SCALA, DATA_SCALA, re.S)
search_java = re.split(FIND_TESTS_JAVA, DATA_JAVA, re.S)

SCALA_PAR = search_scala[1]
SCALA_SEC = search_scala[2]
JAVA_SEC = search_java[1]
JAVA_FP = search_java[2]

with open("scala_parallel","w+") as file:
    file.write(INFO)
    file.write(getValues(SCALA_PAR))

with open("scala_functional","w+") as file:
    file.write(INFO)
    file.write(getValues(SCALA_SEC))

with open("java_functional","w+") as file:
    file.write(INFO)
    file.write(getValues(JAVA_SEC))

with open("java","w+") as file:
    file.write(INFO)
    file.write(getValues(JAVA_FP))
