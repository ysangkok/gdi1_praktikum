#!/usr/bin/env python

import sys

 
def getkeys(filename):
 keys = []
 f1= open(filename, "r")
 line = f1.readline()
 while line:
  line = f1.readline()
  (key, _, _2) = line.partition("=")
  key = key.strip()
  if key == "": continue
  if key[0:2] == "//": continue
  (prefix, _, suffix) = key.partition(".")
  if suffix == "toolTipText": continue
  if suffix == "mnemonic": continue
  if (key in keys):
    print "duplicate key! key: " + key
    sys.exit(1)
  keys.append(key)
 return keys

k1 = getkeys(sys.argv[1])
k2 = getkeys(sys.argv[2])

for x in k2:
  if x in k1: k1.remove(x)

print "in first but not second:"
for i in k1:
  print i
