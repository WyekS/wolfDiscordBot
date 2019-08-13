#!/usr/bin/python3
# coding=utf-8

import time
import sys

print "This is the name of the script: ", sys.argv[0]
print "Number of arguments: ", len(sys.argv)
print "The arguments are: " , str(sys.argv)
print "Start : %s" % time.ctime()

argument = sys.argv[2]

if argument == 1 or argument == 2:
    print()
    sys.exit(0)

time.sleep(3)

sys.exit(-1)
