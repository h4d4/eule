#! /bin/bash

rm -fr jif-classes/auctionPayment;
jifc -d jif-classes -sourcepath jif-src -addsigcp sig-classes -classpath 'jif-classes' -nonrobust $@
