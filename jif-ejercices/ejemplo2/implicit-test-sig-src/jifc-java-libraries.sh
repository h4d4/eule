#! /bin/bash

rm -fr jif-classes/auctionPayment;
jifc -d jif-classes -sourcepath jif-src -addsigcp sig-classes -classpath 'java-classes:jif-classes' -nonrobust $@

#to code jif
